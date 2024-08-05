package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
public class BookingDtoRequest {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}