import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportCreatorTest {
    ReportCreator creator;
    CentralinaStatus status;

    int maxTraffic;
    int minTraffic;
    int maxVariation;

    @Before
    public void setUp() throws Exception {
        status = new CentralinaStatus(111);
        ReportCreator.createReportCreator(status);
        creator = ReportCreator.getInstance();
        maxTraffic = creator.getMaxTraffic();
        minTraffic = creator.getMinTraffic();
        maxVariation = creator.getMaxVariation();
    }

    @Test
    public void testRetrieveDataTraffic() {
        int current = 1;
        for (int i=0; i < 10000; i++){
            current = ReportCreator.retrieveDataTraffic(current, maxTraffic, minTraffic, maxVariation);
            assertTrue("currentTraffic must be beetwen min and max", (current >= minTraffic) && (current <= maxTraffic));
        }
    }

    @Test
    public void testGetMinTraffic() {
        assertTrue("minTraffic constant should be greater than zero", creator.getMaxTraffic() > 0);
    }

    @Test
        public void testGetTrafficGeneratorDelay() {
        assertTrue("trafficGeneratorDelay must be greater than zero", creator.getTrafficGeneratorDelay() > 0);
    }

    @Test
    public void testGetMaxVariation() {
        assertTrue("maxVariation should guarantee at least " +
                "some level of traffic differentiation (min 3 level)", maxVariation < ((maxTraffic - minTraffic)/3));
    }
}