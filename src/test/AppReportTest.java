import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppReportTest {

    private AppReport appReportTest;

    @Before
    public void testSetup() {
        Position appReportPosition = new Position(0, 0);
        appReportTest = new AppReport(appReportPosition);
    }

    @Test
    public void getPosition() {
        assertTrue("AppReport returns right position", appReportTest.getPosition().equals(new Position(0, 0)));
    }

    @Test
    public void getReportDateTime() {
        assertTrue("Set AppReport LocalDateTime in the constructor", appReportTest.getReportDateTime() != null);
    }
}