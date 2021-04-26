package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityNotCreatedException extends DaoException {
    
    public EntityNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EntityNotCreatedException(String message) {
        super(message);
    }
    
    public EntityNotCreatedException() {
        super();
    }

}
