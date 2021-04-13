package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityUpdatingFailureException extends RuntimeException {

    public EntityUpdatingFailureException() {
        super();
    }

    public EntityUpdatingFailureException(String message) {
        super(message);
    }

    public EntityUpdatingFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
