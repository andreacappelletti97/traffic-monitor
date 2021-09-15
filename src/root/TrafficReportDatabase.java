import java.io.InvalidObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class TrafficReportDatabase {

    private static TrafficReportDatabase trafficReportDatabase;
    private StreetList savedTrafficData;
    private long millisecondsLimitOfAppReportValidity;
    private int checkValidityTimerTime;

    private TrafficReportDatabase() {
        initializeSavedTrafficData();

        this.millisecondsLimitOfAppReportValidity = 1000 * 60;
        this.checkValidityTimerTime = 1000 * 4;
        checkAppReportValidityTimer();
    }
    public static TrafficReportDatabase getInstance() {
        if (trafficReportDatabase == null) {
            trafficReportDatabase = new TrafficReportDatabase();
        }

        return trafficReportDatabase;
    }

    public void saveTrafficData(State reportSent) throws InvalidObjectException, IllegalStateException {
        // Find the street from which the report was sent
        StreetTrafficHandler streetTrafficRetriever = getStreetFromStateReportSent(reportSent);

        // Set street traffic amount (do not if AppReport sent from StreetCovered!)
        if (!(reportSent instanceof AppReport && streetTrafficRetriever instanceof StreetCovered)) {
            streetTrafficRetriever.setTrafficAmount(reportSent);
            Main.highlightStreetBasedOnTrafficAmount(streetTrafficRetriever.getStreetId(), streetTrafficRetriever.getTrafficAmount());
        }

        // Show report notification
        showReportNotificationOnInterface(streetTrafficRetriever);
    }

    // Get all list of streets from MapManager 'retrieveStreetList()'
    private void initializeSavedTrafficData() throws IllegalStateException {
        if (savedTrafficData == null) {
            this.savedTrafficData = MapManager.getInstance().retrieveStreetList();
        } else {
            throw new IllegalStateException("Variable 'savedTrafficData' already initialized");
        }
    }
    public StreetList retrieveTrafficDataFromListOfStreetIndexes(ArrayList<Integer> listOfStreets) {
        StreetList listOfStreetsReturn = new StreetList();
        for (Integer streetIndex: listOfStreets) {
            listOfStreetsReturn.getStreetList().add(getSavedTrafficData().getStreetList().get(streetIndex));
        }

        return listOfStreetsReturn;
    }
    public StreetList getSavedTrafficData() {
        return savedTrafficData;
    }
    public int getTrafficAmountInPosition(Position position) {
        return getSavedTrafficData().getStreetList().get(MapManager.getInstance().retrieveStreetIdFromPosition(position)).getTrafficAmount();
    }
    public String getStreetNameFromPosition(Position position) {
        return getSavedTrafficData().getStreetList().get(MapManager.getInstance().retrieveStreetIdFromPosition(position)).getStreetName();
    }

    private StreetTrafficHandler getStreetFromStateReportSent(State reportState) throws InvalidObjectException, IllegalStateException {
        /*
        OPTIONS
        1 | CentralinaReport in StreetCovered or AppReport in StreetEmpty (legit, save report)
        2 | AppReport in StreetCovered (legit, retrieve the traffic data from the Centralina in the street)
        3 | CentralinaReport in StreetEmpty (This cannot happen, if there is a Centralina, then the street must be covered)
        4 | Something else (Theoretically impossible but possibly unexpected, throw error)
         */
        StreetTrafficHandler streetData = getSavedTrafficData().getStreetList().get(getStreetIdFromStateReportSent(reportState));
        System.out.println("Report from: " + streetData.getStreetId() + " " + streetData.getTrafficAmount());

        if ((reportState instanceof CentralinaReport && streetData instanceof StreetCovered) ||
                (reportState instanceof AppReport && streetData instanceof StreetEmpty)) {
            return streetData;
        } else if (reportState instanceof AppReport && streetData instanceof StreetCovered) {
            try {
                System.out.println("App report sent from a street covered by a Centralina");
                return new StreetCovered(
                        streetData.getStreetId(),
                        streetData.getStreetName(),
                        openConnectionWithCentralina(streetData.getStreetId()).retrieveTrafficInCentralina());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (reportState instanceof CentralinaReport && streetData instanceof StreetEmpty) {
            // NB: Case CentralinaReport + StreetEmpty will (should) never happen
            throw new IllegalStateException("!!! WARINING !!! A CentralinaReport has been sent from a StreetEmpty. There is a bug in the way the system stores the 'savedTrafficData' variable");
        } else {
            throw new InvalidObjectException("State sent is not a CentralinaReport or an AppReport");
        }

        return null;
    }
    private int getStreetIdFromStateReportSent(State reportState) throws InvalidObjectException {
        if (reportState instanceof CentralinaReport) {
            return  ((CentralinaReport) reportState).getStreetId();
        } else if (reportState instanceof AppReport) {
            return MapManager.getInstance().retrieveStreetIdFromPosition(((AppReport) reportState).getPosition());
        } else {
            throw new InvalidObjectException("Cannot find streetId from Report, State sent is not a CentralinaReport or an AppReport");
        }
    }

    private RemoteCentralinaConnector openConnectionWithCentralina(int centralinaStreetId) {
        // Open RMI connection with centralSystemConnector
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(100 * (centralinaStreetId + 1));

            // Looking up the registry for the remote object
            RemoteCentralinaConnector centralinaStub = (RemoteCentralinaConnector) registry.lookup("Centralina" + centralinaStreetId);
            System.out.println("Central System - Centralina " + centralinaStreetId + " connection opened successfully");

            return centralinaStub;
        } catch (Exception e) {
            System.err.println("RMI Client exception: " + e.toString());
            e.printStackTrace();
        }

        return null;
    }
    // Remove AppReport from list of StreetEmpty reports if expired
    private void checkAppReportValidityTimer() {
        StreetList streetEmptyList = getAllStreetEmptyList();
        Timer streetEmptyAppReportResetTimer = new Timer();
        streetEmptyAppReportResetTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (StreetTrafficHandler streetEmpty: streetEmptyList.getStreetList()) {
                    ArrayList<AppReport> streetSavedReports = ((StreetEmpty) streetEmpty).getReports();

                    for (int reportToRemoveIndex = streetSavedReports.size() - 1; reportToRemoveIndex >= 0; --reportToRemoveIndex) {
                        if (!isAppReportStillValid(streetSavedReports.get(reportToRemoveIndex))) {
                            streetSavedReports.get(reportToRemoveIndex).printAppReportInformation();
                            streetSavedReports.remove(reportToRemoveIndex);

                            // Update street traffic highlight color
                            Main.highlightStreetBasedOnTrafficAmount(streetEmpty.getStreetId(), streetEmpty.getTrafficAmount());
                            System.out.println("AppReport expired has been removed!");
                        } else {
                            // Since the AppReport are stored in chronological order, if you reach a still valid AppReport,
                            // the others in the list are valid too, you stop checking them
                            break;
                        }
                    }
                }
            }
        }, 0, checkValidityTimerTime);
    }
    private StreetList getAllStreetEmptyList() {
        StreetList listOfEmptyStreets = new StreetList();

        for (StreetTrafficHandler street: getSavedTrafficData().getStreetList()) {
            if (street instanceof StreetEmpty) listOfEmptyStreets.addStreet((StreetEmpty) street);
        }

        return listOfEmptyStreets;
    }
    private boolean isAppReportStillValid(AppReport appReport) {
        return Duration.between(appReport.getReportDateTime(),
                LocalDateTime.now()).toMillis() <= millisecondsLimitOfAppReportValidity;
    }
    private void showReportNotificationOnInterface(StreetTrafficRetriever streetTrafficRetriever) {
        if (Main.getNotificationView() != null) {
            showSingleReport(streetTrafficRetriever);
            showAllStreetListSorted();
        }
    }
    private void showSingleReport(StreetTrafficRetriever streetTrafficRetriever) {
        // Single report
        Main.getNotificationView().clearAndAddNotification(streetTrafficRetriever);
    }
    private void showAllStreetListSorted() {
        // Sorted list of street
        StreetList sortList = new StreetList((ArrayList<StreetTrafficHandler>) getSavedTrafficData().getStreetList().clone());
        Collections.sort(sortList.getStreetList());
        Main.getTrafficInfoView().clearAndAddNotificationNode(sortList);
    }
}
