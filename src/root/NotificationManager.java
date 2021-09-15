import javafx.util.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class NotificationManager {

    private static NotificationManager notificationManager;
    private HashMap<Integer, Pair<Street, LocalDateTime>> streetNotificationList;
    private static final long SHOW_AGAIN_TIME = 1000 * 12;

    private NotificationManager(){
        streetNotificationList = new HashMap<>();
    }
    public static NotificationManager getInstance(){
        if (notificationManager == null) {
            return notificationManager = new NotificationManager();
        }

        return notificationManager;
    }

    public void callShowNotification(StreetList notificationList){
        System.out.print("Show notification called: ");
        for (StreetTrafficHandler streetSent: notificationList.getStreetList()){
            if (this.streetNotificationList.containsKey(streetSent.getStreetId())) {
                // Update value
                if (isToNotify(streetSent)) {
                    System.out.println("showing (again)");
                    Pair<Street, LocalDateTime> pairUpdate = new Pair<>(streetSent.convertToStreetForMobileApp(), LocalDateTime.now());
                    this.streetNotificationList.replace(streetSent.getStreetId(), pairUpdate);
                    MobileAppInterface.getInstance().showNotification(streetSent.convertToStreetForMobileApp());
                }
            } else {
                // First time: insert
                System.out.println("showing (first time)");
                Pair<Street, LocalDateTime> pairInsert = new Pair<>(streetSent.convertToStreetForMobileApp(), LocalDateTime.now());
                this.streetNotificationList.putIfAbsent(streetSent.getStreetId(), pairInsert);
                MobileAppInterface.getInstance().showNotification(streetSent.convertToStreetForMobileApp());
            }
        }
    }

    private boolean isToNotify(StreetTrafficHandler currentStreet) {
        long timeDifference = Duration.between(this.streetNotificationList.get(currentStreet.getStreetId()).getValue(), LocalDateTime.now()).toMillis();
        System.out.println("Notification time difference: " + timeDifference);
        return timeDifference > SHOW_AGAIN_TIME;
    }
}