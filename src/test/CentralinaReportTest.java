import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CentralinaReportTest {

    private CentralinaReport centralinaReportTest;

    @Before
    public void testSetup() {
        centralinaReportTest = new CentralinaReport(0, 0);
    }

    @Test
    public void getTrafficAmount() {
        assertTrue("CentralinaReport returns right trafficAmount", centralinaReportTest.getTrafficAmount() == 0);
    }
}