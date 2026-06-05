package com.fssimulator.core;

import java.io.Serializable;
import java.util.*;

public class Journal implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<JournalEntry> entries = new ArrayList<>();
    private long nextId = 1;

    public JournalEntry begin(JournalEntry.Operation op, String path) {
        JournalEntry e = new JournalEntry(nextId++, op, path);
        entries.add(e);
        return e;
    }
    public List<JournalEntry> recoverPending() {
        List<JournalEntry> recovered = new ArrayList<>();
        for (JournalEntry e : entries) {
            if (e.getStatus() == JournalEntry.Status.PENDING) { e.abort(); recovered.add(e); }
        }
        return recovered;
    }
    public int size() { return entries.size(); }
    public long countByStatus(JournalEntry.Status s) { return entries.stream().filter(e -> e.getStatus() == s).count(); }
    public void print() { entries.forEach(System.out::println); }
}