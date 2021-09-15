import java.util.Timer;
import java.util.TimerTask;

public class ReportCreator {

    private int trafficGeneratorDelay; // Time interval(ms) between traffic value generation

    /* SETTING PARAMETERS */
    final private int maxTraffic; // Maximum value of traffic
    final private int minTraffic; // Minimum value of traffic
    final private int maxVariation; // How far it can vary from one point to another

    private int currentTraffic; // Actual traffic value

    private CentralinaStatus centralinaInformation; // contains street identifier
    private static ReportCreator reportCreator; //INSTANCE

    public static ReportCreator getInstance() {
        return reportCreator;
    }

    // this creator method prevent exposing constructor
    public static void createReportCreator(CentralinaStatus state){
        if (reportCreator == null)
            reportCreator = new ReportCreator(state);
    }

    private ReportCreator(CentralinaStatus state) {
        this.maxTraffic = 20;
        this.minTraffic = 1;
        this.currentTraffic =  minTraffic;
        this.maxVariation = 4;
        this.centralinaInformation = state; // contructor bind new centralina with proper street
        this.trafficGeneratorDelay = 200;
        trafficGenerator();
    }

    public int getMaxTraffic() {
        return this.maxTraffic;
    }

    public int getMinTraffic() {
        return this.minTraffic;
    }

    public int getMaxVariation() {
        return maxVariation;
    }

    public CentralinaReport createCentralinaReport() {
        return new CentralinaReport(getCentralinaInformation().getStreetId(), currentTraffic);
    }

    // Actual creator of new traffic data
    // TRAFFIC SIMULATION IS CONSIDERABLY SPEEDED UP!!
    public static int retrieveDataTraffic(int currentTraffic, int maxTraffic, int minTraffic, int maxVariation){
        int test = currentTraffic;
        test += (int) (((float)maxVariation/2) - (Math.random()*maxVariation));
        if (test > maxTraffic)
            test = maxTraffic;
        if (test < minTraffic)
            test = minTraffic;
        return test;
    }

    //new timer that manage the creation
    private void trafficGenerator(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTraffic = retrieveDataTraffic(currentTraffic, maxTraffic, minTraffic, maxVariation);
            }
        };
        timer.schedule(timerTask, 100,(long) trafficGeneratorDelay);
    }

    public CentralinaStatus getCentralinaInformation() {
        return centralinaInformation;
    }

    public int getTrafficGeneratorDelay() {
        return trafficGeneratorDelay;
    }
}
