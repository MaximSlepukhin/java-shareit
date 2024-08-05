package ru.practicum.shareit.user.userExceptions;

public class UserIsNotOwnerException extends RuntimeException {

    public UserIsNotOwnerException(String message) {
        super(message);
    }
}
