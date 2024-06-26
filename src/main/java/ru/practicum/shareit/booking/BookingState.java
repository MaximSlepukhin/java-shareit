package ru.practicum.shareit.booking;

public enum BookingState {

    ALL("Все бронирования"),

    CURRENT("Текущее бронирования"),

    PAST("Завершенные бронирования"),

    FUTURE("Будущие бронирования"),

    WAITING("Новое бронирование, ожидает одобрения."),

    REJECTED("Бронирование отклонено владельцем.");

    private final String title;

    BookingState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "BookingStatus{" +
                "title='" + title + '\'' +
                "} " + super.toString();
    }
}
