package com.queuedesk;

import java.time.LocalDateTime;
public class FeatureRequestTicket extends Ticket {

    private int votes;
    private final String businessJustification;

    public FeatureRequestTicket(String title, String description,String requester,Priority priority, String businessJustification) {
        this(title,description, requester,priority, businessJustification, 0,LocalDateTime.now());
    }

    public FeatureRequestTicket(String title, String description,String requester, Priority priority,String businessJustification, int votes, LocalDateTime createdAt) {
        super(title, description, requester, priority, createdAt);
        if (votes <0)throw new IllegalArgumentException("votes can not be negative");
        this.votes =votes;
        this.businessJustification=(businessJustification ==null)? "":businessJustification.trim();
    }

    public int getVotes()                    { return votes; }
    public String getBusinessJustification() { return businessJustification; }

    public void upvote() { votes++; }

    @Override
    public double estimateEffortHours() {
        return businessJustification.isBlank()?24.0 : 16.0;
    }

    @Override
    public String toString() {return "Feature "+ super.toString() +"votes=" +votes;
    }
}