import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StreetListTest {

    private StreetList streetListEmptyTest;
    private StreetList streetListTest;

    @Before
    public void testSetup() {
        ArrayList<StreetTrafficHandler> trafficHandlerArrayList = new ArrayList<>();
        trafficHandlerArrayList.add(new StreetCovered(0, "nullCovered", 0));
        trafficHandlerArrayList.add(new StreetEmpty(0, "nullEmpty"));

        streetListEmptyTest = new StreetList();
        streetListTest = new StreetList(trafficHandlerArrayList);
    }

    @Test
    public void getStreetList() {
        assertTrue("StreetList empty returns the right streetList ArrayList",
                streetListEmptyTest.getStreetList() != null &&
                streetListEmptyTest.getStreetList().isEmpty());
        assertTrue("StreetList returns the right streetList ArrayList",
                streetListTest.getStreetList() != null &&
                        streetListTest.getStreetList().size() == 2);
    }

    @Test
    public void addStreet() {
        streetListTest.addStreet(new StreetCovered(1, "nullCovered1", 0));
        assertTrue("StreetList adds a new street correctly",
                streetListTest.getStreetList().size() == 3 &&
                streetListTest.getStreetList().get(streetListTest.getStreetList().size() - 1).getStreetName().equals("nullCovered1"));
    }
}