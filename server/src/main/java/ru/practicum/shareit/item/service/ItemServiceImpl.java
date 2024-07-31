package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.itemExceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.item.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.item.itemExceptions.NotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final RequestRepository requestRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));

        ItemRequest itemRequest = null;

        if (itemDto.getRequestId() != null) {
            itemRequest = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с id=" + itemDto.getRequestId() + " не найден."));
        }
        Item item = ItemMapper.mapToItem(itemDto, user, itemRequest);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDtoOut findItemById(Long itemId, Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с id=" + itemId + " не найден."));
        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId,
                BookingStatus.APPROVED, pageable).getContent();

        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentDtoOut> commentsOut = comments.stream()
                .map(CommentMapper::mapToCommentDtoOut)
                .collect(Collectors.toList());

        var now = LocalDateTime.now();
        return ItemMapper.mapToItemDtoOut(item, getLastBooking(now, bookings),
                getSecondBooking(now, bookings), commentsOut);
    }

    @Override
    public List<ItemDtoOut> findItemsOfUser(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        Collection<Item> items = itemRepository.findByOwnerId(userId);

        List<Long> itemsIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        Collection<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId,
                BookingStatus.APPROVED, pageable).getContent();

        List<Comment> commentList = commentRepository.findByItemIdIn(itemsIds);
        var now = LocalDateTime.now();

        List<ItemDtoOut> itemDtos = new ArrayList<>();
        boolean firstItem = true;
        for (Item item : items) {
            ItemDtoOut itemDto = ItemMapper.mapToItemDtoOut(item,
                    firstItem ? getLastBooking(now, bookings) : null,
                    firstItem ? getSecondBooking(now, bookings) : null,
                    commentRepository.findByItemId(item.getId()).stream()
                            .map(CommentMapper::mapToCommentDtoOut)
                            .collect(Collectors.toList()));
            itemDtos.add(itemDto);
            firstItem = false;
        }
        return itemDtos;
    }

    private BookingForItemDto getLastBooking(LocalDateTime now, Collection<Booking> bookings) {
        if (!bookings.isEmpty()) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(now))
                    .max(Comparator.comparing(Booking::getStart))
                    .map(BookingMapper::toBookingForItemDto)
                    .orElse(null);
        }
        return null;
    }

    private BookingForItemDto getSecondBooking(LocalDateTime now, Collection<Booking> bookings) {
        if (!bookings.isEmpty()) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .map(BookingMapper::toBookingForItemDto)
                    .orElse(null);
        }
        return null;
    }

    @Override
    @Transactional
    public ItemDto updateItem(Map<String, String> itemUpdate, Long userId, Long itemId) {
        Item itemForUpdate = (itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с id=" + itemId + " не найден.")));
        if (!itemForUpdate.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId +
                    " не является собственником вещи с id: " + itemId);
        }

        for (Map.Entry<String, String> entry : itemUpdate.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name":
                    itemForUpdate.setName(value);
                    break;
                case "description":
                    itemForUpdate.setDescription(value);
                    break;
                case "available":
                    itemForUpdate.setAvailable(Boolean.parseBoolean(value));
                    break;
            }
        }
        return ItemMapper.mapToItemDto(itemRepository.save(itemForUpdate));
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text, Pageable pageable) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        log.debug("Предмет по запросу найден.");
        var name = text;
        var description = text;
        Collection<Item> items = itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailable(name, description, true, pageable)
                .getContent();
        return items.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoOut addComment(Long userId, CommentDto commentDto, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Предмет с id=" + itemId + " не найден."));
        var now = LocalDateTime.now();
        Collection<Booking> bookings = bookingRepository.findByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(userId,
                BookingStatus.APPROVED, now);
        List<Long> itemsIds = bookings
                .stream()
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors.toList());
        if (!itemsIds.contains(itemId)) {
            throw new ItemIsNotAvailableException("У пользователя нет бронирований с id:" + itemId);
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        return CommentMapper.mapToCommentDtoOut(commentRepository.save(comment));
    }
}
