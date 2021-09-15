import java.io.InvalidObjectException;
import java.time.LocalDateTime;

public class StreetApp extends Street implements StreetTrafficRetriever {
    private int trafficAmount;
    private boolean isTrafficFromCentralina;
    private LocalDateTime lastUpdate;

    public StreetApp(int streetId, String streetName, int trafficAmount, boolean isTrafficFromCentralina) {
        super(streetId, streetName);
        this.trafficAmount = trafficAmount;
        this.isTrafficFromCentralina = isTrafficFromCentralina;
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    public int getStreetId() {
        return super.getStreetId();
    }

    @Override
    public String getStreetName() {
        return super.getStreetName();
    }

    @Override
    public int getTrafficAmount() {
        return this.trafficAmount;
    }

    @Override
    public String notificationViewMessage() {
        return "MobileApp\n " + getStreetName() + "\n  Traffic: " + getTrafficAmount();
    }

    public boolean isTrafficFromCentralina() {
        return isTrafficFromCentralina;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    public void printLastUpdate(){
        System.out.println("Last update: " + getLastUpdate());
    }
}
