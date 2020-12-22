package edu.daemondev.psquare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PsquareNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PsquareNotFoundException(String message) {
        super(message);
    }

    public PsquareNotFoundException(String message, Throwable err) {
        super(message, err);
    }

}
