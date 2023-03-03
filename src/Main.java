package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    // args should only contain one element which is the file name containing the commands to run.
    // If there are any additional elements in args then IllegalArgumentException is thrown.
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("This program must be run with exactly one argument: the name of the file containing the commands to run.");
        }

        String commandFileName = args[0];
        File commands = new File(commandFileName);

        Scanner scanner;
        try {
            scanner = new Scanner(commands);
            FileSystem fileSystem = new FileSystem();
            while(scanner.hasNextLine()) {
                String command = scanner.nextLine();
                fileSystem.executeCommand(command);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File does not exist.");
        }
    }
}