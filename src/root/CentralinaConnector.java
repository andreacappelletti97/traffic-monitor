import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CentralinaConnector implements RemoteCentralinaConnector {

    private static CentralinaConnector centralinaConnector;
    private RemoteCentralSystemConnector centralSystemConnector;

    public static CentralinaConnector getInstance() {
        if (centralinaConnector == null) {
            centralinaConnector = new CentralinaConnector();
        }

        return centralinaConnector;
    }

    private CentralinaConnector() {
        openConnection();
        openCentralinaRMIConnection();
    }

    public void openConnection() {
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
    }
    public void openCentralinaRMIConnection() {
        int centralinaStreetId = ReportCreator.getInstance().getCentralinaInformation().getStreetId();
        try {
            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            RemoteCentralinaConnector centralinaStub = (RemoteCentralinaConnector) UnicastRemoteObject.exportObject(this, centralinaStreetId + 1);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(100 * (centralinaStreetId + 1));

            registry.bind("Centralina" + centralinaStreetId, centralinaStub);
            System.err.println("Centralina in street " + centralinaStreetId + " ready");
        } catch (Exception e) {
            System.err.println("Centralina in street " + centralinaStreetId + " exception: " + e.toString());
            e.printStackTrace();
        }
    }
    public void sendCentralinaReport(State centralinaReport){
        try {
            centralSystemConnector.sendReport(centralinaReport);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int retrieveTrafficInCentralina() throws RemoteException {
        int trafficAmount = ReportCreator.getInstance().createCentralinaReport().getTrafficAmount();

        // Reset timer with new trafficAmount
        TimerManager.getInstance().updateTimerCounter(trafficAmount);
        System.out.println("Report asks to check for the traffic: " + trafficAmount);
        return trafficAmount;
    }
}
