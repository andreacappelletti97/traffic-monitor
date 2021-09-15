import java.io.Serializable;
import java.time.LocalDateTime;

public class AppReport implements State, Serializable {
    private Position position;
    private LocalDateTime reportDateTime;

    public AppReport(Position position) {
        this.position = position;
        this.reportDateTime = LocalDateTime.now();
    }
    public void printAppReportInformation() {
        this.position.printPosition();
        System.out.println(this.reportDateTime);
    }
    public Position getPosition() {
        return position;
    }
    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }
}
