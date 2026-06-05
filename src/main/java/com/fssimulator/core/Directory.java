package com.fssimulator.core;

import java.io.Serializable;
import java.util.*;

public class Directory extends FSNode implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, FSNode> children = new LinkedHashMap<>();

    public Directory(String name) { super(name); }
    public boolean contains(String name) { return children.containsKey(name); }
    public FSNode get(String name) { return children.get(name); }
    public void put(FSNode node) { children.put(node.getName(), node); }
    public void remove(String name) { children.remove(name); }
    public Collection<FSNode> listChildren() { return children.values(); }
    @Override public boolean isDirectory() { return true; }
    @Override public long getSize() { return children.values().stream().mapToLong(FSNode::getSize).sum(); }
    public Directory deepCopy(String newName) {
        Directory copy = new Directory(newName);
        for (FSNode child : children.values()) {
            copy.put(child.isDirectory() ? ((Directory) child).deepCopy(child.getName()) : ((File) child).deepCopy(child.getName()));
        }
        return copy;
    }
    @Override public String toString() { return "[PASTA] " + name + " (" + getSize() + " bytes)"; }
}