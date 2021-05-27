package src.main;

import com.tello.connection.*;

// Extend Connection class, implement clean shutdown sequence

public class Control extends Connection{
    public static final String host = "192.168.10.1";
    public static final int port = 8889;

    public Control(){
        super(host,port);
        this.enterSDKMode();
    }

    public Control(String host, int port){
        super(host,port);
        this.enterSDKMode();
    }

    public void enterSDKMode(){
        super.sendCommand("command");
    }

    public void takeoff(){
        super.sendCommand("takeoff");        
    }

    public void land() {
        super.sendCommand("land");
    }

    public void emergency(){
        super.sendCommand("emergency");
    }

    public void turn(){
        super.sendCommand("cw 360");
    }
    public void forward( int x){
        //super.sendCommand("forward "+x);
        super.sendCommand("rc 0 90 0 0");
    }
    public void backwards( int x){
        //super.sendCommand("back "+x);
        super.sendCommand("rc 0 -90 0 0");
    }
    public void left(){
        super.sendCommand("rc 90 0 0 0");
    }
    public void right(){
        super.sendCommand("rc -90 0 0 0");
    }
    public void down(){
        super.sendCommand("rc 0 0 -50 0");
    }
    public void up(){
        super.sendCommand("rc 0 0 50 0");
    }
    public void hover(){
        super.sendCommand("rc 0 0 0 0 ");
    }
    public void disablePad(){
        super.sendCommand("moff");
    }
    public String registeredActions(String input){
        String actionName = "None given";
        switch(input){
            case "F":emergency();actionName = "emergency";break;
            case "T":takeoff();actionName = "takeoff";break;
            case "R": land();actionName = "land";break;
            //case "F": turn();actionName = "turn";break;
            case "W": forward(20);actionName = "forward";break;
            case "S":backwards(20);actionName = "backwards";break;
            case "D":left();actionName = "leftwards";break;
            case "A":right();actionName = "rightwards";break;
            case "X":hover();actionName = "hover";break;
            case "E":up();actionName  = "up";break;
            case "Q":down();actionName = "down";break;
            default: actionName = "None Taken, Key not bound";break;
        }
    return actionName;
    }
    /*
    public static void main(String args[]) throws InterruptedException{
        Control test= new Control(host,port);

        test.takeoff();
        Thread.sleep(1000);
        test.sendAndReceiveCommand("cw 360");
       
        //Thread.sleep(2000);
        test.emergency();

    }
    */

}
