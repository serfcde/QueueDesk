package com.queuedesk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

//T must be a Ticket or subclass of it
//Repository is kind of storage manager for the tickets
public class Repository<T extends Ticket> {

    public static final String UNASSIGNED = "(unassigned)";
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(Objects.requireNonNull(item, "cannot add null ticket"));
    }
    public List<T> all() {
        return Collections.unmodifiableList(items);
    }

    public T findById(int id) {
        for (T item :items) {
            if (item.getId()== id) {
                return item;
            }
        }
        throw new TicketNotFoundException(id);
    }
    public Map<String, List<T>> groupByAssignee() {
        Map<String, List<T>> groups = new TreeMap<>();
        for (T item : items) {
            String key = (item.getAssignee()==null) ? UNASSIGNED : item.getAssignee();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return groups;
    }
}