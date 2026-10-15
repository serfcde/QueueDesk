package com.queuedesk;

public class TicketDemo {

    public static void main(String[] args) {
        // Ticket is abstract now, so we create concrete subclasses.
        // Declared type is still Ticket: polymorphism at work.
        Ticket t1 = new BugTicket("VPN drops every hour",
                "Disconnects after about 60 minutes on the office Wi-Fi network",
                "asha", Priority.HIGH, Severity.MAJOR, "Connect to office Wi-Fi, wait 60 min");
        Ticket t2 = new AccessRequestTicket("Need Jira access", "ravi", "jira", AccessLevel.WRITE);

        System.out.println(t1);
        System.out.println(t2);

        // Only description, priority, status (plus assignee/tags) have setters
        t2.setPriority(Priority.CRITICAL);
        t2.setStatus(Status.IN_PROGRESS);
        t2.setAssignee("karthik");
        t2.addTag("onboarding");
        System.out.println("After update: " + t2);

        System.out.println("Total created: " + Ticket.totalCreated());
        System.out.println("t1 SLA: " + slaHours(t1.getPriority()) + "h");
        System.out.println("t2 SLA: " + slaHours(t2.getPriority()) + "h");

        // Stage 5: validation rejects blank titles
        try {
            new FeatureRequestTicket("   ", "x", "ravi", Priority.LOW, "why not");
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // Stage 5: safe truncation for display
        System.out.println("Preview: " + TicketUtils.truncate(t1.getDescription(), 30));
        System.out.println("Short text untouched: " + TicketUtils.truncate("Hi", 30));
    }

    static int slaHours(Priority p) {
        return switch (p) {
            case CRITICAL -> 4;
            case HIGH     -> 24;
            case MEDIUM   -> 72;
            case LOW      -> 168;
        };
    }
}