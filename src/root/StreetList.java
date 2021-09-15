import java.io.Serializable;
import java.util.ArrayList;

public class StreetList implements Serializable {

    private ArrayList<StreetTrafficHandler> streetList;

    public StreetList() {
        streetList = new ArrayList<>();
    }
    public StreetList(ArrayList<StreetTrafficHandler> streetList) {
        this.streetList = streetList;
    }

    public ArrayList<StreetTrafficHandler> getStreetList() {
        return streetList;
    }
    public void addStreet(Street street) {
        try {
            getStreetList().add((StreetTrafficHandler) street);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
