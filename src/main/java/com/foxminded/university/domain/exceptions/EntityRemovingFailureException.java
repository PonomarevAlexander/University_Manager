package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityRemovingFailureException extends DaoException {

    public EntityRemovingFailureException() {
        super();
    }

    public EntityRemovingFailureException(String message) {
        super(message);
    }
    
    public EntityRemovingFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
