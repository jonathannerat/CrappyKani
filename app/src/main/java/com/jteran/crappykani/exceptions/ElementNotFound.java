package com.jteran.crappykani.exceptions;

import java.io.IOException;

public class ElementNotFound extends IOException {
    public ElementNotFound() {
    }

    public ElementNotFound(String message) {
        super(message);
    }

    public ElementNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementNotFound(Throwable cause) {
        super(cause);
    }
}
