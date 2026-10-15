package com.queuedesk;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TicketFileLoader {
    public static List<Ticket> load(Path file)throws IOException, InvalidTicketDataException {
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank() || line.startsWith("#")) continue;
                tickets.add(parseLine(line, lineNumber));
            }
        }
        return tickets;
    }
    private static Ticket parseLine(String line, int lineNumber) throws InvalidTicketDataException {
        String[] f = line.split("\\|", -1);
        if (f.length !=11) {
            throw new InvalidTicketDataException(lineNumber, "expected 11 fields but found " + f.length);
        }
        for (int i = 0; i < f.length; i++) {
            f[i] = f[i].trim();
        }
        try {
            Priority priority =Priority.valueOf(f[4].toUpperCase());
            Status status =Status.valueOf(f[5].toUpperCase());
            LocalDateTime createdAt =LocalDateTime.parse(f[6]);

            Ticket ticket = switch (f[0].toUpperCase()) {
                case "BUG" -> new BugTicket(f[1], f[2], f[3], priority,
                        Severity.valueOf(f[9].toUpperCase()), f[10], createdAt);
                case "FEATURE" -> new FeatureRequestTicket(f[1], f[2], f[3], priority,
                        f[9], Integer.parseInt(f[10]), createdAt);
                case "ACCESS" -> new AccessRequestTicket(f[1], f[2], f[3], priority,
                        f[9], AccessLevel.valueOf(f[10].toUpperCase()), createdAt);
                default -> throw new InvalidTicketDataException(lineNumber,
                        "unknown ticket type '" + f[0] + "'");
            };
            ticket.setStatus(status);
            ticket.setAssignee(f[7]);
            for (String tag : f[8].split(",")) {
                ticket.addTag(tag);
            }
            return ticket;
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new InvalidTicketDataException(lineNumber, e.getMessage(), e);
        }
    }
}