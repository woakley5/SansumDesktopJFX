package SansumDesktop;

import com.backendless.Backendless;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class Main extends Application {

    public static Parent displayRoot;
    public static Parent updateRoot;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Stage displayStage = new Stage();
        displayRoot = FXMLLoader.load(getClass().getResource("DisplayWindow.fxml"));
        displayStage.setTitle("Waiting Room Display");
        displayStage.setScene(new Scene(displayRoot, 600, 420));
        displayStage.show();
        displayStage.setResizable(true);

        updateRoot = FXMLLoader.load(getClass().getResource("UpdateWindow.fxml"));
        primaryStage.setTitle("Sansum UC Wait Times");
        primaryStage.setScene(new Scene(updateRoot, 350, 536));
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    public static void main(String[] args) {
        Backendless.initApp("DAF8322E-10F7-BAE2-FF71-CFC61A439900", "4C0FE2F2-CE43-FD58-FF5C-A108BFE42B00", "v1");
        launch(args);
    }
}

