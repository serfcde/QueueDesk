package com.queuedesk;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class QueueDeskApp {

    private static final int DESCRIPTION_PREVIEW = 50;

    public static void main(String[] args) {
        try {
            run();
        } catch (InvalidTicketDataException e) {
            System.err.println("tickets.txt has bad data. " + e.getMessage());
        } catch (TicketNotFoundException e) {
            System.err.println("Lookup failed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File problem: " + e.getMessage());
        }
    }

    private static void run() throws IOException, InvalidTicketDataException {
        Repository<Ticket> repo = new Repository<>();
        for (Ticket t: TicketFileLoader.load(Path.of("tickets.txt"))) {
            repo.add(t);
        }
        System.out.println("Loaded " + repo.all().size() + " tickets.\n");

        printTriageBoard(repo.all());

        PriorityQueue<Ticket> queue = new PriorityQueue<>();
        for (Ticket t :repo.all()) {
            if (t.isActive()) queue.offer(t);
        }
        System.out.println("\nNext up:");
        for (int i =0;i<3 && !queue.isEmpty(); i++) {
            System.out.println("  " + queue.poll());
        }

        String report =ReportGenerator.generate(repo.all(), LocalDateTime.now());
        Path reportFile =Path.of("end-of-day-report.txt");
        ReportGenerator.write(report, reportFile);
        System.out.println("\n" + report);
        System.out.println("Report saved to " + reportFile.toAbsolutePath());

        Scanner in =new Scanner(System.in);
        System.out.print("\nLook up a ticket by id: ");
        if (!in.hasNextInt()) {
            System.out.println("That's not a number.");
            return;
        }
        System.out.println(repo.findById(in.nextInt()));
    }

    static void printTriageBoard(List<Ticket> tickets) {
        System.out.println("Triage Board");
        for (Ticket ticket : tickets) {
            String line =ticket +"effort "+ticket.estimateEffortHours() +"h";
            if (ticket instanceof Escalatable e) {
                line+= e.isEscalated() ?" "+e.escalationBadge():"escalatable";
            }
            System.out.println(line);
            if (!ticket.getDescription().isEmpty()) {
                System.out.println(" " +TicketUtils.truncate(ticket.getDescription(), DESCRIPTION_PREVIEW));
            }
        }
    }
}