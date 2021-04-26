package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityNotFoundException extends DaoException {
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException() {
        super();
    }

}
