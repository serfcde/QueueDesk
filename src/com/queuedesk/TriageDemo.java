package com.queuedesk;

import java.util.List;

public class TriageDemo {

    public static void main(String[] args) {
        BugTicket bug = new BugTicket("Login page 500 error", "Fails on submit", "asha", Priority.HIGH, Severity.BLOCKER, "");
        FeatureRequestTicket feature = new FeatureRequestTicket("Dark mode", "For night shift", "ravi", Priority.LOW, "Reduces eye strain complaints");
        AccessRequestTicket access = new AccessRequestTicket("Prod DB access", "meena", "postgres-prod", AccessLevel.ADMIN);

        feature.upvote();
        feature.upvote();
        bug.escalate();// HIGH → CRITICAL
        bug.escalate();// already CRITICAL, only level rises
        access.escalate();// level 1, priority stays MEDIUM

        List<Ticket> board = List.of(bug, feature, access);
        printTriageBoard(board);
    }

    static void printTriageBoard(List<Ticket> tickets) {
        System.out.println("Triage Board");
        for (Ticket ticket : tickets) {
            String line =ticket.toString()+ " effort"+ticket.estimateEffortHours() +"h";

            if (ticket instanceof Escalatable e) {
                line += e.isEscalated()? " "+e.escalationBadge():" (escalatable)";
            }
            System.out.println(line);
        }
    }
}