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

