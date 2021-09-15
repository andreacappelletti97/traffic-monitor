import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    private Position positionTest;

    @Before
    public void testSetup() {
        positionTest = new Position(0, 0);
    }

    @Test
    public void getxPosition() {
        assertTrue("Position returns the right X value", positionTest.getxPosition() == 0);
    }

    @Test
    public void getyPosition() {
        assertTrue("Position returns the right Y value", positionTest.getyPosition() == 0);
    }

    @Test
    public void equals() {
        assertTrue("Two Position are equal if they have the same X and Y value", positionTest.equals(new Position(0, 0)));
    }
}