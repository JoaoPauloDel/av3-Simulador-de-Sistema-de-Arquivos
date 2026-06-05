package com.fssimulator.shell;

import com.fssimulator.core.FileSystemSimulator;
import java.util.Scanner;

public class Shell {
    private final FileSystemSimulator fs;
    private final Scanner scanner = new Scanner(System.in);

    public Shell(FileSystemSimulator fs) { this.fs = fs; }

    public void run() {
        System.out.println("=== Simulador Inicializado. Digite 'help' ===");
        while (true) {
            System.out.print("\nfs> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split(" ", 3);
            String cmd = tokens[0].toLowerCase();

            try {
                switch (cmd) {
                    case "mkdir" -> fs.createDir(tokens[1]);
                    case "write" -> fs.writeFile(tokens[1], tokens[2]);
                    case "cat" -> fs.readFile(tokens[1]);
                    case "rm" -> fs.deleteFile(tokens[1]);
                    case "ls" -> fs.listDir(tokens.length > 1 ? tokens[1] : "/");
                    case "journal" -> fs.printJournal();
                    case "info" -> fs.info();
                    case "exit" -> { fs.save(); return; }
                    case "help" -> System.out.println("Comandos: mkdir, write, cat, rm, ls, journal, info, exit");
                    default -> System.out.println("Comando inválido.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}