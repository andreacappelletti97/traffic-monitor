import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapManagerTest {

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
        MapManager mapManager1 = MapManager.getInstance();
        MapManager mapManager2 = MapManager.getInstance();

        assertEquals("Only one instance of MapManager", mapManager1, mapManager2);
    }

    @Test
    public void retrieveStreetList() {
        StreetList streetList = MapManager.getInstance().retrieveStreetList();

        assertTrue("MapManager generates the StreetList correctly",
                streetList.getStreetList().size() == 3 &&
                streetList.getStreetList().get(0).getTrafficAmount() == 0 &&
                streetList.getStreetList().get(1).getTrafficAmount() == 0 &&
                streetList.getStreetList().get(2).getTrafficAmount() == 0 &&
                streetList.getStreetList().get(0).getStreetName().equals("Via De Marchi") &&
                streetList.getStreetList().get(1).getStreetName().equals("Via Giovanni Paolo II") &&
                streetList.getStreetList().get(2).getStreetName().equals("Via Orsenigo"));
    }

    @Test
    public void retrieveListOfStreetInRadiusNoResult() {
        assertTrue("MapManager street in radius no results correctly",
                MapManager.getInstance().retrieveListOfStreetInRadius(1, new Position(0, 0)).isEmpty());
    }

    @Test
    public void retrieveListOfStreetInRadius() {
        assertTrue("MapManager street in radius correctly",
                MapManager.getInstance().retrieveListOfStreetInRadius(4, new Position(25, 6)).size() == 2);
    }

    @Test
    public void retrieveStreetIdFromPosition() {
        assertEquals(MapManager.getInstance().retrieveStreetIdFromPosition(new Position(4, 0)), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void retrieveStreetIdFromPositionErrorNoStreet() {
        MapManager.getInstance().retrieveStreetIdFromPosition(new Position(0, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void retrieveStreetIdFromPositionErrorOutOfBounds() {
        MapManager.getInstance().retrieveStreetIdFromPosition(new Position(100, 0));
    }

    @Test
    public void isPositionValidInTheMap() {
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(new Position(0, 0)));
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(new Position(0, 7)));
        assertTrue(MapManager.getInstance().isPositionValidInTheMap(new Position(4, 0)));
        assertTrue(MapManager.getInstance().isPositionValidInTheMap(new Position(0, 9)));
        assertTrue(MapManager.getInstance().isPositionValidInTheMap(new Position(13, 13)));
    }

    @Test(expected = NullPointerException.class)
    public void isPositionValidInTheMapNullPosition() {
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(null));
    }

    @Test
    public void isPositionValidInTheMapOutOfBounds() {
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(new Position(-1, 0)));
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(new Position(0, -1)));
        assertFalse(MapManager.getInstance().isPositionValidInTheMap(new Position(100, 100)));
    }

    @Test
    public void getMap() {
        assertNotNull(MapManager.getInstance().getMap());
    }

    @Test
    public void getStreetIdFromPosition() {
        assertEquals(MapManager.getInstance().getStreetIdFromPosition(new Position(0, 0)), -1);
        assertEquals(MapManager.getInstance().getStreetIdFromPosition(new Position(4, 0)), 0);
        assertEquals(MapManager.getInstance().getStreetIdFromPosition(new Position(0, 9)), 1);
        assertEquals(MapManager.getInstance().getStreetIdFromPosition(new Position(13, 13)), 2);
    }

    @Test
    public void getMapWidth() {
        assertEquals(MapManager.getInstance().getMapWidth(), 30);
    }

    @Test
    public void getMapWidthWithRow() {
        assertEquals(MapManager.getInstance().getMapWidth(4), 30);
    }

    @Test
    public void getMapHeight() {
        assertEquals(MapManager.getInstance().getMapHeight(), 20);
    }
}