import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StreetAppTest {

    private StreetApp streetAppTestFromCentralina;
    private StreetApp streetAppTestNotFromCentralina;

    @Before
    public void testSetup() {
        streetAppTestFromCentralina = new StreetApp(0, "null", 0, true);
        streetAppTestNotFromCentralina = new StreetApp(0, "null", 0, false);
    }

    @Test
    public void getStreetId() {
        assertTrue(streetAppTestFromCentralina.getStreetId() == 0);
    }

    @Test
    public void getStreetName() {
        assertTrue(streetAppTestFromCentralina.getStreetName().equals("null"));
    }

    @Test
    public void getTrafficAmount() {
        assertTrue(streetAppTestFromCentralina.getTrafficAmount() == 0);
    }

    @Test
    public void isTrafficFromCentralina() {
        assertTrue(streetAppTestFromCentralina.isTrafficFromCentralina());
        assertFalse(streetAppTestNotFromCentralina.isTrafficFromCentralina());
    }
}