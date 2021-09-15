import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.InvalidObjectException;

public class ReportGetterStrategy {
    public void handleReport(State report) throws InvalidObjectException {
        // Do not send AppReport if not valid (Check Position in the msp)
        if (report instanceof AppReport) {
            Platform.runLater(() -> Main.addMobileAppPointer(((AppReport) report).getPosition(), Color.RED));
            if (!AppReportChecker.isAppReportValid((AppReport) report)) {
                System.out.println("App report invalid, thrown!");
                return;
            }

            System.out.println("App report valid, saved");
        }

        TrafficReportDatabase.getInstance().saveTrafficData(report);
    }
}
