public class AppReportChecker {
    public static boolean isAppReportValid(AppReport report) {
        return MapManager.getInstance().isPositionValidInTheMap(report.getPosition());
    }
}
