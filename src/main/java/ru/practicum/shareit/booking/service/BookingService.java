package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;


public interface BookingService {

    BookingDto add(BookingDtoRequest bookingDtoOut, Long userId);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto findBookingById(Long userId, Long bookingId);

    List<BookingDto> getAllBookingsOfOwner(Long userId, String state);

    List<BookingDto> findBookingsOfOwnerById(Long userId, String state);
}
