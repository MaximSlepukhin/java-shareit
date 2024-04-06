package ru.practicum.shareit.item.itemExceptions;

public class ItemNotContainFieldException extends RuntimeException {

    public ItemNotContainFieldException(String message) {
        super(message);
    }
}