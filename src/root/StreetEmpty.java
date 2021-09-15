import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.ArrayList;

public class StreetEmpty extends Street
        implements StreetTrafficHandler, Serializable {
    private ArrayList<AppReport> reports;

    public StreetEmpty(int streetId, String streetName) {
        super(streetId, streetName);
        reports = new ArrayList<>();
    }

    public ArrayList<AppReport> getReports() {
        return reports;
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
        return getReports().size();
    }

    @Override
    public String notificationViewMessage() {
        return getStreetName() + "\n [" + getTrafficAmount() + " repo]";
    }

    @Override
    public void setTrafficAmount(State reportSent) throws InvalidObjectException {
        if (reportSent instanceof AppReport) {
            addAppReport((AppReport) reportSent);
        } else {
            throw new InvalidObjectException("Cannot add report because report passed is not an AppReport");
        }
    }

    @Override
    public StreetApp convertToStreetForMobileApp() {
        return new StreetApp(getStreetId(), getStreetName(), getTrafficAmount(), false);
    }

    public void addAppReport(AppReport appReport) {
        reports.add(appReport);
    }

    @Override
    public int compareTo(StreetTrafficHandler o) {
        return Integer.compare(this.getTrafficAmount(), o.getTrafficAmount());
    }
}
