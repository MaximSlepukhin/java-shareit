package ru.practicum.shareit.booking;

import lombok.ToString;

@ToString
public enum BookingStatus {
    WAITING("Новое бронирование, ожидает одобрения."),
    APPROVED("Бронирование подтверждено владельцем."),
    REJECTED("Бронирование отклонено владельцем."),

    CANCELED("Бронирвоание отменено создателем.");
    private final String title;

    BookingStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
