import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.geom.Point2D;

public class Main extends Application {

    private Scene scene;
    private static Group root;
    private static NotificationView notificationView;
    private static NotificationView trafficInfoView;
    private static MapGrid mapGridStreets;
    private static Text streetLabelText;

    public static void main(String[] args) {
        System.out.println("CentralSystem Start...");
        startupCentralSystem();

        // Start Graphical Interface
        launch(args);

        /* ----- SETUP COMPLETE ----- */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Run interface data setup
        notificationViewSetup();
        trafficInfoViewSetup();
        streetLabelTextSetup();
        mapGridStreets = new MapGrid();

        root = new Group(mapGridStreets, notificationView, trafficInfoView, streetLabelText);

        launch(primaryStage, root);
    }

    public static void addMobileAppPointer(Position position, Color pointerColor) {
        Circle pointer = new Circle(
                MapDataSetup.getCoordinatesOnInterfaceFromPositionForCircle(position).x,
                MapDataSetup.getCoordinatesOnInterfaceFromPositionForCircle(position).y,
                MapDataSetup.getCellSize() / 2,
                pointerColor);

        pointer.setStroke(Color.WHITE);
        pointer.setStrokeWidth(0.5);

        FadeTransition ft = new FadeTransition(Duration.millis(400), pointer);
        ft.setDelay(new Duration(1800));
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setOnFinished(event -> root.getChildren().remove(pointer));
        ft.play();

        ScaleTransition st = new ScaleTransition(Duration.millis(2000), pointer);
        st.setDelay(new Duration(200));
        float scaleSize = (MapManager.getInstance().getMapWidth() * 0.03f);
        System.out.println("Scale size: " + scaleSize);
        st.setByX(scaleSize);
        st.setByY(scaleSize);
        st.play();

        root.getChildren().add(pointer);
    }
    private void notificationViewSetup() {
        notificationView = new NotificationView(
                "Reports",
                new Point2D.Double(Toolkit.getDefaultToolkit().getScreenSize().width/8*6 - 20, 10),
                new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/8, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.85))
        );
        notificationView.setIsFadingView(true);
        notificationView.setHasTimeReport(true);
    }
    private void trafficInfoViewSetup() {
        trafficInfoView = new NotificationView(
                "Traffic Info",
                new Point2D.Double(Toolkit.getDefaultToolkit().getScreenSize().width/8*7 - 10, 10),
                new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/8, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.85))
        );

        trafficInfoView.addNotificationNode(TrafficReportDatabase.getInstance().getSavedTrafficData());
    }
    private void streetLabelTextSetup() {
        streetLabelText = new Text();
        streetLabelText.setFont(Font.font(20));
        streetLabelText.setFill(Color.DARKGREY);
        streetLabelText.setY(MapDataSetup.getStartingCoordinatesOnInterface());
        streetLabelText.setX(MapDataSetup.getStartingCoordinatesOnInterface());
        streetLabelText.setOpacity(0.7);
    }

    public static void changeStreetSelectedLabelText(String streetName) {
        if (!streetLabelText.getText().equals(streetName)) streetLabelText.setText(streetName);
    }
    public static NotificationView getTrafficInfoView() {
        return trafficInfoView;
    }
    public static NotificationView getNotificationView() {
        return notificationView;
    }
    private static void startupCentralSystem() {
        // Open Server RMI Connection
        CentralSystemConnector centralSystemConnector = new CentralSystemConnector();
        CentralSystemConnector.RMIConnection(centralSystemConnector);

        // Retrieve map data and street names from external file
        MapManager.getInstance();

        // Initialize street list (it needs the MapManager to be fully ready) and starts the timer to empty app reports
        TrafficReportDatabase.getInstance();
    }
    public static void highlightStreetBasedOnTrafficAmount(int streetId, int trafficAmount) {
        mapGridStreets.highlightStreets(streetId, trafficAmount);
    }

    private void launch(Stage primaryStage, Group root) {
        //Creating a Scene
        scene = new Scene(root);

        //Setting title to the scene
        primaryStage.setTitle("Traffic Monitor v1.0");

        //Adding the scene to the stage
        primaryStage.setScene(scene);

        // Stop system when close button is clicked
        primaryStage.setOnCloseRequest(event -> System.exit(2));

        // Maximized size to fit screen
        primaryStage.setMaximized(true);

        //Displaying the contents of a scene
        primaryStage.show();
    }
}