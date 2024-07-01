package ru.practicum.shareit.item.itemExceptions;

public class UserNotFoundInDataBaseException extends RuntimeException {
    public UserNotFoundInDataBaseException(String message) {
        super(message);
    }

}
