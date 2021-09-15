import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;

import static org.junit.Assert.*;

public class StreetEmptyTest {

    private StreetEmpty streetEmptyTest;

    @Before
    public void testSetup() {
        streetEmptyTest = new StreetEmpty(0, "null");
    }

    @Test
    public void getReports() {
        assertTrue("StreetEmpty returns the stored reports right", streetEmptyTest.getReports() != null && streetEmptyTest.getReports().isEmpty());
    }

    @Test
    public void getStreetId() {
        assertTrue("StreetEmpty returns the right streetId", streetEmptyTest.getStreetId() == 0);
    }

    @Test
    public void getStreetName() {
        assertTrue("StreetEmpty returns the right streetName", streetEmptyTest.getStreetName().equals("null"));
    }

    @Test
    public void getTrafficAmount() {
        assertTrue("StreetCovered returns the right trafficAmount", streetEmptyTest.getTrafficAmount() == 0);
    }

    @Test
    public void setTrafficAmountAppReport() throws InvalidObjectException {
        streetEmptyTest.setTrafficAmount(new AppReport(new Position(0, 0)));
        assertTrue("StreetEmpty sets the right trafficAmount when it gets an AppReport as a parameter", streetEmptyTest.getTrafficAmount() == 1);
    }

    @Test(expected = InvalidObjectException.class)
    public void setTrafficAmountCentralinaReport() throws InvalidObjectException {
        streetEmptyTest.setTrafficAmount(new CentralinaReport(0, 0));
    }

    @Test
    public void convertToStreetForMobileApp() {
        assertTrue("StreetEmpty creates the right Street for MobileApp",
                streetEmptyTest.convertToStreetForMobileApp().getStreetName().equals("null") &&
                        streetEmptyTest.convertToStreetForMobileApp().getTrafficAmount() == 0 &&
                        streetEmptyTest.convertToStreetForMobileApp().getStreetId() == 0 &&
                        !streetEmptyTest.convertToStreetForMobileApp().isTrafficFromCentralina());
    }

    @Test
    public void addAppReport() {
        streetEmptyTest.addAppReport(new AppReport(new Position(0, 0)));
        assertTrue("StreetEmpty adds a new AppReport to the list correctly",
                streetEmptyTest.getReports().size() == 1 &&
                streetEmptyTest.getReports().get(streetEmptyTest.getReports().size() - 1).getPosition().equals(new Position(0,0)));
    }

    @Test
    public void compareTo() {
        // Street empty with 2 reports
        StreetEmpty streetEmptyMore = new StreetEmpty(1, "null1");
        streetEmptyMore.addAppReport(new AppReport(new Position(0, 0)));
        streetEmptyMore.addAppReport(new AppReport(new Position(1, 0)));

        // StreetEmpty with 0 reports
        StreetEmpty streetEmptyLess = new StreetEmpty(2, "null2");

        // StreetEmpty with 1 report
        streetEmptyTest.addAppReport(new AppReport(new Position(0, 0)));

        assertTrue("StreetEmpty compares two same values right", streetEmptyTest.compareTo(streetEmptyTest) == 0);
        assertTrue("StreetEmpty compares two different values right", streetEmptyTest.compareTo(streetEmptyMore) < 0);
        assertTrue("StreetEmpty compares two different values right", streetEmptyTest.compareTo(streetEmptyLess) > 0);
    }
}