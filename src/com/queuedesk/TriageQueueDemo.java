package com.queuedesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TriageQueueDemo {

    public static void main(String[] args) {
        BugTicket loginBug = new BugTicket("Login page 500 error", "Fails on submit", "asha", Priority.HIGH, Severity.BLOCKER, "1. Open /login  2. Submit");
        BugTicket typoBug = new BugTicket("Typo on dashboard", "Recieve -> Receive", "ravi",Priority.LOW, Severity.TRIVIAL, "Open dashboard");
        FeatureRequestTicket darkMode = new FeatureRequestTicket("Dark mode", "For night shift", "ravi", Priority.MEDIUM, "Reduces eye strain complaints");
        AccessRequestTicket dbAccess = new AccessRequestTicket("Prod DB access", "meena", "postgres-prod", AccessLevel.ADMIN);
        AccessRequestTicket wikiAccess = new AccessRequestTicket("Wiki edit access", "asha", "confluence", AccessLevel.WRITE);

        loginBug.setAssignee("divya");
        typoBug.setAssignee("dheeraj");
        dbAccess.setAssignee("ridhima");
        wikiAccess.setAssignee("   ");//blank states unassigned

        loginBug.setStatus(Status.IN_PROGRESS);

        Repository<Ticket> repo = new Repository<>();
        repo.add(loginBug);
        repo.add(typoBug);
        repo.add(darkMode);
        repo.add(dbAccess);
        repo.add(wikiAccess);

        PriorityQueue<Ticket> naturalQueue=new PriorityQueue<>();
        PriorityQueue<Ticket> assigneeQueue =new PriorityQueue<>(TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);

        for (Ticket t : repo.all()) {
            naturalQueue.offer(t);
            assigneeQueue.offer(t);
        }

        System.out.println("Serve by priority, then oldest");
        serveAll(naturalQueue);
        System.out.println();
        System.out.println("Serve by assignee, then priority ");
        serveAll(assigneeQueue);
        System.out.println();

        System.out.println("Grouped by assignee");
        Map<String, List<Ticket>> groups = repo.groupByAssignee();
        for (Map.Entry<String, List<Ticket>> entry : groups.entrySet()) {
            System.out.println(entry.getKey()+" (" + entry.getValue().size() + ")");
            for (Ticket t : entry.getValue()) System.out.println("    " + t);
        }


        System.out.println("\nFound by id: "+repo.findById(dbAccess.getId()));

        Repository<BugTicket> bugRepo =new Repository<>();
        bugRepo.add(loginBug);
        bugRepo.add(typoBug);

        BugTicket oldestBug = TicketUtils.oldest(bugRepo.all());
        System.out.println("Oldest bug severity: " + oldestBug.getSeverity());
    }

    private static void serveAll(PriorityQueue<Ticket> queue) {
        Ticket next;
        while ((next = queue.poll()) != null) {
            System.out.println("Serving " + next);
        }
    }
}