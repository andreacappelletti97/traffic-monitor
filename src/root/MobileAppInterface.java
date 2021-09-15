import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class MobileAppInterface {

    private static MobileAppInterface mobileAppInterface;

    private JPanel rootPanel;
    private JLabel statusLabel;
    private JButton sendReport;
    private JButton setRandomPositionButton;
    private JLabel mobileAppPointerColorLabel;

    private LocalDateTime lastReportSent;
    private long millisBetweenReports = 1000 * 30;
    private Thread notificationPanelThread;

    public static MobileAppInterface getInstance(){
        if (mobileAppInterface == null) {
            mobileAppInterface = new MobileAppInterface();
        }

        return mobileAppInterface;
    }
    private MobileAppInterface() {
        // Open Connection through MobileAppConnector Instance
        try {
            MobileAppConnector.getInstance().openConnection();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Initialize Buttons
        String rgbColor = UpdatingAppTimer.getMobileAppPointerColor().getR() * 255 + ", " +
                UpdatingAppTimer.getMobileAppPointerColor().getG() * 255 + ", " +
                UpdatingAppTimer.getMobileAppPointerColor().getB() * 255;

        statusLabel.setText("<html><b><font color='#33f747'>Connected</font></b></html>");
        mobileAppPointerColorLabel.setText("<html><b><font style='color: rgb(" + rgbColor + ")'>MobileApp Pointer Color</font></b></html>");

        // SendReport Button on Action
        sendReport.addActionListener(e -> {
            try {
                if (lastReportSent == null ||
                        Duration.between(lastReportSent, LocalDateTime.now()).toMillis() > millisBetweenReports) {
                    lastReportSent = LocalDateTime.now();
                    MobileAppConnector.getInstance().sendAppReport();
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });

        // Set a new Random Position on the MAP
        // Additional request by the Tutor
        setRandomPositionButton.addActionListener(e -> {
            try {
                UpdatingAppTimer.getInstance().resetNewRandomPosition();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void showNotification(StreetApp streetApp){
        String notificationMessage =
                "Traffico " + generateWarningFromTrafficAmount(streetApp.getTrafficAmount()) +
                        " rilevato in " + streetApp.getStreetName();
        if (!streetApp.isTrafficFromCentralina()) {
            notificationMessage =
                    streetApp.getTrafficAmount() + " utenti segnalano la presenza di traffico in " + streetApp.getStreetName();
        }

        String finalNotificationMessage = notificationMessage;
        notificationPanelThread = new Thread(() -> JOptionPane.showMessageDialog(
                null,
                finalNotificationMessage,
                "Attenzione Traffico - " + LocalDateTime.now(),
                JOptionPane.WARNING_MESSAGE));
        notificationPanelThread.start();

        System.out.println("Stop execution");
    }

    private String generateWarningFromTrafficAmount(int trafficAmount) {
        double maxTraffic = 20.0;
        double minTraffic = 5.0;
        double trafficAmountPercentage = (trafficAmount - minTraffic) / (maxTraffic - minTraffic);
        if (trafficAmountPercentage < 0.25) return "leggero";
        else if (trafficAmountPercentage >= 0.25 && trafficAmountPercentage < 0.5) return "moderato";
        else if (trafficAmountPercentage >= 0.5 && trafficAmountPercentage < 0.75) return "elevato";
        return "molto elevato";
    }

    public static void main(String[] args) {
        // Set app pointer color
        Random randomGenerator = new Random();
        UpdatingAppTimer.setMobileAppPointerColor(
                randomGenerator.nextDouble(),
                randomGenerator.nextDouble(),
                randomGenerator.nextDouble());

        MobileAppInterface mobileAppInterface = new MobileAppInterface();
        JFrame frame = new JFrame("TRAFFIC MONITOR APP");
        JPanel rootPanel = mobileAppInterface.rootPanel;
        rootPanel.setBackground(Color.darkGray);
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300,500));
        frame.pack();
        frame.setVisible(true);
    }
}
