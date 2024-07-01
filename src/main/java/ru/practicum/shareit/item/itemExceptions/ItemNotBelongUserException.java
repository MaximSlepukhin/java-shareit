package ru.practicum.shareit.item.itemExceptions;

public class ItemNotBelongUserException extends RuntimeException {
    public ItemNotBelongUserException(String message) {
        super(message);
    }

}