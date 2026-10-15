package com.queuedesk;

import java.util.List;

public final class TicketUtils {

    private TicketUtils() { }   // utility class: no instances

    /** Returns the ticket with the earliest createdAt. Keeps the caller's specific type. */
    public static <T extends Ticket> T oldest(List<T> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("Cannot find oldest of an empty list");
        }
        T oldest = tickets.get(0);
        for (T t : tickets) {
            if (t.getCreatedAt().isBefore(oldest.getCreatedAt())) {
                oldest = t;
            }
        }
        return oldest;
    }

    /** Trims and rejects null/blank. Used by Ticket and its subclasses. */
    public static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    /** Shortens text for DISPLAY only, never for storage. Never throws for valid maxLength. */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (maxLength < 4) {
            throw new IllegalArgumentException("maxLength must be at least 4");
        }
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}