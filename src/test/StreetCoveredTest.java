import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;

import static org.junit.Assert.*;

public class StreetCoveredTest {

    private StreetCovered streetCoveredTest;

    @Before
    public void testSetup() {
        streetCoveredTest = new StreetCovered(0, "null", 0);
    }

    @Test
    public void getStreetId() {
        assertTrue("StreetCovered returns the right streetId", streetCoveredTest.getStreetId() == 0);
    }

    @Test
    public void getStreetName() {
        assertTrue("StreetCovered returns the right streetName", streetCoveredTest.getStreetName().equals("null"));
    }

    @Test
    public void getTrafficAmount() {
        assertTrue("StreetCovered returns the right trafficAmount", streetCoveredTest.getTrafficAmount() == 0);
    }

    @Test
    public void setTrafficAmountCentralinaReport() throws InvalidObjectException {
        streetCoveredTest.setTrafficAmount(new CentralinaReport(0, 1));
        assertTrue("StreetCovered set the right trafficAmount when it gets a CentralinaReport as parameter", streetCoveredTest.getTrafficAmount() == 1);
    }

    @Test
    public void setTrafficAmountAppReport() throws InvalidObjectException {
        streetCoveredTest.setTrafficAmount(new AppReport(new Position(0, 0)));
        assertTrue("StreetCovered set the right trafficAmount when it gets an AppReport as parameter", streetCoveredTest.getTrafficAmount() == 1);
    }

    @Test
    public void convertToStreetForMobileApp() {
        assertTrue("StreetCovered creates the right Street for MobileApp",
                streetCoveredTest.convertToStreetForMobileApp().getStreetName().equals("null") &&
                streetCoveredTest.convertToStreetForMobileApp().getTrafficAmount() == 0 &&
                streetCoveredTest.convertToStreetForMobileApp().getStreetId() == 0 &&
                streetCoveredTest.convertToStreetForMobileApp().isTrafficFromCentralina());
    }

    @Test
    public void compareTo() {
        assertTrue("StreetCovered compares two same values right", streetCoveredTest.compareTo(streetCoveredTest) == 0);
        assertTrue("StreetCovered compares two different values right", streetCoveredTest.compareTo(new StreetCovered(0, "null", 1)) < 0);
        assertTrue("StreetCovered compares two different values right", streetCoveredTest.compareTo(new StreetCovered(0, "null", -1)) > 0);
    }
}