package src;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

// Maintains a collection of Directories.
// Accepts commands via executeCommand() which executes various READ/WRITE commands on the FileSystem.
//
// Directories are individually identified by their names within a specific base directory.
// Directory names do not include white space.
public class FileSystem {

    // Subdirectories of a directory. Contains the names of the subdirectories and a collection of subdirectories for those subdirectories.
    private static class Subdirectory {

        private SortedMap<String, Subdirectory> subdirectories;

        private Subdirectory() {
            subdirectories = new TreeMap<>();
        }
    }

    private SortedMap<String, Subdirectory> directories;

    public FileSystem() {
        directories = new TreeMap<>();
    }

    // command is assumed to be non-null.
    // All white space is stripped when parsing command.
    // A valid command is of the form "<COMMAND> <ARG1> <ARG2> ..." where the number of args depends on the command.
    // Valid commands are: CREATE, MOVE, LIST, DELETE. Commands must be all uppercase.
    // Directory names are of the form <directory>/<subdirectory>/<subdirectory>/...
    public void executeCommand(String command) {
        System.out.println(command);
        String[] commandTerms = command.split("\\s");

        switch(commandTerms[0]) {
            case "CREATE":
                if (commandTerms.length == 1) {
                    System.out.println("Cannot create directory - directory name not given");
                } else if (commandTerms.length > 2) {
                    System.out.println("Cannot create multiple directories");
                } else {
                    createDirectory(commandTerms[1]);
                }
                break;
            case "MOVE":
                if (commandTerms.length == 1) {
                    System.out.println("Cannot move directory - no directories given");
                } else if (commandTerms.length == 2) {
                    System.out.println("Cannot move " + commandTerms[1] + " - no destination given");
                } else if (commandTerms.length > 3) {
                    System.out.println("Cannot move directory - too many directories");
                } else {
                    moveDirectory(commandTerms[1], commandTerms[2]);
                }
                break;
            case "LIST":
                if (commandTerms.length != 1) {
                    System.out.println("Cannot list specific directories");
                } else {
                    listDirectories();
                }
                break;
            case "DELETE":
                if (commandTerms.length == 1) {
                    System.out.println("Cannot delete directory - no directory given");
                } else if (commandTerms.length > 2) {
                    System.out.println("Cannot delete multiple directories");
                } else {
                    deleteDirectory(commandTerms[1]);
                }
                break;
            case "":
                System.out.println("No command given");
                break;
            default:
                System.out.println("Cannot execute " + command + " - not a valid command");
        }
    }
    
    // Creates a single directory.
    private void createDirectory(String directoryName) {
        String[] directoryNameTerms = directoryName.split("/");
        SortedMap<String, Subdirectory> currentDirectory = directories;
        String subdirectoryName = directoryNameTerms[0];  // guaranteed to bed set in while loop. Just need to initialize value.
        int i = 1;
        while (i < directoryNameTerms.length) {
            Subdirectory subdirectory = currentDirectory.get(subdirectoryName);
            if (subdirectory == null) {
                break;
            }
            subdirectoryName = directoryNameTerms[i];
            currentDirectory = subdirectory.subdirectories;
            i++;
        }

        if (i == directoryNameTerms.length) {

            if (currentDirectory.containsKey(subdirectoryName)) {
                System.out.println("Cannot create " + directoryName + " - " + directoryName + " already exists");
            } else {
                currentDirectory.put(subdirectoryName, new Subdirectory());
            }
        } else {
            StringBuilder baseDirectoryBuilder = new StringBuilder(directoryNameTerms[0]);
            for (int j = 1; j < i; j++) {
                baseDirectoryBuilder.append(directoryNameTerms[j]);
            }
            System.out.println("Cannot create " + directoryName + " - " + baseDirectoryBuilder.toString() + " does not exist");
        }
    }

    // Move the source directory to the destination directory as a new subdirectory.
    private void moveDirectory(String source, String destination) {
        SortedMap<String, Subdirectory> sourceDirectory = directories;
        String[] sourceTerms = source.split("/");
        boolean foundDirectory = true;
        for (int i = 0; i < sourceTerms.length - 1; i++) {
            Subdirectory subdirectory = directories.get(sourceTerms[i]);
            if (subdirectory == null) {
                foundDirectory = false;
                break;
            }
            sourceDirectory = subdirectory.subdirectories;
        }

        if (!foundDirectory || !sourceDirectory.containsKey(sourceTerms[sourceTerms.length - 1])) {
            System.out.println("Cannot move " + source + " to " + destination + " - " + source + " does not exist");
            return;
        }

        SortedMap<String, Subdirectory> destinationDirectory = directories;
        String[] destinationTerms = destination.split("/");
        foundDirectory = true;
        for (int i = 0; i < destinationTerms.length - 1; i++) {
            Subdirectory subdirectory = destinationDirectory.get(destinationTerms[i]);
            if (subdirectory == null) {
                foundDirectory = false;
                break;
            }
            destinationDirectory = subdirectory.subdirectories;
        }

        if (!foundDirectory || !destinationDirectory.containsKey(destinationTerms[destinationTerms.length - 1])) {
            System.out.println("Cannot move " + source + " to " + destination + " - " + destination + " does not exist");
            return;
        }

        String finalSourceTerm = sourceTerms[sourceTerms.length - 1];
        String finalDestinationTerm = destinationTerms[destinationTerms.length - 1];
        Subdirectory sourceSubdirectory = sourceDirectory.remove(finalSourceTerm);
        destinationDirectory = destinationDirectory.get(finalDestinationTerm).subdirectories;
        if (destinationDirectory.containsKey(finalSourceTerm)) {
            System.out.println("Cannot move " + source + " to " + destination + " - " + destination + "/" + finalSourceTerm + " already exists");
        } else {
            destinationDirectory.put(finalSourceTerm, sourceSubdirectory);
        }
    } 

    // Print the current directory structure.
    // Every directory is on its own line.
    // Subdirectories are indented by two white space characters.
    private void listDirectories() {
        listDirectoriesHelper(directories, 0);
    }

    private void listDirectoriesHelper(SortedMap<String, Subdirectory> currentLevel, int numIndentation) {
        for (Entry<String, Subdirectory> directory : currentLevel.entrySet()) {
            StringBuilder listLineBuilder = new StringBuilder();
            for (int i = 0; i < numIndentation; i++) {
                listLineBuilder.append("  ");
            }
            listLineBuilder.append(directory.getKey());

            System.out.println(listLineBuilder.toString());
            listDirectoriesHelper(directory.getValue().subdirectories, numIndentation + 1);
        }
    }

    // Delete the directory if it exists.
    private void deleteDirectory(String directoryName) {
        SortedMap<String, Subdirectory> directory = directories;
        String[] directoryTerms = directoryName.split("/");
        boolean foundDirectory = true;
        StringBuilder directoryTermsIteratedOn = new StringBuilder();
        int i;
        for (i = 0; i < directoryTerms.length - 1; i++) {
            String directoryTerm = directoryTerms[i];
            Subdirectory subdirectory = directory.get(directoryTerm);
            if (subdirectory == null) {
                foundDirectory = false;
                break;
            }
            directory = subdirectory.subdirectories;
            directoryTermsIteratedOn.append(directoryTerm + "/");
        }
        directoryTermsIteratedOn.append(directoryTerms[i]);

        if (!foundDirectory || !directory.containsKey(directoryTerms[directoryTerms.length - 1])) {
            System.out.println("Cannot delete " + directoryName + " - " + directoryTermsIteratedOn.toString() + " does not exist");
            return;
        }

        directory.remove(directoryTerms[directoryTerms.length - 1]);
    }
}
