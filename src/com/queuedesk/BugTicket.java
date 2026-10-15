package com.queuedesk;

import java.time.LocalDateTime;
import java.util.Objects;

public class BugTicket extends Ticket implements Escalatable {

    private final Severity severity;
    private final String stepsToReproduce;
    private int escalationLevel = 0;

    //new bug
    public BugTicket(String title,String description,String requester,Priority priority, Severity severity, String stepsToReproduce) {
        this(title, description, requester,priority, severity,stepsToReproduce, LocalDateTime.now());
    }

    //loaded bug (original timestamp)
    public BugTicket(String title,String description,String requester,Priority priority,Severity severity, String stepsToReproduce, LocalDateTime createdAt) {
        super(title, description, requester, priority, createdAt);
        this.severity=Objects.requireNonNull(severity, "severity can not be null");
        this.stepsToReproduce =(stepsToReproduce==null)?"":stepsToReproduce.trim();
    }

    public Severity getSeverity()       { return severity; }
    public String getStepsToReproduce() { return stepsToReproduce; }

    @Override
    public double estimateEffortHours() {
        double base=switch (severity) {
            case TRIVIAL-> 1.0;
            case MINOR-> 3.0;
            case MAJOR-> 6.0;
            case BLOCKER-> 14.0;
        };
        return stepsToReproduce.isBlank() ? base + 3.0 : base;
    }

    @Override
    public void escalate() {
        if (escalationLevel>=MAX_LEVEL) return;
        escalationLevel++;
        // An escalated bug also becomes more urgent, take priority to +1 step
        if (getPriority()!= Priority.CRITICAL) {
            setPriority(Priority.values()[getPriority().ordinal() + 1]);
        }
    }

    @Override
    public boolean isEscalated() {
        return escalationLevel > 0;
    }

    @Override
    public int escalationLevel() {
        return escalationLevel;
    }

    @Override
    public String toString() {
        return "BUG "+super.toString()+" severity=" +severity;
    }
}