import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddPersonTest {
    // Create a sample Person object for all tests to use
    Person person = new Person("56s_d%&fAB", "15-11-2001");

    /**
     * Test a valid person ID, address, and birthdate.
     * Should return true since all values are correct.
     */
    @Test
    public void testValidPerson() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertTrue(result);
    }

    /**
     * Test when the person ID is too short (less than 10 characters).
     * Should return false.
     */
    @Test
    public void testInvalidIDTooShort() {
        boolean result = person.addPerson("56s_dAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }

    /**
     * Test when the address has the wrong state.
     * Should return false because the system only accepts 'Victoria'.
     */
    @Test
    public void testInvalidAddressWrongState() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|NSW|Australia", "15-11-1990");
        assertFalse(result);
    }

    /**
     * Test when the birthdate is in the wrong format should be dd-MM-yyyy.
     */
    @Test
    public void testInvalidBirthdateFormat() {
        boolean result = person.addPerson("56s_d%&fAB", "32|Highland Street|Melbourne|Victoria|Australia", "1990-11-15");
        assertFalse(result);
    }

    /**
     * Test when the ID doesn't contain at least 2 special characters between positions 2 and 7.
     */
    @Test
    public void testIDMissingSpecialChars() {
        boolean result = person.addPerson("56abcdffAB", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }
}
