package com.queuedesk;
public class InvalidTicketDataException extends Exception {
    private final int lineNumber;
    public InvalidTicketDataException(int lineNumber, String message) {
        super("Line "+lineNumber + ": "+message);
        this.lineNumber=lineNumber;
    }
    public InvalidTicketDataException(int lineNumber, String message, Throwable cause) {
        super("Line "+lineNumber + ": "+message, cause);
        this.lineNumber=lineNumber;
    }
    public int getLineNumber() { return lineNumber; }

}