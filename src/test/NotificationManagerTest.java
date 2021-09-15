import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

public class NotificationManagerTest {

    NotificationManager notificationManager;
    HashMap<Street, LocalDateTime> streetNotificationList;
    final long SHOW_AGAIN_TIME = 1000 * 60; //ONE MINUTE

    @Before
    public void setUp() throws Exception {
        notificationManager = NotificationManager.getInstance();
        streetNotificationList = new HashMap<>();


    }

    @Test
    public void callShowNotification() {
        StreetApp streetApp = new StreetApp(1, "Via Anzani", 10, true);
        streetNotificationList.putIfAbsent(streetApp, LocalDateTime.now());
        assertTrue("Street List Notification does not contains StreetApp", streetNotificationList.containsKey(streetApp));
        //isToNotify Method
        assertFalse("It's not to notify", (abs(Duration.between(streetNotificationList.get(streetApp), LocalDateTime.now()).toMillis()) > SHOW_AGAIN_TIME));
    }

}