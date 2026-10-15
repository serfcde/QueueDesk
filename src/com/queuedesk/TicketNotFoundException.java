package com.queuedesk;
public class TicketNotFoundException extends RuntimeException {

    private final int ticketId;
    public TicketNotFoundException(int ticketId) {
        super("No ticket with id " + ticketId);
        this.ticketId = ticketId;
    }
    public int getTicketId() { return ticketId; }
}