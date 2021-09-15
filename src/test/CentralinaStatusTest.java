import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CentralinaStatusTest {

    private CentralinaStatus centralinaStatusTest;

    @Before
    public void testSetup() {
        centralinaStatusTest = new CentralinaStatus(0);
    }

    @Test
    public void getStreetId() {
        assertTrue("CentralinaStatus returns the right streetId", centralinaStatusTest.getStreetId() == 0);
    }
}