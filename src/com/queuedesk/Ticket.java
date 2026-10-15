package com.queuedesk;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

//abstract class: hide implementation and cannot be instantiated directly
public abstract class Ticket implements Comparable<Ticket> {
    private static int nextId =0;
    private final int id;
    private final String title;
    private String description;
    private final String requester;
    private Priority priority;
    private Status status;
    private final LocalDateTime createdAt;
    private String assignee; // null means not assigned to anyone
    private final Set<String> tags = new LinkedHashSet<>();

    protected Ticket(String title, String description,String requester,Priority priority, LocalDateTime createdAt) {
        this.id = ++nextId;
        this.title=TicketUtils.requireText(title, "title");
        this.description=(description == null) ?"" : description.trim();
        this.requester =TicketUtils.requireText(requester, "requester");
        this.priority= Objects.requireNonNull(priority, "priority must not be null");
        this.status =Status.OPEN;
        this.createdAt= Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    protected Ticket(String title, String description, String requester, Priority priority) {
        this(title, description, requester, priority, LocalDateTime.now());
    }

    public Ticket(String title, String requester) {
        this(title, "", requester, Priority.MEDIUM);
    }

    public int getId()                  { return id; }
    public String getTitle()            { return title; }
    public String getDescription()      { return description; }
    public String getRequester()        { return requester; }
    public Priority getPriority()       { return priority; }
    public Status getStatus()           { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getAssignee()         { return assignee; }
    public Set<String> getTags()        { return Collections.unmodifiableSet(tags); }

    public void setDescription(String description) {
        this.description = (description == null) ? "" : description;
    }
    public boolean isActive() {
        return status== Status.OPEN || status == Status.IN_PROGRESS;
    }
    public void setPriority(Priority priority) {
        this.priority =Objects.requireNonNull(priority, "priority must not be null");
    }
    public void setStatus(Status status) {
        this.status= Objects.requireNonNull(status, "status must not be null");
    }
    public void setAssignee(String assignee) {
        // blank and null both will be taken as unassigned
        this.assignee =(assignee ==null||assignee.isBlank())? null:assignee.trim();
    }

    public void addTag(String tag) {
        if (tag ==null||tag.isBlank()) return;
        tags.add(tag.trim().toLowerCase());
    }

    public long ageInHours() {
        return Duration.between(createdAt, LocalDateTime.now()).toHours();
    }
    public static int totalCreated() {
        return nextId;
    }

    @Override
    public int compareTo(Ticket other) {
        int byPriority= other.priority.compareTo(this.priority);
        if (byPriority!=0) return byPriority;

        int byCreated=this.createdAt.compareTo(other.createdAt);
        if (byCreated!=0) return byCreated;
        return Integer.compare(this.id, other.id);
    }

    //abstract method is declared without a body that child class have to implement until it is also an abstract class
    public abstract double estimateEffortHours();

    @Override
    public String toString() {
        return "#"+ id +"[" +priority +"/"+status +"] "+title+" (by "+requester+", " +(assignee==null ?"unassigned":"@"+assignee) + (tags.isEmpty()?"" :",tags=" +tags) +")";
    }
}