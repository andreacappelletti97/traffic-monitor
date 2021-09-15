import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerManagerTest {
    private TimerManager tm;
    private ReportCreator rp;
    private CentralinaStatus status;

    @Before
    public void setUp() throws Exception {
        status = new CentralinaStatus(111);
        ReportCreator.createReportCreator(status);
        rp = ReportCreator.getInstance();
        tm = TimerManager.getInstance();
    }

    @Test
    public void testGetInstance() {
        TimerManager newTM = TimerManager.getInstance();
        assertEquals("getInstance method should return always the same object", tm, newTM);
    }

    @Test
    public void testGetTimerTime() {
        assertTrue("timerTime variable should be greater than zero", tm.getTimerTime() > 0);
    }

    @Test
    public void testUpdateTimerCounter() {
        tm.updateTimerCounter(10);
        assertTrue("updateTimerCounter method must end with creation of new Timer object", (tm.getTimer()) != null);
    }
}