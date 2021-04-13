package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class EntityCreatingFailureException extends RuntimeException {
    
    public EntityCreatingFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EntityCreatingFailureException(String message) {
        super(message);
    }
    
    public EntityCreatingFailureException() {
        super();
    }

}
