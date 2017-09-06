package SansumDesktop;

import com.backendless.Backendless;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    public static Parent displayRoot;
    public static Parent updateRoot;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Stage displayStage = new Stage();
        displayRoot = FXMLLoader.load(getClass().getResource("DisplayWindow.fxml"));
        displayStage.setTitle("Waiting Room Display");
        displayStage.setScene(new Scene(displayRoot, 1600, 1200));

        try { //tries to find external monitor

            Screen externalScreen = Screen.getScreens().get(1);

            Rectangle2D externalMonitorBounds = externalScreen.getVisualBounds();

            displayStage.setX(externalMonitorBounds.getMinX() + 100);
            displayStage.setY(externalMonitorBounds.getMinY() + 100);

            displayStage.show();
            displayStage.setFullScreen(true);

        }

        //catch throws if no monitor is detected
        catch(IndexOutOfBoundsException e) {
            System.out.println("No external monitor attached");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: No Monitor");
            alert.setHeaderText("There is no display board monitor connected to this computer.");
            alert.setContentText("The display board window will launch in windowed mode. Connect a monitor and relaunch to show the display board in fullscreen.");
            alert.showAndWait();

            displayStage.show();
            displayStage.setResizable(true);
        }

        //finally launches dashboard window
        updateRoot = FXMLLoader.load(getClass().getResource("UpdateWindow.fxml"));
        primaryStage.setTitle("Sansum UC Wait Times");
        primaryStage.setScene(new Scene(updateRoot, 350, 562));
        primaryStage.show();
        primaryStage.setResizable(false);

        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Application Closed by click to Close Button(X)");
                        PlatformImpl.tkExit();
                        System.exit(0);
                    }
                });
            }
        });

    }

    @Override
    public void stop(){
        PlatformImpl.tkExit();
        System.exit(0);
    }

    public static void main(String[] args) {
        Backendless.initApp("A1E62F8F-86D6-C2D9-FFAA-E16DFCFAFC00", "4C0FE2F2-CE43-FD58-FF5C-A108BFE42B00");
        launch(args);
    }
}

