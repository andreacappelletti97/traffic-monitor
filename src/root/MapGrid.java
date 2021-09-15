import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.HashMap;


public class MapGrid extends Group {

    private HashMap<Integer, Group> streetRectangles;

    public MapGrid() {
        // Draw grid reference lines
        // Vertical lines
        for (int grid = 0; grid <= MapManager.getInstance().getMapWidth(); ++grid) {
            addLineVertical(grid);
        }
        // Horizontal lines
        for (int grid = 0; grid <= MapManager.getInstance().getMapHeight(); ++grid) {
            addLineHorizontal(grid);
        }

        // Mark streets on the map
        streetRectangles = new HashMap<>();
        for (int y = 0; y < MapManager.getInstance().getMapHeight(); ++y) {
            for (int x = 0; x < MapManager.getInstance().getMapWidth(y); ++x) {
                int streetIdInPosition = MapManager.getInstance().getStreetIdFromPosition(new Position(x, y));
                if (streetIdInPosition != -1) {
                    addStreetRectangle(streetIdInPosition, new Position(x, y));
                }
            }
        }
    }

    private void addLineVertical(int x) {
        Line verticalLine = defaultLine();

        // Set Start and End points
        Point2D.Double verticalOrigin = MapDataSetup.getCoordinatesOnInterfaceFromPosition(
                new Position(x, 0)
        );
        Point2D.Double verticalEnding = MapDataSetup.getCoordinatesOnInterfaceFromPosition(
                new Position(x, MapManager.getInstance().getMapHeight())
        );

        // Draw vertical line
        verticalLine.setStartX(verticalOrigin.x);
        verticalLine.setStartY(verticalOrigin.y);
        verticalLine.setEndX(verticalEnding.x);
        verticalLine.setEndY(verticalEnding.y);

        this.getChildren().add(verticalLine);
    }
    private void addLineHorizontal(int y) {
        Line horizontalLine = defaultLine();

        // Set Start and End points
        Point2D.Double horizontalOrigin = MapDataSetup.getCoordinatesOnInterfaceFromPosition(
                new Position(0, y)
        );
        Point2D.Double horizontalEnding = MapDataSetup.getCoordinatesOnInterfaceFromPosition(
                new Position(MapManager.getInstance().getMapWidth(), y)
        );

        // Draw horizontal line
        horizontalLine.setStartX(horizontalOrigin.x);
        horizontalLine.setStartY(horizontalOrigin.y);
        horizontalLine.setEndX(horizontalEnding.x);
        horizontalLine.setEndY(horizontalEnding.y);

        this.getChildren().add(horizontalLine);
    }

    private void addStreetRectangle(int streetId, Position position) {
        Rectangle rectangle = new Rectangle(
                MapDataSetup.getCellSize(),
                MapDataSetup.getCellSize(),
                trafficToColorConverter(0));

        rectangle.setX(MapDataSetup.getCoordinatesOnInterfaceFromPosition(position).x);
        rectangle.setY(MapDataSetup.getCoordinatesOnInterfaceFromPosition(position).y);
        rectangle.setOnMouseEntered(event -> Main.changeStreetSelectedLabelText(
                TrafficReportDatabase.getInstance().getStreetNameFromPosition(position) + " | Traffic: " +
                TrafficReportDatabase.getInstance().getTrafficAmountInPosition(position)));
        rectangle.setOpacity(0.8);

        if (streetRectangles.get(streetId) == null) streetRectangles.put(streetId, new Group());
        streetRectangles.get(streetId).getChildren().add(rectangle);

        if (!this.getChildren().contains(streetRectangles.get(streetId))) {
            this.getChildren().add(streetRectangles.get(streetId));
        }
    }

    public void highlightStreets(int streetId, int trafficAmount) {
        for (Node rect: streetRectangles.get(streetId).getChildren()) {
            ((Rectangle) rect).setFill(trafficToColorConverter(trafficAmount));
        }
    }

    private Color trafficToColorConverter(int trafficAmount) {
        double maxTrafficAmount = 20.0;
        double minTrafficAmount = 1.0;
        double maxColorIndex = .7;
        double minColorIndex = 0;

        // Color based on a inverse linear mapping of (20 - 1)[traffic] -> (0 - 0.7)[color rgb] (more traffic less darker color and vice versa)
        double colorIndex = (1.0 - (trafficAmount - minTrafficAmount) / (maxTrafficAmount - minTrafficAmount)) * (maxColorIndex - minColorIndex) + minColorIndex;
        return new Color(colorIndex, colorIndex, colorIndex, 1);
    }
    private Line defaultLine() {
        Line newLine = new Line();
        newLine.setStrokeWidth(1);
        newLine.setStroke(Color.rgb(0, 0, 0, 0.02));

        return newLine;
    }
}
