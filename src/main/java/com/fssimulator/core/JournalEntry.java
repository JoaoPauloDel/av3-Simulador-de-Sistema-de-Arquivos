package com.fssimulator.core;

import java.io.Serializable;
import java.time.LocalDateTime;

public class JournalEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Status { PENDING, COMMITTED, ABORTED }
    public enum Operation { CREATE_FILE, CREATE_DIR, DELETE_FILE, DELETE_DIR, COPY_FILE, COPY_DIR, WRITE_FILE, RENAME_FILE, RENAME_DIR }

    private final long id;
    private final Operation operation;
    private final String path;
    private Status status = Status.PENDING;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public JournalEntry(long id, Operation op, String path) { this.id = id; this.operation = op; this.path = path; }
    public void commit() { this.status = Status.COMMITTED; }
    public void abort() { this.status = Status.ABORTED; }
    public Status getStatus() { return status; }
    @Override public String toString() { return String.format("#%04d [%s] %s -> %s", id, status, operation, path); }
}