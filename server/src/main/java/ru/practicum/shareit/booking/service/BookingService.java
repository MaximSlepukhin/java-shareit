package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {

    BookingDto add(Long userId, BookingDtoRequest bookingDtoOut);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto findBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookingsOfUser(Long userId, String state, Pageable pageable);

    List<BookingDto> findBookingsOfOwnerById(Long userId, String state, Pageable pageable);
}
