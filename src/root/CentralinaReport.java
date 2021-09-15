import java.io.Serializable;

public class CentralinaReport extends CentralinaStatus
        implements Serializable {

    private int trafficAmount;

    public CentralinaReport(int streetId, int trafficAmount) {
        super(streetId);
        this.trafficAmount = trafficAmount;
    }
    public int getTrafficAmount() {
        return trafficAmount;
    }
}
