package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.exceptions.BookingDateException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingStatusException;
import ru.practicum.shareit.booking.exceptions.StateNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.itemExceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.item.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.item.itemExceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userExceptions.UserIsNotOwnerException;
import ru.practicum.shareit.user.userExceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto add(Long userId, BookingDtoRequest bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new BookingDateException("Некорректно заданы даты бронирования.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + bookingDto.getItemId() + " не найдена."));
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Бронирование не возможно.");
        }
        Booking booking = BookingMapper.toBooking(item, user, bookingDto);
        if (!item.getAvailable()) {
            throw new ItemIsNotAvailableException("Бронирование не возможно.");
        }
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        User user = userRepository.findById(userId).orElse(null);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id:" + bookingId + " не найдено."));
        if (booking.getStatus().equals(BookingStatus.APPROVED)
                || booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new BookingStatusException("Бронирование имеет статус.");
        }
        var ownerId = booking.getItem().getOwner().getId();
        if (!ownerId.equals(userId)) {
            throw new UserIsNotOwnerException("Пользователь с id: " + userId + " не является владельцем вещи.");
        } else {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        }
    }

    @Override
    public BookingDto findBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Бронирование с id: " + bookingId + " не существует."));
        var bookerId = booking.getBooker().getId();
        var ownerId = booking.getItem().getOwner().getId();
        if (bookerId.equals(userId) || ownerId.equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException(
                    "Пользователь с id: " + userId + " не является ни автором бронирования, ни владельцем вещи.");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsOfUser(Long userId, String state, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id:" + userId + " не найден."));
        Collection<Booking> bookings = null;
        LocalDateTime now = LocalDateTime.now();
        BookingState enumState = null;
        try {
            enumState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateNotFoundException("Unknown state: " + state);
        }
        switch (enumState) {
            case ALL:
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId, pageable).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                        now, pageable).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, now, now, pageable)
                        .getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId,
                        now, pageable).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING, pageable).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED, pageable).getContent();
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findBookingsOfOwnerById(Long userId, String state, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id:" + userId + " не найден."));
        Collection<Booking> bookings = null;
        LocalDateTime now = LocalDateTime.now();
        try {
            BookingState enumState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateNotFoundException("Unknown state: " + state);
        }
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, pageable).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, now, pageable)
                        .getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                        now, now, pageable).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStart(userId, now, pageable)
                        .getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                        pageable).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                        pageable).getContent();
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
