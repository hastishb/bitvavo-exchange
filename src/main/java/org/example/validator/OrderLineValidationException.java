package org.example.validator;

public class OrderLineValidationException extends RuntimeException {
    private final String code;
    public OrderLineValidationException(String code, String message, String line) {
        super(message + "in line: " + line);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
