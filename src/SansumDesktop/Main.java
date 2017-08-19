package SansumDesktop;

import com.backendless.Backendless;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    public static Parent displayRoot;
    public static Parent updateRoot;

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {

            Screen externalScreen = Screen.getScreens().get(1);

            Rectangle2D externalMonitorBounds = externalScreen.getVisualBounds();

            Stage displayStage = new Stage();
            displayRoot = FXMLLoader.load(getClass().getResource("DisplayWindow.fxml"));
            displayStage.setTitle("Waiting Room Display");
            displayStage.setScene(new Scene(displayRoot, 600, 400));
            displayStage.setX(externalMonitorBounds.getMinX() + 100);
            displayStage.setY(externalMonitorBounds.getMinY() + 100);
            displayStage.show();
            displayStage.setResizable(true);
            displayStage.setFullScreen(true);

            updateRoot = FXMLLoader.load(getClass().getResource("UpdateWindow.fxml"));
            primaryStage.setTitle("Sansum UC Wait Times");
            primaryStage.setScene(new Scene(updateRoot, 350, 536));
            primaryStage.show();
            primaryStage.setResizable(false);
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("No external monitor attached");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: No Monitor");
            alert.setHeaderText("There is no display board monitor connected to this computer.");
            alert.setContentText("Please connect an external monitor and relaunch the application.");
            alert.showAndWait();


            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }

        }

    }

    public static void main(String[] args) {
        Backendless.initApp("DAF8322E-10F7-BAE2-FF71-CFC61A439900", "4C0FE2F2-CE43-FD58-FF5C-A108BFE42B00", "v1");
        launch(args);
    }
}

