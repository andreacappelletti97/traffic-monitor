import java.rmi.RemoteException;
import java.util.TimerTask;
import java.util.Timer;

public class UpdatingAppTimer {

    private static UpdatingAppTimer updatingAppTimer;
    private Position currentPosition;
    private static AppPointerColor mobileAppPointerColor;

    // Timer attributes
    private long timerTime;
    private Timer timer;
    private TimerTask timerTask;


    private UpdatingAppTimer() throws RemoteException {
        // Set default pointer color
        if (mobileAppPointerColor == null) mobileAppPointerColor = new AppPointerColor();

        MapDataSetup.setPositionWithStreet(MobileAppConnector.getInstance().getCentralSystemConnector().getPositionWithStreet());
        retrievePositionGPS();
    }
    public static UpdatingAppTimer getInstance() throws RemoteException {
        if (updatingAppTimer == null) {
            return updatingAppTimer = new UpdatingAppTimer();
        }

        return updatingAppTimer;
    }
    public static void setMobileAppPointerColor(double r, double g, double b) {
        mobileAppPointerColor = new AppPointerColor(r, g, b);
    }
    public static AppPointerColor getMobileAppPointerColor() {
        return mobileAppPointerColor;
    }

    public void timerCycle(){
        timer = new Timer();

        //Retrieve notification every time the timer finishes
         timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    // Retrieve and show notifications
                    NotificationManager.getInstance().callShowNotification(
                            MobileAppConnector.getInstance().getCentralSystemConnector().retrieveNotification(currentPosition, mobileAppPointerColor));

                    // Set new MobileApp currentPosition after movement
                    retrievePositionGPS();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        //Schedule the timer to run every TimerUpdate time to retrieve notifications
        System.out.println("NEW TIMER SET FOR: " + timerTime + "s");
        timer.schedule(timerTask, timerTime * 1000);
    }
    private void updateTimerCounter(){
        // Create a timerTime period based on the car speed
        timerTime = 3;

        System.out.println("NEW POSITION GENERATED: " + timerTime + "s");

        // Delete TimerTask
        if (timerTask != null) timerTask.cancel();
        //Delete Timer
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        // Create the new timer
        timerCycle();
    }

    public void retrievePositionGPS() {
        currentPosition = MobileAppMovement.movement(
                currentPosition,
                currentPosition == null ? 1 :setSpeedBasedOnTrafficAmount()
                );
        updateTimerCounter();
    }
    private int setSpeedBasedOnTrafficAmount() {
        int trafficAmountInCurrentPosition = 1; // Default Traffic

        try {
            trafficAmountInCurrentPosition = MobileAppConnector.getInstance().getCentralSystemConnector().getTrafficAmountInPosition(currentPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int speed = trafficToSpeedConverter(trafficAmountInCurrentPosition);

        System.out.println("Speed is: " + speed);
        return speed;
    }
    private int trafficToSpeedConverter(int trafficAmount) {
        double maxTrafficAmount = 20.0;
        double minTrafficAmount = 1.0;
        double maxSpeed = 5.0;
        double minSpeed = 1.0;

        // speed = (100% - (traffic_value - traffic_bottom) / (traffic_top - traffic_bottom)) * (speed_top - speed_bottom) + speed_bottom;
        // Speed based on a inverse linear mapping of (20 - 1)[traffic] -> (1 - 5)[speed] (more traffic less speed and vice versa)
        return (int) ((1.0 - (trafficAmount - minTrafficAmount) / (maxTrafficAmount - minTrafficAmount)) * (maxSpeed - minSpeed) + minSpeed);
    }
    public void resetNewRandomPosition() {
        MobileAppMovement.reset();
        currentPosition = null;
        retrievePositionGPS();
    }
    public Position getCurrentPosition() {
        return currentPosition;
    }
}

