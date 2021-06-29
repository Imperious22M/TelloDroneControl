package src.main;

import com.tello.connection.*;

// Extend Connection class, implement clean shutdown sequence

public class DroneControl extends Connection{
    public static final String host = "192.168.10.1";
    public static final int port = 8889;

    public DroneControl(){
        super(host,port);
        this.enterSDKMode();
    }

    public DroneControl(String host, int port){
        super(host,port);
        this.enterSDKMode();
    }


    public String enterSDKMode(){
        super.sendCommand("command");
        return "command";
    }

    public String takeoff(){
        super.sendCommand("takeoff");        
        return "takeoff";
    }   

    public String land() {
        super.sendCommand("land");
        return "land";
    }

    public String emergency(){
        super.sendCommand("emergency");
        return "emergency";
    }

    public String fullTurn(){ // Kinda not works?
        super.sendCommand("cw 360");
        return "cw 360";
    }
    public String forward(){
        super.sendCommand("rc 0 90 0 0");
        return "rc 0 90 0 0";
    }
    public String backwards(){
        super.sendCommand("rc 0 -90 0 0");
        return "rc 0 -90 0 0";
    }
    public String left(){
        super.sendCommand("rc 90 0 0 0");
        return "rc 90 0 0 0";
    }
    public String right(){
        super.sendCommand("rc -90 0 0 0");
        return "rc -90 0 0 0";
    }
    public String down(){
        super.sendCommand("rc 0 0 -50 0");
        return "rc 0 0 -50 0";
    }
    public String up(){
        super.sendCommand("rc 0 0 50 0");
        return "rc 0 0 50 0";
    }
    public String hover(){
        super.sendCommand("rc 0 0 0 0 ");
        return  "rc  0 0 0 0";
    }
    public String disablePad(){
        super.sendCommand("moff");
        return "moff";
    }

    public String streamOn(){
        super.sendCommand("streamon");
        return "streamon";
    }

    public String setSpeed(int x){
        String command = "speed "+x;
        super.sendCommand(command);
        //System.out.println(super.sendAndReceiveCommand(command));
        return command;
    }

    public String manualRCControll(int VertAxis, int LatAxis ,int Altitude, int LongAxis){
        String command  = "rc "+ VertAxis + " " + LatAxis + " " + Altitude + " " +(LongAxis);
        // LongAxis = left/right(Horizontal turn), LatAxis = forward/back, Altitude  = up/down, VetAxis = yaw(vertical turn)
        super.sendCommand(command); 
        return command;
    }

    public String registeredActions(String input){ // for keyboard control
        String actionName = "None given";
        switch(input){
            case "F":emergency();actionName = "emergency";break;
            case "T":takeoff();actionName = "takeoff";break;
            case "R": land();actionName = "land";break;
            //case "F": turn();actionName = "turn";break;
            case "W": forward();actionName = "forward";break;
            case "S":backwards();actionName = "backwards";break;
            case "D":left();actionName = "leftwards";break;
            case "A":right();actionName = "rightwards";break;
            case "X":hover();actionName = "hover";break;
            case "E":up();actionName  = "up";break;
            case "Q":down();actionName = "down";break;
            default: actionName = "None Taken, Key not bound";break;
        }
    return actionName;
    }


}
