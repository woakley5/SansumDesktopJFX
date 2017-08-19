package SansumDesktop;/*
 * Created by woakley on 8/7/17.
 */

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class DisplayWindow {

    public Label timeLabel;
    public Label messageLabel;

   public DisplayWindow(){
       Timer updateTimer = new Timer();
       TimerTask updateTask = new TimerTask() {
           @Override
           public void run() {
               updateTime();
           }
       };
       updateTimer.schedule(updateTask, 1000, 3000);

       Timer messageTimer = new Timer();
       TimerTask messageTask = new TimerTask() {
           @Override
           public void run() {
               updateMessage();
           }
       };
       messageTimer.schedule(messageTask, 1000, 10000);

       Platform.runLater(
               () -> {
                   messageLabel.setText("");
               }
       );
    }

    public void updateTime() {
        if (!Context.getInstance().getTime().equals(timeLabel.getText())) {
            Platform.runLater(
                    () -> {
                        FadeTransition ft = new FadeTransition(Duration.millis(1000), timeLabel);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        ft.setCycleCount(1);
                        ft.setAutoReverse(false);
                        ft.play();
                        ft.setOnFinished(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                timeLabel.setText(Context.getInstance().getTime());
                                FadeTransition ft2 = new FadeTransition(Duration.millis(1000), timeLabel);
                                ft2.setFromValue(0.0);
                                ft2.setToValue(1.0);
                                ft2.setCycleCount(1);
                                ft2.setAutoReverse(false);
                                ft2.play();
                            }
                        });
                    }
            );
        }
    }

    public void updateMessage(){
        Platform.runLater(
                () -> {
                    if(Context.getInstance().getMessageNumber() < Context.getInstance().getMessagesLength()){
                        TranslateTransition moveOut = new TranslateTransition(Duration.millis(1000), messageLabel);
                        moveOut.setToY(50);
                        moveOut.play();
                        moveOut.setOnFinished(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println(Context.getInstance().getMessageNumber());
                                messageLabel.setText(Context.getInstance().getMessageAtIndex(Context.getInstance().getMessageNumber()));
                                Context.getInstance().increaseMessageNumber();
                                TranslateTransition moveIn = new TranslateTransition(Duration.millis(1000), messageLabel);
                                moveIn.setToY(0);
                                moveIn.play();
                            }
                        });
                    }
                    else{
                        Context.getInstance().resetMessageNumber();
                    }
                }
        );

    }

}
