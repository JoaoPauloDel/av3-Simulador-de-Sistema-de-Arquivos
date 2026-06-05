package com.fssimulator.core;

import java.io.*;
import java.util.*;

public class FileSystemSimulator implements Serializable {
    private static final long serialVersionUID = 1L;
    private Directory root = new Directory("/");
    private Journal journal = new Journal();
    private final String imagePath;

    public static FileSystemSimulator open(String path) throws IOException {
        java.io.File f = new java.io.File(path);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                FileSystemSimulator fs = new FileSystemSimulator(path);
                fs.root = (Directory) ois.readObject();
                fs.journal = (Journal) ois.readObject();
                fs.journal.recoverPending();
                return fs;
            } catch (Exception e) { throw new IOException("Erro ao ler arquivo .jfs"); }
        }
        FileSystemSimulator fs = new FileSystemSimulator(path);
        fs.save();
        return fs;
    }

    private FileSystemSimulator(String path) { this.imagePath = path; }
    public void save() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(imagePath))) {
            oos.writeObject(root); oos.writeObject(journal);
        }
    }

    private String[] parts(String p) { return Arrays.stream(p.split("/")).filter(s -> !s.isBlank()).toArray(String[]::new); }
    private Directory resolveParent(String p) throws Exception {
        String[] ps = parts(p); Directory cur = root;
        for (int i = 0; i < ps.length - 1; i++) {
            FSNode n = cur.get(ps[i]);
            if (n == null || !n.isDirectory()) throw new Exception("Caminho inválido");
            cur = (Directory) n;
        }
        return cur;
    }

    public void createDir(String path) throws Exception {
        JournalEntry e = journal.begin(JournalEntry.Operation.CREATE_DIR, path); save();
        try {
            String[] ps = parts(path); Directory cur = root;
            for (String p : ps) {
                FSNode n = cur.get(p);
                if (n == null) { Directory d = new Directory(p); cur.put(d); cur = d; }
                else if (n.isDirectory()) { cur = (Directory) n; }
                else throw new Exception("Arquivo já existe no caminho");
            }
            e.commit();
        } catch (Exception ex) { e.abort(); throw ex; } finally { save(); }
    }

    public void writeFile(String path, String content) throws Exception {
        JournalEntry e = journal.begin(JournalEntry.Operation.WRITE_FILE, path); save();
        try {
            Directory parent = resolveParent(path); String name = parts(path)[parts(path).length - 1];
            FSNode n = parent.get(name);
            if (n == null) parent.put(new File(name, content.getBytes()));
            else if (!n.isDirectory()) ((File) n).setContent(content.getBytes());
            else throw new Exception("É uma pasta");
            e.commit();
        } catch (Exception ex) { e.abort(); throw ex; } finally { save(); }
    }

    public void readFile(String path) throws Exception {
        Directory parent = resolveParent(path); String name = parts(path)[parts(path).length - 1];
        FSNode n = parent.get(name);
        if (n == null || n.isDirectory()) throw new Exception("Arquivo não encontrado");
        System.out.println(new String(((File) n).getContent()));
    }

    public void deleteFile(String path) throws Exception {
        JournalEntry e = journal.begin(JournalEntry.Operation.DELETE_FILE, path); save();
        try {
            Directory parent = resolveParent(path); String name = parts(path)[parts(path).length - 1];
            parent.remove(name); e.commit();
        } catch (Exception ex) { e.abort(); throw ex; } finally { save(); }
    }

    public void listDir(String path) throws Exception {
        String[] ps = parts(path); Directory cur = root;
        for (String p : ps) { cur = (Directory) cur.get(p); }
        if (cur == null) { System.out.println("Diretório vazio ou inválido."); return; }
        cur.listChildren().forEach(System.out::println);
    }

    public void printJournal() { journal.print(); }
    public void info() {
        System.out.println("Arquivo em disco real: " + imagePath);
        System.out.println("Sucesso (COMMITTED): " + journal.countByStatus(JournalEntry.Status.COMMITTED));
    }
}