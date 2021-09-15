import java.io.InvalidObjectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SystemModuleConnector implements RemoteCentralSystemConnector {

    @Override
    public void sendReport(State state) {
        try {
            (new ReportGetterStrategy()).handleReport(state);
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StreetList retrieveNotification(Position position, AppPointerColor appPointerColor) throws RemoteException {
        StreetList streetList = new NotificationRetriever().retrieveTrafficInPosition(position, appPointerColor);
        System.out.println(streetList.getStreetList().size() + " notifications");
        return streetList;
    }

    @Override
    public int getTrafficAmountInPosition(Position position) throws RemoteException {
        int trafficAmount =  TrafficReportDatabase.getInstance().getTrafficAmountInPosition(position);
        return trafficAmount == 0 ? 1 : trafficAmount;
    }

    @Override
    public ArrayList<Position> getPositionWithStreet() throws RemoteException {
        return MapDataSetup.getPositionWithStreet();
    }
}
