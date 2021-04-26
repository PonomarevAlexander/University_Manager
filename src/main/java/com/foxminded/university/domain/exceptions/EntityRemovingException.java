package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityRemovingException extends DaoException {

    public EntityRemovingException() {
        super();
    }

    public EntityRemovingException(String message) {
        super(message);
    }
    
    public EntityRemovingException(String message, Throwable cause) {
        super(message, cause);
    }
}
