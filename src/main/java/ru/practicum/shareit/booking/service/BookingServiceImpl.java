package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.bookingExceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.exceptions.BookingDateException;
import ru.practicum.shareit.booking.exceptions.BookingStatusException;
import ru.practicum.shareit.booking.exceptions.StateNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.itemExceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.item.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
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
    public BookingDto add(BookingDtoRequest bookingDtoOut, Long userId) {
        if (bookingDtoOut.getStart().isAfter(bookingDtoOut.getEnd())
                || bookingDtoOut.getStart().isEqual(bookingDtoOut.getEnd())) {
            throw new BookingDateException("Некорректно заданы даты бронирования");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден."));
        Item item = itemRepository.findById(bookingDtoOut.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id=" + bookingDtoOut.getItemId() + " не найдена."));
        if (item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("");
        }
            Booking booking = BookingMapper.toBooking(item, user, bookingDtoOut);
        if (!item.getAvailable()) {
            throw new ItemIsNotAvailableException("Бронирование не возможно");
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
            throw new UserNotFoundException("Пользователь с id: " + userId + " не является владельцем вещи");
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
    public List<BookingDto> getAllBookingsOfOwner(Long userId, String state) {
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
                bookings = bookingRepository.findByBookerIdOrderByStart(userId);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStart(userId, now);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(userId,
                        now, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStart(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findBookingsOfOwnerById(Long userId, String state) {
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
                bookings = bookingRepository.findByItemOwnerIdOrderByStart(userId);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStart(userId, now);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStart(userId,
                        now, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStart(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
