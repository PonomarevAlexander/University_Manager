package com.foxminded.university.domain.exceptions;

@SuppressWarnings("serial")
public class HibernateException extends RuntimeException {

    public HibernateException() {
        super();
    }

    public HibernateException(String message, Throwable cause) {
        super(message, cause);
    }

    public HibernateException(String message) {
        super(message);
    }
    

}
