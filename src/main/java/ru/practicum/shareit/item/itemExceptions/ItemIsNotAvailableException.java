package ru.practicum.shareit.item.itemExceptions;

public class ItemIsNotAvailableException extends RuntimeException {
    public ItemIsNotAvailableException(String message) {
        super(message);
    }

}
