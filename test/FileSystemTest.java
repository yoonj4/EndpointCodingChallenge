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

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[0]);
    }

    @Test
    public void testCreateSubdirectory() {
        String createBaseDirectoryCommand = "CREATE test";
        dir.executeCommand(createBaseDirectoryCommand);
        String createSubdirectoryCommand = createBaseDirectoryCommand + "/subdirectory";
        
        dir.executeCommand(createSubdirectoryCommand);

        String[] lines = outContent.toString().split(System.getProperty("line.separator"));
        assertEquals(createSubdirectoryCommand, outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testCreateNoName() {
        dir.executeCommand("CREATE");

        assertEquals("CREATE", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot create directory - directory name not given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testCreateWhiteSpace() {
        dir.executeCommand("CREATE ");

        assertEquals("CREATE ", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot create directory - directory name not given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testCreateExistingDirectory() {
        String command = "CREATE test";
        dir.executeCommand(command);

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[1]);
        assertEquals("Cannot create test - test already exists", outContent.toString().split(System.getProperty("line.separator"))[2]);
    }

    @Test
    public void testCreateSubdirectoryBaseDirectoryNotExist() {
        dir.executeCommand("CREATE test/subdirectory");

        assertEquals("CREATE test/subdirectory", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot create test/subdirectory - test does not exist", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testCreateMultipleDirectories() {
        dir.executeCommand("CREATE test1 test2");

        assertEquals("CREATE test1 test2", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot create multiple directories", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testListNoDirectories() {
        String command = "LIST";

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[0]);
    }

    @Test
    public void testListWithDirectories() {
        String command = "LIST";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[2]);
        assertEquals("test", outContent.toString().split(System.getProperty("line.separator"))[3]);
        assertEquals("  subdirectory", outContent.toString().split(System.getProperty("line.separator"))[4]);
    }

    @Test
    public void testListSpecificDirectory() {
        dir.executeCommand("LIST test");

        assertEquals("LIST test", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot list specific directories", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testMove() {
        String command = "MOVE subdirectory test";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[2]);
    }

    @Test
    public void testMoveSubdirectory() {
        String command = "MOVE test1/subdirectory test2";
        dir.executeCommand("CREATE test1");
        dir.executeCommand("CREATE test1/subdirectory");
        dir.executeCommand("CREATE test2");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[3]);
    }

    @Test
    public void testMoveNoArgs() {
        dir.executeCommand("MOVE");

        assertEquals("MOVE", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot move directory - no directories given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testMoveOneDirectory() {
        dir.executeCommand("MOVE test");

        assertEquals("MOVE test", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot move test - no destination given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testMoveSourceNotExist() {
        dir.executeCommand("CREATE test");

        dir.executeCommand("MOVE not_exist test");

        assertEquals("MOVE not_exist test", outContent.toString().split(System.getProperty("line.separator"))[1]);
        assertEquals("Cannot move not_exist to test - not_exist does not exist", outContent.toString().split(System.getProperty("line.separator"))[2]);
    }

    @Test
    public void testMoveDestinationNotExist() {
        dir.executeCommand("CREATE test");

        dir.executeCommand("MOVE test not_exist");

        assertEquals("MOVE test not_exist", outContent.toString().split(System.getProperty("line.separator"))[1]);
        assertEquals("Cannot move test to not_exist - not_exist does not exist", outContent.toString().split(System.getProperty("line.separator"))[2]);       
    }

    @Test
    public void testMoveDestinationAlreadyContainsSource() {
        dir.executeCommand("CREATE test1");
        dir.executeCommand("CREATE test1/subdirectory");
        dir.executeCommand("CREATE test2");
        dir.executeCommand("CREATE test2/subdirectory");

        dir.executeCommand("MOVE test1/subdirectory test2");

        assertEquals("MOVE test1/subdirectory test2", outContent.toString().split(System.getProperty("line.separator"))[4]);
        assertEquals("Cannot move test1/subdirectory to test2 - test2/subdirectory already exists", outContent.toString().split(System.getProperty("line.separator"))[5]);
    }

    @Test
    public void testMoveTooManyDirectories() {
        dir.executeCommand("MOVE test1 test2 test3");

        assertEquals("MOVE test1 test2 test3", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot move directory - too many directories", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testDeleteDirectory() {
        String command = "DELETE test";
        dir.executeCommand("CREATE test");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testDeleteSubdirectory() {
        String command = "DELETE test/subdirectory";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[2]);
    }

    @Test
    public void testDeleteBaseDirectory() {
        String command = "DELETE test";
        dir.executeCommand("CREATE test");
        dir.executeCommand("CREATE test/subdirectory");

        dir.executeCommand(command);

        assertEquals(command, outContent.toString().split(System.getProperty("line.separator"))[2]);
    }

    @Test
    public void testDeleteNoDirectory() {
        dir.executeCommand("DELETE");

        assertEquals("DELETE", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot delete directory - no directory given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testDeleteNonexistentDirectory() {
        dir.executeCommand("DELETE test");

        assertEquals("DELETE test", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot delete test - test does not exist", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testDeleteNonexistentBaseDirectory() {
        dir.executeCommand("DELETE test/subdirectory");

        assertEquals("DELETE test/subdirectory", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot delete test/subdirectory - test does not exist", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testDeleteMultipleDirectories() {
        dir.executeCommand("DELETE test1 test2");

        assertEquals("DELETE test1 test2", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot delete multiple directories", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testNoCommand() {
        dir.executeCommand("");

        assertEquals("", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("No command given", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }

    @Test
    public void testInvalidCommand() {
        dir.executeCommand("test");

        assertEquals("test", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("Cannot execute test - not a valid command", outContent.toString().split(System.getProperty("line.separator"))[1]);
    }
}
