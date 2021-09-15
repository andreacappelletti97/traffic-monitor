import java.io.InvalidObjectException;
import java.io.Serializable;

public class StreetCovered extends Street
        implements StreetTrafficHandler, Serializable {
    private int trafficAmount;

    public StreetCovered(int streetId, String streetName, int trafficAmount) {
        super(streetId, streetName);
        this.trafficAmount = trafficAmount;
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
    public String notificationViewMessage() {
        return getStreetName() + "\n [" + getTrafficAmount() + " stat]";
    }

    @Override
    public int getTrafficAmount() {
        return trafficAmount;
    }

    @Override
    public void setTrafficAmount(State reportSent) throws InvalidObjectException {
        if (reportSent instanceof CentralinaReport) {
            this.trafficAmount = ((CentralinaReport) reportSent).getTrafficAmount();
        } else if (reportSent instanceof AppReport) {
            this.trafficAmount += 1;
        } else {
            throw new InvalidObjectException("Cannot update traffic amount, report sent is not an AppReport nor a CentralinaReport");
        }
    }

    @Override
    public StreetApp convertToStreetForMobileApp() {
        return new StreetApp(getStreetId(), getStreetName(), getTrafficAmount(), true);
    }

    @Override
    public int compareTo(StreetTrafficHandler o) {
        return Integer.compare(this.getTrafficAmount(), o.getTrafficAmount());
    }
}
