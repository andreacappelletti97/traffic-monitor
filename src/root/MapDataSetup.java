import javafx.stage.Screen;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MapDataSetup {

    // Map Source Directory in resources
    private static final String mapDirectory = "MapData3//";

    // All 'Position' that contain a street
    private static ArrayList<Position> positionWithStreet;

    // Cell sizes (map display on interface)
    private static Double cellSize;

    private static final int topLeftMargin = 24;

    public static String getMapDirectory() {
        return mapDirectory;
    }

    public static ArrayList<Position> getPositionWithStreet() {
        if (positionWithStreet == null) {
            throw new NullPointerException("List of 'positionWithStreet' has not been initialized");
        }

        return positionWithStreet;
    }

    public static void setPositionWithStreet(ArrayList<Position> positionWithStreet) {
        MapDataSetup.positionWithStreet = positionWithStreet;
    }

    public static double getCellSize() {
        if (cellSize == null) {
            double screenHeightSpace = Screen.getPrimary().getBounds().getHeight() - 100;
            double screenWidthSpace = Screen.getPrimary().getBounds().getWidth() * 0.7;
            double minCellSize = Math.min(
                    screenHeightSpace / MapManager.getInstance().getMapHeight(),
                    screenWidthSpace / MapManager.getInstance().getMapWidth());

            cellSize = minCellSize;
        }

        return cellSize;
    }

    public static Point2D.Double getCoordinatesOnInterfaceFromPosition(Position position) {
        return new Point2D.Double(
                (getCellSize() * position.getxPosition()) + topLeftMargin,
                (getCellSize() * position.getyPosition()) + topLeftMargin);
    }
    public static Point2D.Double getCoordinatesOnInterfaceFromPositionForCircle(Position position) {
        return new Point2D.Double(
                (getCellSize() * position.getxPosition()) + topLeftMargin + (getCellSize() / 2),
                (getCellSize() * position.getyPosition()) + topLeftMargin + (getCellSize() / 2));
    }
    public static double getStartingCoordinatesOnInterface() {
        return topLeftMargin;
    }
}
