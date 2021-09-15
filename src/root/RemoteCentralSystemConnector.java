import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteCentralSystemConnector extends Remote {
    void sendReport(State state) throws RemoteException;
    StreetList retrieveNotification(Position position, AppPointerColor appPointerColor) throws RemoteException;
    int getTrafficAmountInPosition(Position position) throws RemoteException;
    ArrayList<Position> getPositionWithStreet() throws RemoteException;
}
