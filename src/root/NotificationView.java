import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.LocalTime;

public class NotificationView extends ScrollPane {

    private final int textLineHeightSpacing = 8;
    private final int panePadding = 16;
    private final int showingDurationMillis = 6000;
    private final int fadeOutDurationMillis = 600;
    private boolean isFadingView = false;
    private boolean hasTimeReport = false;

    private Pane mainPane;

    public NotificationView(String paneTitle, Point2D.Double position, Dimension prefSize) {
        // Call default setup
        defaultSetup();
        mainPaneSetup();
        titleSetup(paneTitle);

        // Pane position in the screen
        this.setLayoutX(position.x);
        this.setLayoutY(position.y);

        // ScrollPane sizes
        this.setPrefHeight(prefSize.height);
        this.setPrefWidth(prefSize.width);

        // Set pane sizes
        mainPane.setPrefWidth(prefSize.width - panePadding * 1.3);
        mainPane.setPrefHeight(prefSize.height - panePadding * 1.3);

        // Set content
        this.setContent(mainPane);
    }
    private void defaultSetup() {
        // ScrollPane setup
        this.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), new CornerRadii(12), Insets.EMPTY)));
        this.setPadding(new Insets(panePadding >> 1));
        this.setOpacity(0.8);
        this.setBorder(new Border(new BorderStroke(Color.AQUAMARINE, BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(2))));
    }
    private void mainPaneSetup() {
        // Main pane setup
        mainPane = new Pane();
        getMainPane().setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    private void titleSetup(String paneTitle) {
        // Pane title setup
        Text title = new Text(paneTitle);
        title.setTextOrigin(VPos.TOP);
        title.setX(panePadding);
        title.setY(this.getChildren().size() * textLineHeightSpacing + (panePadding >> 1));
        title.setFont(Font.font(21));

        mainPane.getChildren().add(title);
    }

    public Text addNotificationNodeSingle(StreetTrafficRetriever streetNotification) {
        String message = streetNotification.notificationViewMessage();
        if (hasTimeReport) message += "\n" + LocalTime.now();

        Text textNotification = new Text(message);
        textNotification.setTextOrigin(VPos.TOP);
        textNotification.setX(getMainPane().getChildren().get(0).getLayoutBounds().getMinX());
        textNotification.setY(getMainPane().getChildren().get(0).getLayoutBounds().getMaxY() + textLineHeightSpacing);
        textNotification.setEffect(new DropShadow(0.1, -1, 0, Color.GRAY));

        if (isFadingView) {
            FadeTransition ft = new FadeTransition(Duration.millis(fadeOutDurationMillis), textNotification);
            ft.setDelay(new Duration(showingDurationMillis));
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setOnFinished(event -> getChildren().remove(textNotification));
            ft.play();
        }

        for(int child = 1; child < getMainPane().getChildren().size(); ++child) {
            Text myChild = ((Text) getMainPane().getChildren().get(child));
            myChild.setY(myChild.getLayoutBounds().getMaxY() + textLineHeightSpacing);
        }

        return textNotification;
    }

    public Pane getMainPane() {
        return mainPane;
    }
    public void setHasTimeReport(boolean hasTimeReport) {
        this.hasTimeReport = hasTimeReport;
    }
    public void setIsFadingView(boolean fadingView) {
        isFadingView = fadingView;
    }

    public void addNotificationNode(StreetList streetNotification) {
        for (StreetTrafficRetriever streetTraffic: streetNotification.getStreetList()) {
            getMainPane().getChildren().add(addNotificationNodeSingle(streetTraffic));
        }
    }

    public void clearAndAddNotification(StreetTrafficRetriever streetTrafficRetriever) {
        Platform.runLater(() -> getMainPane().getChildren().add(addNotificationNodeSingle(streetTrafficRetriever)));
    }
    public void clearAndAddNotificationNode(StreetList streetNotification) {
        Platform.runLater(() -> {
            getMainPane().getChildren().remove(1, getMainPane().getChildren().size());
            addNotificationNode(streetNotification);
        });
    }
}
