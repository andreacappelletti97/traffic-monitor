import java.io.InvalidObjectException;

public interface StreetTrafficHandler extends StreetTrafficRetriever, Comparable<StreetTrafficHandler> {
    void setTrafficAmount(State reportSent) throws InvalidObjectException;
    StreetApp convertToStreetForMobileApp();
}
