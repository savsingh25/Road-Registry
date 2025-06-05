
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UpdatePersonalDetailsTest {
    // Sample person object used for calling methods
    Person person = new Person("dummyID", "01-01-1990");

    /**
     * Test case: Update should fail when the old ID is not found in the file.
     * This makes sure only existing users can be updated.
     */
@Test
public void testUpdateFailsWhenOldIdNotFound() {
    boolean result = person.updatePersonalDetails(
        "ZZ99notFound",
        "ZZ99notFound",
        "123|Fake St|Melb|Victoria|Australia",
        "01-01-1990"
    );

    assertFalse(result);
}
    /**
     * This should fail because minors are not allowed to change their address.
     */
    @Test
    public void testUpdateAddressFailsForMinor() {
        person.addPerson("29!!kidBB", "200|Young Rd|Geelong|Victoria|Australia", "01-01-2010");

        boolean result = person.updatePersonalDetails(
            "29!!kidBB",
            "29!!kidBB",
            "201|Other Rd|Geelong|Victoria|Australia",
            "01-01-2010"
        );

        assertFalse(result);
    }

    /**
     * Add a valid person to the file.
     * This test checks if the person is added successfully.
     */
@Test
public void testAddValidPersonOnly() {
    String personId = "23!!abcdXY";
    String address = "101|Main St|Melbourne|Victoria|Australia";
    String birthdate = "12-12-1990";

    boolean result = person.addPerson(personId, address, birthdate);

    assertTrue(result, "合法人员应成功添加");
}

    /**
     * Changing both the address and birthdate at the same time.
     * This is not allowed and should fail.
     */
    @Test
    public void testChangeBirthdateAndAddressShouldFail() {
        person.addPerson("88&&badDD", "400|Elm St|Bendigo|Victoria|Australia", "01-01-1992");

        boolean result = person.updatePersonalDetails(
            "88&&badDD",
            "88&&badDD",
            "401|Oak St|Bendigo|Victoria|Australia",
            "02-02-1992"
        );

        assertFalse(result);
    }

    /**
     * Trying to change the ID when it starts with an even digit.
     * System rules say you can't change the ID if it starts with an even number.
     * So this test should fail
     */
    @Test
    public void testChangeIDWhenStartsWithEvenDigitShouldFail() {
        person.addPerson("24@@idEE", "500|Main St|Shepparton|Victoria|Australia", "01-01-1990");

        boolean result = person.updatePersonalDetails(
            "24@@idEE",
            "25@@idEE", 
            "500|Main St|Shepparton|Victoria|Australia",
            "01-01-1990"
        );

        assertFalse(result);
    }
}
