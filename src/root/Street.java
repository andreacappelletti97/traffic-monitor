import java.io.Serializable;

public abstract class Street implements StreetData, Serializable {
    private int streetId;
    private String streetName;

    public Street(int streetId, String streetName) {
        this.streetId = streetId;
        this.streetName = streetName;
    }

    @Override
    public int getStreetId() {
        return streetId;
    }

    @Override
    public String getStreetName() {
        return streetName;
    }

    @Override
    public void printStreet() {
        System.out.print("(" + getStreetId() + ") Street: " + getStreetName() + " - ");
    }
}
