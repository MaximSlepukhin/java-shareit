package ru.practicum.shareit.user.userExceptions;

public class UserEmailNotCorrectException extends RuntimeException {
    public UserEmailNotCorrectException(String message) {
        super(message);
    }
}

