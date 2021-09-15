import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CentralSystemConnector extends SystemModuleConnector {
    public static void RMIConnection(CentralSystemConnector systemConnector) {
        try {
            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            RemoteCentralSystemConnector centralSystemStub = (RemoteCentralSystemConnector) UnicastRemoteObject.exportObject(systemConnector, 101);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1001);

            registry.bind("CentralSystemConnector", centralSystemStub);
            System.err.println("Central System ready");
        } catch (Exception e) {
            System.err.println("Central System exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
