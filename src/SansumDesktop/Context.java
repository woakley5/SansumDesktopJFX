package SansumDesktop;/*
 * Created by woakley on 8/17/17.
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private String time = "-";

    public String getTime() {
        return time;
    }

    public void setTime(String t){
        System.out.println("Time update to: " + t);
        time = t;
    }

    private String[] messages = {};
    private int messageNumber = 0;

    public void increaseMessageNumber(){
        messageNumber++;
    }

    public void resetMessageNumber(){
        messageNumber = 0;
    }

    public int getMessageNumber(){
        return messageNumber;
    }

    public int getMessagesLength(){
        return messages.length;
    }

    public String getMessageAtIndex(int i){
        return messages[i];
    }

    public void updateMessages(String m){
        if(m.contains("\n")) {
            messageNumber = 0;
            messages = m.split("\n");
        }
        else{
            Arrays.fill(messages, null);
            String[] a = {m};
            messages = a;
        }

        for(int x = 0; x < messages.length; x++){
            System.out.println(messages[x]);
        }
        System.out.println(messageNumber);

    }

}
