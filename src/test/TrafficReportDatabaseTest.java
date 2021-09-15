import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TrafficReportDatabaseTest {

    // NOTE: All test run on MapData3

    @Before
    public void testSetup() {
        // Retrieve map data and street names from external file
        MapManager.getInstance();

        // Initialize street list (it needs the MapManager to be fully ready) and starts the timer to empty app reports
        TrafficReportDatabase.getInstance();
    }

    @Test
    public void getInstance() {
        TrafficReportDatabase database1 = TrafficReportDatabase.getInstance();
        TrafficReportDatabase database2 = TrafficReportDatabase.getInstance();

        assertEquals("Only one instance of TrafficReportDatabase", database1, database2);
    }

    @Test
    public void getSavedTrafficData() {
        System.out.println(TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(0).getTrafficAmount() +
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(0).getStreetName() +
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(1).getTrafficAmount() +
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(1).getStreetName() +
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(2).getTrafficAmount() +
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(2).getStreetName());

        assertTrue("TrafficReportDatabase returns savedTrafficData correctly",
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().size() == 3 &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(0).getTrafficAmount() == 0 &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(1).getTrafficAmount() == 1 &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(2).getTrafficAmount() == 0 &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(0).getStreetName().equals("Via De Marchi") &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(1).getStreetName().equals("Via Giovanni Paolo II") &&
                        TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(2).getStreetName().equals("Via Orsenigo"));
    }

    @Test(expected = NullPointerException.class)
    public void saveTrafficDataCentralinaReportOnStreetCovered() throws InvalidObjectException {
        TrafficReportDatabase.getInstance().saveTrafficData(new CentralinaReport(0, 10));
        assertTrue("TrafficReportDatabase saves the traffic data from a CentralinaReport correctly",
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(0).getTrafficAmount() == 10);
    }

    @Test(expected = IllegalStateException.class)
    public void saveTrafficDataCentralinaReportOnStreetEmpty() throws InvalidObjectException {
        TrafficReportDatabase.getInstance().saveTrafficData(new CentralinaReport(1, 10));
    }

    @Test(expected = NullPointerException.class)
    public void saveTrafficDataAppReportOnStreetCovered() throws InvalidObjectException {
        TrafficReportDatabase.getInstance().saveTrafficData(new AppReport(new Position(4, 0)));
    }

    @Test(expected = NullPointerException.class)
    public void saveTrafficDataAppReportOnStreetEmpty() throws InvalidObjectException {
        TrafficReportDatabase.getInstance().saveTrafficData(new AppReport(new Position(0, 9)));
        assertTrue("TrafficReportDatabase saves the traffic data from an AppReport correctly",
                TrafficReportDatabase.getInstance().getSavedTrafficData().getStreetList().get(1).getTrafficAmount() == 1);
    }

    @Test
    public void retrieveTrafficDataFromListOfStreetIndexesNoResults() {
        assertTrue("TrafficReportDatabase returns the right street from indexes",
                TrafficReportDatabase.getInstance().retrieveTrafficDataFromListOfStreetIndexes(new ArrayList<>()).getStreetList().isEmpty());
    }

    @Test
    public void retrieveTrafficDataFromListOfStreetIndexes() {
        ArrayList<Integer> listOfStreetIndexes = new ArrayList<>();
        listOfStreetIndexes.add(0);

        assertTrue("TrafficReportDatabase returns the right street from indexes",
                TrafficReportDatabase.getInstance().retrieveTrafficDataFromListOfStreetIndexes(listOfStreetIndexes)
                        .getStreetList().size() == 1);
    }
}