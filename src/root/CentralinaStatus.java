public class CentralinaStatus implements State {
    private int streetId;

    public CentralinaStatus(int streetId) {
        this.streetId = streetId;
    }
    public int getStreetId() {
        return streetId;
    }
}
