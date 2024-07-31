package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.exceptions.StateNotFoundException;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
		log.info("POST запрос на новое бронирование от пользователя с id:{}", userId);
		return bookingClient.add(userId, bookingDtoRequest);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
											   @PathVariable Long bookingId,
											   @RequestParam Boolean approved) {
		log.info("PATCH запрос на обновление статуса о бронирвоании от пользователя с id:{}", userId);
		return bookingClient.updateStatus(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> findBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
												  @PathVariable Long bookingId) {
		log.info("GET запрос на получение данных о бронировании с id:{} от пользователя с id:{}", bookingId, userId);
		return bookingClient.findBookingById(userId, bookingId);
	}

	@GetMapping
	ResponseEntity<Object> getAllBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
												 @RequestParam(name = "state", defaultValue = "all") String stateParam,
												 @RequestParam(value = "from", required = false,
														 defaultValue = "0") Integer from,
												 @RequestParam(value = "size", required = false,
														 defaultValue = "7") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new StateNotFoundException("Unknown state: " + stateParam));
		return bookingClient.getAllBookingsOfUser(userId, state, from, size);
	}

	@GetMapping("/owner")
	ResponseEntity<Object> findAllBookingsOfOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
												  @RequestParam(name = "state", defaultValue = "all") String stateParam,
												  @RequestParam(value = "from", required = false,
														  defaultValue = "0") Integer from,
												  @RequestParam(value = "size", required = false,
														  defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new StateNotFoundException("Unknown state: " + stateParam));
		return bookingClient.findBookingsOfOwnerById(ownerId, state, from, size);
	}
}
