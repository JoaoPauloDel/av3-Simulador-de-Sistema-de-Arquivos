package com.fssimulator;

import com.fssimulator.core.FileSystemSimulator;
import com.fssimulator.shell.Shell;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean demo = false;
        String imagePath = "filesystem.jfs";

        for (String arg : args) {
            if ("--demo".equalsIgnoreCase(arg)) demo = true;
            else imagePath = arg;
        }

        FileSystemSimulator fs = FileSystemSimulator.open(imagePath);

        if (demo) {
            System.out.println("=== Rodando modo demonstração ===");
            fs.createDir("/documentos");
            fs.writeFile("/documentos/readme.txt", "Simulador SO.");
            fs.listDir("/");
            fs.printJournal();
            fs.info();
        }

        new Shell(fs).run();
    }
}