package SansumDesktop;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class UpdateWindow {

    public Label currentTimeLabel;
    public TextField timeTextField;
    public TextArea messagesField;
    private final static Preferences prefs = Preferences.userNodeForPackage(Main.class);


    public UpdateWindow(){
        setTimeLabelText("Current Time: -");
        refreshTime(null);
        Platform.runLater(
                () -> {
                    messagesField.setText(prefs.get("MESSAGES_STRING", ""));
                    updateMessages(null);
                }
        );
    }

    public void updateTime(ActionEvent actionEvent) {
        HashMap dataPoint = new HashMap();
        try {
            dataPoint.put("Time", Integer.parseInt(timeTextField.getText()));
            Context.getInstance().setTime(timeTextField.getText());

            Backendless.Persistence.of("Times").save(dataPoint, new AsyncCallback<Map>() {
                public void handleResponse(Map response) {
                    showAlert("Database Update", "Success!", "The wait time has successfully been updated to " + timeTextField.getText(), Alert.AlertType.INFORMATION);
                    System.out.println("Update was successful");
                    timeTextField.setText("");
                    refreshTime(null);
                }

                public void handleFault(BackendlessFault fault) {
                    showAlert("Database Error", "Error Updating Time", "There was a problem updating the time in the database. Please relaunch and try again.", Alert.AlertType.ERROR);

                    System.out.println("Error: " + fault);
                }
            });
        }
        catch(Exception e){
            showAlert("Invalid Input", timeTextField.getText() + " is not a valid number.", "Please input a valid integer.", Alert.AlertType.ERROR);
            timeTextField.setText("");
        }
    }

    public void refreshTime(ActionEvent actionEvent){

        Backendless.Persistence.of( "Times" ).findLast( new AsyncCallback<Map>(){
            @Override
            public void handleResponse( Map result )
            {
                String time = result.get("Time").toString();
                setTimeLabelText("Current Time: " + time);
                Context.getInstance().setTime(time);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                setTimeLabelText("Error");
                showAlert("Database Error", "Error Refreshing Time", "There was a problem fetching the time from the database. Please relaunch and try again.", Alert.AlertType.ERROR);
                System.out.print("Error: " + fault);
            }
        });

    }

    void setTimeLabelText(String text){
        Platform.runLater(
                () -> {
                    currentTimeLabel.setText(text);
                }
        );
    }

    void showAlert(String title, String headerText, String contentText, Alert.AlertType type){
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(type);
                    alert.setTitle(title);
                    alert.setHeaderText(headerText);
                    alert.setContentText(contentText);
                    alert.showAndWait();
                }
        );
    }

    public void updateMessages(ActionEvent e){
        Context.getInstance().updateMessages(messagesField.getText());
        String messages = "";
        for(int x = 0; x < Context.getInstance().getMessagesLength(); x++){
            messages += Context.getInstance().getMessageAtIndex(x);
            messages += "\n";
        }
        if(!messages.equals("")){
            showAlert("Message Display", "The following messages will display on the wait board:", messages, Alert.AlertType.INFORMATION);
        }
    }

}
