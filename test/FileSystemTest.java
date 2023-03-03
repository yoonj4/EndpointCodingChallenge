package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.FileSystem;

public class FileSystemTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private FileSystem dir;

    @Before
    public void setupTests() {
        dir = new FileSystem();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void teardownTests() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testCreateDirectory() {
        String command = "CREATE test";

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testCreateSubdirectory() {
        String createBaseDirectoryCommand = "CREATE test";
        dir.executeCommand(createBaseDirectoryCommand);
        String createSubdirectoryCommand = createBaseDirectoryCommand + "/subdirectory";
        
        dir.executeCommand(createSubdirectoryCommand);

        assertEquals(createSubdirectoryCommand, outContent.toString());
    }

    @Test
    public void testCreateNoName() {
        dir.executeCommand("CREATE");

        assertEquals("Cannot create directory - directory name not given", outContent.toString());
    }

    @Test
    public void testCreateWhiteSpace() {
        dir.executeCommand("CREATE ");

        assertEquals("Cannot create directory - directory name not given", outContent.toString());
    }

    @Test
    public void testCreateExistingDirectory() {
        String command = "CREATE test";
        dir.executeCommand(command);

        dir.executeCommand(command);

        assertEquals("Cannot create test - test already exists", outContent.toString());
    }

    @Test
    public void testCreateSubdirectoryBaseDirectoryNotExist() {
        dir.executeCommand("CREATE test/subdirectory");

        assertEquals("Cannot create test/subdirectory - test does not exist", outContent.toString());
    }

    @Test
    public void testListNoDirectories() {
        String command = "LIST";

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testListWithDirectories() {
        String command = "LIST";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        String expected = command + "\ntest\n  subdirectory";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testMove() {
        String command = "MOVE subdirectory test";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testMoveSubdirectory() {
        String command = "MOVE test1/subdirectory test2";
        dir.executeCommand("CREATE test1");
        dir.executeCommand("CREATE test1/subdirectory");
        dir.executeCommand("CREATE test2");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testMoveNoArgs() {
        dir.executeCommand("MOVE");

        assertEquals("Cannot move directory - no directories given", outContent.toString());
    }

    @Test
    public void testMoveOneDirectory() {
        dir.executeCommand("MOVE test");

        assertEquals("Cannot move test - no destination given", outContent.toString());
    }

    @Test
    public void testMoveSourceNotExist() {
        dir.executeCommand("CREATE test");

        dir.executeCommand("MOVE not_exist test");

        assertEquals("Cannot move not_exist to test - not_exist does not exist", outContent.toString());
    }

    @Test
    public void testMoveDestinationNotExist() {
        dir.executeCommand("CREATE test");

        dir.executeCommand("MOVE test not_exist");

        assertEquals("Cannot move test to not_exist - not_exist does not exist", outContent.toString());       
    }

    @Test
    public void testDeleteDirectory() {
        String command = "DELETE test";
        dir.executeCommand("CREATE test");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testDeleteSubdirectory() {
        String command = "DELETE test/subdirectory";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testDeleteBaseDirectory() {
        String command = "DELETE test";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString());
    }

    @Test
    public void testDeleteNoDirectory() {
        dir.executeCommand("DELETE");

        assertEquals("Cannot delete directory - no directory given", outContent.toString());
    }

    @Test
    public void testDeleteNonexistentDirectory() {
        dir.executeCommand("DELETE test");

        assertEquals("Cannot delete test - test does not exist", outContent.toString());
    }

    @Test
    public void testDeleteNonexistentBaseDirectory() {
        dir.executeCommand("DELETE test/subdirectory");

        assertEquals("Cannot delete test/subdirectory - test does not exist", outContent.toString());
    }
}
