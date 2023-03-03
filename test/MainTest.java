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

        assertEquals("CREATE fruits", outContent.toString().split(System.getProperty("line.separator"))[0]);
        assertEquals("CREATE vegetables", outContent.toString().split(System.getProperty("line.separator"))[1]);
        assertEquals("CREATE grains", outContent.toString().split(System.getProperty("line.separator"))[2]);
        assertEquals("CREATE fruits/apples", outContent.toString().split(System.getProperty("line.separator"))[3]);
        assertEquals("CREATE fruits/apples/fuji", outContent.toString().split(System.getProperty("line.separator"))[4]);
        assertEquals("LIST", outContent.toString().split(System.getProperty("line.separator"))[5]);
        assertEquals("fruits", outContent.toString().split(System.getProperty("line.separator"))[6]);
        assertEquals("  apples", outContent.toString().split(System.getProperty("line.separator"))[7]);
        assertEquals("    fuji", outContent.toString().split(System.getProperty("line.separator"))[8]);
        assertEquals("grains", outContent.toString().split(System.getProperty("line.separator"))[9]);
        assertEquals("vegetables", outContent.toString().split(System.getProperty("line.separator"))[10]);
        assertEquals("CREATE grains/squash", outContent.toString().split(System.getProperty("line.separator"))[11]);
        assertEquals("MOVE grains/squash vegetables", outContent.toString().split(System.getProperty("line.separator"))[12]);
        assertEquals("CREATE foods", outContent.toString().split(System.getProperty("line.separator"))[13]);
        assertEquals("MOVE grains foods", outContent.toString().split(System.getProperty("line.separator"))[14]);
        assertEquals("MOVE fruits foods", outContent.toString().split(System.getProperty("line.separator"))[15]);
        assertEquals("MOVE vegetables foods", outContent.toString().split(System.getProperty("line.separator"))[16]);
        assertEquals("LIST", outContent.toString().split(System.getProperty("line.separator"))[17]);
        assertEquals("foods", outContent.toString().split(System.getProperty("line.separator"))[18]);
        assertEquals("  fruits", outContent.toString().split(System.getProperty("line.separator"))[19]);
        assertEquals("    apples", outContent.toString().split(System.getProperty("line.separator"))[20]);
        assertEquals("      fuji", outContent.toString().split(System.getProperty("line.separator"))[21]);
        assertEquals("  grains", outContent.toString().split(System.getProperty("line.separator"))[22]);
        assertEquals("  vegetables", outContent.toString().split(System.getProperty("line.separator"))[23]);
        assertEquals("    squash", outContent.toString().split(System.getProperty("line.separator"))[24]);
        assertEquals("DELETE fruits/apples", outContent.toString().split(System.getProperty("line.separator"))[25]);
        assertEquals("Cannot delete fruits/apples - fruits does not exist", outContent.toString().split(System.getProperty("line.separator"))[26]);
        assertEquals("DELETE foods/fruits/apples", outContent.toString().split(System.getProperty("line.separator"))[27]);
        assertEquals("LIST", outContent.toString().split(System.getProperty("line.separator"))[28]);
        assertEquals("foods", outContent.toString().split(System.getProperty("line.separator"))[29]);
        assertEquals("  fruits", outContent.toString().split(System.getProperty("line.separator"))[30]);
        assertEquals("  grains", outContent.toString().split(System.getProperty("line.separator"))[31]);
        assertEquals("  vegetables", outContent.toString().split(System.getProperty("line.separator"))[32]);
        assertEquals("    squash", outContent.toString().split(System.getProperty("line.separator"))[33]);
    }
}
