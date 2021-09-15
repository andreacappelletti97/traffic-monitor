import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MapDataSetupTest {

    @Test(expected = NullPointerException.class)
    public void getPositionWithStreet() {
        MapDataSetup.getPositionWithStreet();
    }

    @Test
    public void setPositionWithStreet() {
        ArrayList<Position> positionWithStreet = new ArrayList<>();
        positionWithStreet.add(new Position(0, 0));
        positionWithStreet.add(new Position(1, 0));

        MapDataSetup.setPositionWithStreet(positionWithStreet);
        assertTrue("MapDataSetup sets the positionWithStreet ArrayList correctly", MapDataSetup.getPositionWithStreet() == positionWithStreet);
    }
}