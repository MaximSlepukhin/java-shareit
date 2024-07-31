package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemExceptions.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDtoOut createRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        var now = LocalDateTime.now();
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user, now);
        itemRequest = requestRepository.save(itemRequest);
        return ItemRequestMapper.mapToItemRequestDtoOut(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        List<ItemRequest> itemRequests = requestRepository.findByRequesterId(userId);
        return setItemsForRequests(itemRequests, userId);
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        List<ItemRequest> itemRequests = requestRepository.findByRequesterIdNot(userId, pageable).getContent();

        return setItemsForRequests(itemRequests, userId);
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден."));
        List<Item> itemsDto = itemRepository.findByRequestId(requestId);
        List<ItemDto> itemDto = itemsDto.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.mapToItemRequestDto(itemRequest, itemDto, userId);
    }

    private List<ItemRequestDto> setItemsForRequests(List<ItemRequest> itemRequests, Long userId) {
        List<Long> itemRequestsIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<ItemDto> itemsDto = itemRepository.findByRequestIdIn(itemRequestsIds).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
        Map<Long, List<ItemDto>> requestsOut = itemsDto.stream()
                .collect(Collectors.groupingBy(ItemDto::getRequestId));
        List<ItemRequestDto> collectionOfItemsDto = new ArrayList<>();

        itemRequests.forEach(request -> {
            Long id = request.getId();
            if (requestsOut.containsKey(id)) {
                List<ItemDto> items = requestsOut.get(id);
                ItemRequestDto itemRequestDto = ItemRequestMapper.mapToItemRequestDto(request, items, userId);
                collectionOfItemsDto.add(itemRequestDto);
            } else {
                ItemRequestDto itemRequestDto = ItemRequestMapper.mapToItemRequestDto(request,
                        new ArrayList<>(), userId);
                collectionOfItemsDto.add(itemRequestDto);
            }
        });
        return collectionOfItemsDto;
    }
}
