package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityUpdatingException extends DaoException {

    public EntityUpdatingException() {
        super();
    }

    public EntityUpdatingException(String message) {
        super(message);
    }

    public EntityUpdatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
