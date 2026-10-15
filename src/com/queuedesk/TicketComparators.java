package com.queuedesk;

import java.util.Comparator;

public final class TicketComparators {

    private TicketComparators() { }   // utility class: no instances

    /** Group by person (unassigned last), most urgent first within each person. */
    public static final Comparator<Ticket> BY_ASSIGNEE_THEN_PRIORITY =
            Comparator.comparing(Ticket::getAssignee,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Ticket::getPriority, Comparator.reverseOrder())
                    .thenComparingInt(Ticket::getId);

    /** Workflow order (OPEN → CLOSED), oldest first within each status. */
    public static final Comparator<Ticket> BY_STATUS_THEN_AGE =
            Comparator.comparing(Ticket::getStatus)
                    .thenComparing(Ticket::getCreatedAt)
                    .thenComparingInt(Ticket::getId);
}