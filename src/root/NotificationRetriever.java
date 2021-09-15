import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class NotificationRetriever {

    private int notificationRadius;
    private int isTrafficLimit;

    public NotificationRetriever() {
        this.isTrafficLimit = 5;
        this.notificationRadius = 4;
    }

    public StreetList retrieveTrafficInPosition(Position position, AppPointerColor appPointerColor) {
        StreetList streetList = getListOfStreetsWithTrafficAmount(getIndexesOfStreetsInRadius(position));
        removeStreetsWithNotEnoughTraffic(streetList);
        Platform.runLater(() -> Main.addMobileAppPointer(position, convertAppPointerColorToColor(appPointerColor)));

        return streetList;
    }
    private StreetList getListOfStreetsWithTrafficAmount(ArrayList<Integer> listOfStreetIndexes) {
        return TrafficReportDatabase.getInstance().retrieveTrafficDataFromListOfStreetIndexes(listOfStreetIndexes);
    }
    private ArrayList<Integer> getIndexesOfStreetsInRadius(Position position) {
        return MapManager.getInstance().retrieveListOfStreetInRadius(this.notificationRadius, position);
    }
    private void removeStreetsWithNotEnoughTraffic(StreetList streetList) {
        // Remove street which has not enough traffic
        // Using Iterator 'removeIf'
        streetList.getStreetList().removeIf(trafficAmountHandler -> !isRealTraffic((trafficAmountHandler).getTrafficAmount()));
    }
    private boolean isRealTraffic(int trafficAmount) {
        return trafficAmount > this.isTrafficLimit;
    }
    private Color convertAppPointerColorToColor(AppPointerColor appPointerColor) {
        return new Color(appPointerColor.getR(), appPointerColor.getG(), appPointerColor.getB(), 1);
    }
}
