package SansumDesktop;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class UpdateWindow {

    public Label currentTimeLabel;
    public TextField timeTextField;
    public TextArea messagesField;
    private final static Preferences prefs = Preferences.userNodeForPackage(Main.class);
    public Button updateButton;
    public String messageString;
    public ArrayList<String> messageIDs = new ArrayList<>();

    public UpdateWindow(){
        setTimeLabelText("Current Time: -");
        refreshTime(null);
        Platform.runLater(
                () -> {
                    getMessages();
                }
        );
    }

    public void updateTime(ActionEvent actionEvent) {
        updateButton.setDisable(true);
        HashMap dataPoint = new HashMap();
        try {
            dataPoint.put("Time", Integer.parseInt(timeTextField.getText()));

            Backendless.Persistence.of("Times").save(dataPoint, new AsyncCallback<Map>() {
                public void handleResponse(Map response) {
                    showAlert("Database Update", "Success!", "The wait time has successfully been updated to " + timeTextField.getText(), Alert.AlertType.INFORMATION);
                    System.out.println("Update was successful");
                    timeTextField.setText("");
                    refreshTime(null);
                    updateButton.setDisable(false);
                }

                public void handleFault(BackendlessFault fault) {
                    showAlert("Database Error", "Error Updating Time", "There was a problem updating the time in the database. Please relaunch and try again. \n \n" + fault, Alert.AlertType.ERROR);

                    System.out.println("Error: " + fault);
                    updateButton.setDisable(false);
                }
            });

            Backendless.Persistence.of( "Times" ).findFirst( new AsyncCallback<Map>(){
                @Override
                public void handleResponse( Map time )
                {
                    System.out.println(time);
                    Backendless.Data.of( "Times" ).remove(time, new AsyncCallback<Long>() {
                        @Override
                        public void handleResponse(Long aLong) {
                            System.out.println("Successfully deleted old time");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            System.out.println("Error deleting old time " + backendlessFault);
                        }
                    });

                }
                @Override
                public void handleFault( BackendlessFault fault )
                {
                    System.out.println("Error finding oldest time");
                }
            });
        }
        catch(Exception e){
            showAlert("Invalid Input", timeTextField.getText() + " is not a valid number.", "Please input a valid integer.", Alert.AlertType.ERROR);
            timeTextField.setText("");
            updateButton.setDisable(false);
        }
    }

    public void refreshTime(ActionEvent actionEvent){

        Backendless.Persistence.of("Times").findLast( new AsyncCallback<Map>(){
            @Override
            public void handleResponse( Map result )
            {
                String time = result.get("Time").toString();
                setTimeLabelText("Current Time: " + time);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                setTimeLabelText("Error");
                showAlert("Database Error", "Error Refreshing Time", "There was a problem fetching the time from the database. Please relaunch and try again.", Alert.AlertType.ERROR);
                System.out.print("Error: " + fault);
            }
        });

        getMessages();

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
        String[] messages = messagesField.getText().split("\n");
        messageString = messagesField.getText();

        HashMap dbMessages = new HashMap();

        for(int x = 0; x < messageIDs.size(); x++){
            dbMessages.put("objectId", messageIDs.get(x));
            Backendless.Persistence.of("Messages").remove(dbMessages);
            dbMessages.clear();
        }
        System.out.println("Removed all messages");


        HashMap newMessage = new HashMap();

        for(int x = 0; x < messages.length; x++){
            newMessage.put("Message", messages[x]);
            Backendless.Persistence.of("Messages").save(newMessage);
            newMessage.clear();
        }
        System.out.println("Saved New messages");
        getMessages();

        showAlert("Message Display", "The following messages will display on the wait board: ", messageString, Alert.AlertType.INFORMATION);

    }

    public void getMessages(){
        Backendless.Persistence.of( "Messages" ).find( new AsyncCallback<List<Map>>(){
            @Override
            public void handleResponse(List<Map> maps) {
                messageString = "";
                messageIDs.clear();
                for(int x = 0; x < maps.size(); x++){
                    messageIDs.add(maps.get(x).get("objectId").toString());
                    messageString += maps.get(x).get("Message") + "\n";
                }
                messagesField.setText(messageString);
                System.out.println(messageIDs);
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                System.out.println("Error finding oldest time");
            }
        });
    }

}
