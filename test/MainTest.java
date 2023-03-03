package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.Main;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    // Tests

    @Test(expected = IllegalArgumentException.class)
    public void testNoArgs() {
        Main.main(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyArgs() {
        Main.main(new String[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileNotFound() {
        String[] args = new String[1];
        args[0] = "not a file";
        Main.main(args);
    }

    @Test
    public void testMainRunsMultipleCommands() {
        String[] args = new String[1];
        args[0] = "bin/test/data/commands.txt";
        Main.main(args);
        String expected = "CREATE fruits\n" +
        "CREATE vegetables\n" +
        "CREATE grains\n" +
        "CREATE fruits/apples\n" +
        "CREATE fruits/apples/fuji\n" +
        "LIST\n" +
        "fruits\n" +
        "  apples\n" +
        "    fuji\n" +
        "grains\n" +
        "vegetables\n" +
        "CREATE grains/squash\n" +
        "MOVE grains/squash vegetables\n" +
        "CREATE foods\n" +
        "MOVE grains foods\n" +
        "MOVE fruits foods\n" +
        "MOVE vegetables foods\n" +
        "LIST\n" +
        "foods\n" +
        "  fruits\n" +
        "    apples\n" +
        "      fuji\n" +
        "  grains\n" +
        "  vegetables\n" +
        "    squash\n" +
        "DELETE fruits/apples\n" +
        "Cannot delete fruits/apples - fruits does not exist\n" +
        "DELETE foods/fruits/apples\n" +
        "LIST\n" +
        "foods\n" +
        "  fruits\n" +
        "  grains\n" +
        "  vegetables\n" +
        "    squash\n";
        assertEquals(expected, outContent.toString());
    }
}
