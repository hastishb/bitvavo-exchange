package org.example.validator;

public class OrderLineValidationException extends RuntimeException {
    private final String code;
    public OrderLineValidationException(String code, String message, int lineNumber) {
        super(message + " at line number " + lineNumber);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
