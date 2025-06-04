import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class AddDemeritPointsTest {

    @BeforeEach
    public void clearFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("demerits.txt"));
        writer.write("");
        writer.close();
    }

    @Test
    public void testValidUnder21Suspended() {
        Person p = new Person("23!!abcdXY", "15-11-2006");
        p.addDemeritPoints("01-01-2024", 3);
        p.addDemeritPoints("01-06-2025", 4);
        assertTrue(p.isSuspended());
    }

    @Test
    public void testValidOver21NotSuspended() {
        Person p = new Person("24@@idAB", "15-11-1990");
        p.addDemeritPoints("01-01-2024", 6);
        p.addDemeritPoints("01-06-2025", 5);
        assertFalse(p.isSuspended());
    }

    @Test
    public void testInvalidDateFormat() {
        Person p = new Person("29##xxCD", "15-11-1990");
        String result = p.addDemeritPoints("2024/05/01", 3);
        assertEquals("Failed", result);
    }

    @Test
    public void testInvalidPointValue() {
        Person p = new Person("88&&badDD", "15-11-1990");
        String result = p.addDemeritPoints("01-05-2024", 10);
        assertEquals("Failed", result);
    }

    @Test
    public void testSuspensionOver21() {
        Person p = new Person("24@@idEE", "15-11-1990");
        p.addDemeritPoints("01-01-2023", 6);
        
        p.addDemeritPoints("01-06-2024", 7);
        assertFalse(p.isSuspended());
    }
}
