package com.queuedesk;

import java.time.LocalDateTime;
import java.util.Objects;

//child class
//a type of ticket that can be escalated
public class AccessRequestTicket extends Ticket implements Escalatable {

    //final because they cannot be changed after creation
    private final String systemName;
    private final AccessLevel accessLevel;

    private int escalationLevel =0;

    public AccessRequestTicket(String title, String requester,String systemName, AccessLevel accessLevel) {
        this(title,"",requester, Priority.MEDIUM,systemName, accessLevel,LocalDateTime.now());
    }

    public AccessRequestTicket(String title, String description, String requester,Priority priority,String systemName, AccessLevel accessLevel, LocalDateTime createdAt) {
        super(title, description, requester, priority, createdAt);
        this.systemName= TicketUtils.requireText(systemName, "systemName");
        this.accessLevel =Objects.requireNonNull(accessLevel, "accessLevel can not be null");
    }

    public String getSystemName(){
        return systemName;
    }
    public AccessLevel getAccessLevel(){
        return accessLevel;
    }

    //if child class is also abstract can leave it unimplemented
    //need to define the body of this method otherwise would not compile
    @Override
    public double estimateEffortHours() {
        return switch (accessLevel) {
            case READ-> 1.0;
            case WRITE-> 2.0;
            case ADMIN-> 4.0;
        };
    }

    //route to a senior approver
    @Override
    public void escalate() {
        if (escalationLevel < MAX_LEVEL) {
            escalationLevel++;
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
        return "ACCESS"+super.toString() +" " +accessLevel + "on "+systemName;
    }
}