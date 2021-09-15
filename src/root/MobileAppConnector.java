import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MobileAppConnector {

    private static MobileAppConnector  mobileAppConnector;
    private RemoteCentralSystemConnector centralSystemConnector;

    public static MobileAppConnector getInstance() {
        if (mobileAppConnector == null) {
            mobileAppConnector = new MobileAppConnector();
        }

        return mobileAppConnector;
    }
    private MobileAppConnector() {}

    public void openConnection() throws RemoteException {
        // Open RMI connection with centralSystemConnector
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(1001);

            // Looking up the registry for the remote object
            RemoteCentralSystemConnector centralSystemStub = (RemoteCentralSystemConnector) registry.lookup("CentralSystemConnector");

            // Calling the remote method using the obtained object
            centralSystemConnector = centralSystemStub;

            System.out.println("Central System connection opened successfully");
        } catch (Exception e) {
            System.err.println("RMI Client exception: " + e.toString());
            e.printStackTrace();
        }

        UpdatingAppTimer.getInstance();
    }
    public void sendAppReport() throws RemoteException {
        // Send AppReport through centralSystemConnector Facade with createAppReport
        centralSystemConnector.sendReport(createAppReport());
    }
    public RemoteCentralSystemConnector getCentralSystemConnector() {
        return centralSystemConnector;
    }

    private AppReport createAppReport() throws RemoteException {
        // Get position and create a new AppReport
        return new AppReport(UpdatingAppTimer.getInstance().getCurrentPosition());
    }
}
