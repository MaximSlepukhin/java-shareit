package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {

    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime end;
}
