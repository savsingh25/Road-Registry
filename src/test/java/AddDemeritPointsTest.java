import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

/**
 * Tests for AddDemeritPoints method in the Person class.
 */
public class AddDemeritPointsTest {

    /**
     * Clears the demerits.txt file before each test.
     * This makes sure the file is empty so tests aren't mixed up with each other.
     */
    @BeforeEach
    public void clearFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("demerits.txt"));
        writer.write("");
        writer.close();
    }

    /**
     * Checks if someone under 21 gets suspended after getting more than 6 points.
     */
    @Test
    public void testValidUnder21Suspended() {
        Person p = new Person("23!!abcdXY", "15-11-2006");
        p.addDemeritPoints("01-01-2024", 3);
        p.addDemeritPoints("01-06-2025", 4);
        assertTrue(p.isSuspended());
    }

    /**
     * Checks if someone over 21 is not suspended if they get 11 points which is allowed
     */
    @Test
    public void testValidOver21NotSuspended() {
        Person p = new Person("24@@idAB", "15-11-1990");
        p.addDemeritPoints("01-01-2024", 6);
        p.addDemeritPoints("01-06-2025", 5);
        assertFalse(p.isSuspended());
    }

    /**
     * Checks if the method fails when the date format is wrong.
     */
    @Test
    public void testInvalidDateFormat() {
        Person p = new Person("29##xxCD", "15-11-1990");
        String result = p.addDemeritPoints("2024/05/01", 3);
        assertEquals("Failed", result);
    }

    /**
     * Checks if the method fails when too many points are added in one go.
     */
    @Test
    public void testInvalidPointValue() {
        Person p = new Person("88&&badDD", "15-11-1990");
        String result = p.addDemeritPoints("01-05-2024", 10); //wrong format
        assertEquals("Failed", result);
    }

    /**
     * Checks if someone over 21 is not suspended if they got too many points but some points were added more than 2 years ago.
     */
    @Test
    public void testSuspensionOver21() {
        Person p = new Person("24@@idEE", "15-11-1990");
        p.addDemeritPoints("01-01-2023", 6);
        
        p.addDemeritPoints("01-06-2024", 7);
        assertFalse(p.isSuspended());
    }
}

