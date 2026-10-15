package com.queuedesk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ReportGenerator {

    public static final long CRITICAL_SLA_HOURS = 4;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ReportGenerator() { }

    public static String generate(List<? extends Ticket> tickets, LocalDateTime reportTime) {
        // Pre-fill every enum key with 0 so empty categories still appear in the report
        Map<Status, Integer> byStatus = new EnumMap<>(Status.class);
        for (Status s : Status.values()) byStatus.put(s, 0);

        Map<Priority, Integer> byPriority = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) byPriority.put(p, 0);

        List<Ticket> openTickets = new ArrayList<>();
        List<Ticket> overdueCritical = new ArrayList<>();

        // ONE pass collects everything
        for (Ticket t : tickets) {
            byStatus.merge(t.getStatus(), 1, Integer::sum);
            byPriority.merge(t.getPriority(), 1, Integer::sum);

            if (t.getStatus() == Status.OPEN) {
                openTickets.add(t);
            }
            if (t.getPriority() == Priority.CRITICAL && t.isActive()
                    && hoursOld(t, reportTime) >= CRITICAL_SLA_HOURS) {
                overdueCritical.add(t);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========== QueueDesk end-of-day report ==========\n");
        sb.append("Generated: ").append(reportTime.format(FORMAT)).append('\n');
        sb.append("Total tickets: ").append(tickets.size()).append("\n\n");

        sb.append("By status:\n");
        for (Map.Entry<Status, Integer> e : byStatus.entrySet()) {
            sb.append(String.format("  %-12s %3d%n", e.getKey(), e.getValue()));
        }

        sb.append("\nBy priority:\n");
        for (Map.Entry<Priority, Integer> e : byPriority.entrySet()) {
            sb.append(String.format("  %-12s %3d%n", e.getKey(), e.getValue()));
        }

        sb.append("\nOldest OPEN ticket:\n");
        if (openTickets.isEmpty()) {
            sb.append("  none\n");
        } else {
            Ticket oldest = TicketUtils.oldest(openTickets);
            sb.append("  ").append(oldest)
                    .append(" (").append(hoursOld(oldest, reportTime)).append("h old)\n");
        }

        sb.append("\nOverdue CRITICAL tickets (SLA ").append(CRITICAL_SLA_HOURS).append("h):\n");
        if (overdueCritical.isEmpty()) {
            sb.append("  none\n");
        } else {
            for (Ticket t : overdueCritical) {
                sb.append("  ").append(t)
                        .append(" (").append(hoursOld(t, reportTime)).append("h old)\n");
            }
        }

        return sb.toString();
    }

    public static void write(String report, Path outputFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write(report);
        }   
    }

    private static long hoursOld(Ticket t, LocalDateTime reportTime) {
        return Duration.between(t.getCreatedAt(), reportTime).toHours();
    }
}