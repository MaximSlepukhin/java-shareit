package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private User booker;

    private Item item;

    private BookingStatus status;
}
