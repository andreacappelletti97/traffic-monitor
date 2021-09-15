import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {

    private Timer timer; //timer object
    private long timerTime; //waiting time till next report creation and sending
    private static TimerManager instance; //INSTANCE

    int maxTraffic; //max value of traffic

    public long getTimerTime() {
        return timerTime;
    }

    public Timer getTimer() {
        return timer;
    }

    public static TimerManager getInstance(){
        if (instance == null)
            instance = new TimerManager();
        return instance;
    }
    private TimerManager() {
        this.timerTime = 100; // Starting timer time interval
        maxTraffic = ReportCreator.getInstance().getMaxTraffic(); //get max traffic value
        backgroundTimerHandler();
    }

    public void updateTimerCounter(int lastTrafficAmount) {
        // Delete old timer
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
        }


        // Next sending takes place after a time that is proportional
        // to the last traffic data detected and sent, there is 3 possible interval
        int maxInterval = 20000; // Max delay
        int minInterval = 3000; // Min delay
        timerTime = (long) (maxInterval - (maxInterval - minInterval) * ((float) lastTrafficAmount / this.maxTraffic));

        // Create the new timer
        backgroundTimerHandler();
    }

    // Call for sending report and set new waiting time
    private void backgroundTimerHandler() {
        if (timer == null);
            timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ReportSender.sendReport();
            }
        };

        timer.schedule(timerTask, timerTime);
    }
}