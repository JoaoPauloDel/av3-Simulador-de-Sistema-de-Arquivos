package com.fssimulator.core;

import java.io.Serializable;

public class File extends FSNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] content = new byte[0];

    public File(String name) { super(name); }
    public File(String name, byte[] content) { super(name); this.content = content; }
    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; this.modifiedAt = java.time.LocalDateTime.now(); }
    @Override public boolean isDirectory() { return false; }
    @Override public long getSize() { return content.length; }
    public File deepCopy(String newName) { return new File(newName, content.clone()); }
    @Override public String toString() { return "[ARQUIVO] " + name + " (" + getSize() + " bytes)"; }
}