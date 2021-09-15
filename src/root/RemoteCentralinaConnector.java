import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCentralinaConnector extends Remote {
    int retrieveTrafficInCentralina() throws RemoteException;
}
