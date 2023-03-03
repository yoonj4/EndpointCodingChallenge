package src;

import java.util.SortedSet;
import java.util.TreeSet;

public class FileSystem {

    private static class Directory {

        private String name;
        private SortedSet<Directory> subDirectories;

        private Directory(String name) {
            this.name = name;
            subDirectories = new TreeSet<>();
        }
    }

    private SortedSet<Directory> directories;

    public FileSystem() {
        directories = new TreeSet<>();
    }

    public void executeCommand(String command) {

    }
}
