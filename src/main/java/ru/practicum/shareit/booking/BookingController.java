package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.Util;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(Util.USER_HEADER) Long userId,
                             @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("POST запрос на новое бронирование от пользователя с id:{}", userId);
        return bookingService.add(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(Util.USER_HEADER) Long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam boolean approved) {
        log.info("PATCH запрос на обновление статуса о бронирвоании от пользователя с id:{}", userId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader(Util.USER_HEADER) Long userId,
                                      @PathVariable Long bookingId) {
        log.info("GET запрос на получение данных о бронировании с id:{} от пользователя с id:{}", bookingId, userId);
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getAllBookingsOfOwner(@RequestHeader(Util.USER_HEADER) Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsOfOwner(userId, state);

    }

    @GetMapping("/owner")
    List<BookingDto> findAllBookingsOfOwner(@RequestHeader(Util.USER_HEADER) Long ownerId,
                                            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingsOfOwnerById(ownerId, state);
    }
}
