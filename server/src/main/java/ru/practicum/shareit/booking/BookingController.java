package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.Util;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(Util.USER_HEADER) Long userId,
                             @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("POST запрос на новое бронирование от пользователя с id:{}", userId);
        return bookingService.add(userId, bookingDtoRequest);
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
    List<BookingDto> getAllBookingsOfUser(@RequestHeader(Util.USER_HEADER) Long userId,
                                           @RequestParam(defaultValue = "ALL") String state,
                                           @RequestParam(value = "from", required = false,
                                                   defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", required = false,
                                                   defaultValue = "7") Integer size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getAllBookingsOfUser(userId, state, pageable);
    }

    @GetMapping("/owner")
    List<BookingDto> findAllBookingsOfOwner(@RequestHeader(Util.USER_HEADER) Long ownerId,
                                            @RequestParam(defaultValue = "ALL") String state,
                                            @RequestParam(value = "from", required = false,
                                                    defaultValue = "0") Integer from,
                                            @RequestParam(value = "size", required = false,
                                                    defaultValue = "10") Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return bookingService.findBookingsOfOwnerById(ownerId, state, pageable);
    }
}
