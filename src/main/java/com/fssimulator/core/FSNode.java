package com.fssimulator.core;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class FSNode implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected LocalDateTime createdAt = LocalDateTime.now();
    protected LocalDateTime modifiedAt = LocalDateTime.now();

    public FSNode(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; this.modifiedAt = LocalDateTime.now(); }
    public abstract boolean isDirectory();
    public abstract long getSize();
}