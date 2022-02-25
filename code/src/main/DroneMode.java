package src.main;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.Enumeration;
import javax.swing.JTextArea;


public class DroneMode implements Runnable{
    
    private AtomicBoolean isActive;
    private int selectedControllerIndex;
    private JoystickControl joystickControl;
    private JTextArea displayArea;
    private DroneControl droneAction;
    static final String newline = System.getProperty("line.separator");

    public DroneMode(int index, JoystickControl currenInstance, JTextArea display, DroneControl droneActionIN){
        isActive = new AtomicBoolean(true);
        selectedControllerIndex  = index;
        joystickControl = currenInstance;
        displayArea = display;
        droneAction = droneActionIN;
    }

    public void stop(){
        isActive.set(false);
    }

    @Override
    public void run(){
        while(isActive.get()){
            Dictionary<String,String> valueDictionary = joystickControl.getPollSelectedController(selectedControllerIndex);
            System.out.println(valueDictionary.toString());
            //sendtoDisplayArea(valueDictionary.toString());
              /*
            Enumeration<String> enu = valueDictionary.keys();
            while(enu.hasMoreElements()){
                System.out.println(enu.nextElement());
            }
            */

            //Main control logic of joysticks
            int LongAxis, LatAxis, Altitude, VertAxis;
            LongAxis = Integer.parseInt(valueDictionary.get("X Rotation"));
            LatAxis = Integer.parseInt(valueDictionary.get("Y Axis"));
            Altitude = Integer.parseInt(valueDictionary.get("Y Rotation"));
            VertAxis = (-1)*Integer.parseInt(valueDictionary.get("X Axis")); // Negative one is needed for proper H Left/Right Movement
            int Xbutt = Integer.parseInt(valueDictionary.get("Button 2"));
            int Abutt = Integer.parseInt(valueDictionary.get("Button 0"));
            int Bbutt = Integer.parseInt(valueDictionary.get("Button 1"));
            if(Bbutt==1){//Button Actions
                droneAction.land();
            }
            if(Xbutt ==1){
                droneAction.takeoff();
            }
            if(Abutt==1){
                droneAction.emergency();
            }

            //Function to smooth drone movements at low Joystick value
            LongAxis = (LongAxis<9&&LongAxis>-9)? 0:LongAxis;
            LatAxis = (LatAxis<9&&LatAxis>-9)? 0:LatAxis;
            Altitude = (Altitude<9&&Altitude>-9)? 0:Altitude;
            VertAxis = (VertAxis<9&&VertAxis>-9)? 0:VertAxis;

            //String comm = droneAction.manualRCControll(LongAxis, LatAxis, Altitude, VertAxis);
            String comm = droneAction.manualRCControll(VertAxis, LatAxis, Altitude, LongAxis);
            System.out.println(comm);
            sendtoDisplayArea(comm);

             // We have to give processor some rest.
             try {
                Thread.sleep(25); 
            } catch (InterruptedException ex) {
                System.out.println("Error in poll Selected Interrupt");
            }
        }
    }

    public void sendtoDisplayArea(String str){
        displayArea.append(str+ newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength()); // scroll down window automatically
    }


}
