package com.mantzavelas.bookworm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidIsbnException extends RuntimeException {

    public InvalidIsbnException() {
    }

    public InvalidIsbnException(String message) {
        super(message);
    }
}
