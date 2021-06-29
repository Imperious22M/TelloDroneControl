package src.main;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.stream.Stream;

import javax.swing.border.Border;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

//This class is the JFrame window, everything graphical gets added to it
public class Keys extends JFrame implements KeyListener{ // class for GUI of control center

    static final String newline = System.getProperty("line.separator");
    private JTextArea displayArea;
    private JTextArea telemetryArea;
    private DroneControl droneAction;
    private JComboBox jcombo_controllerList;
    private final int jFrame_default_width = 1200;
    private final int JFrame_default_height = 800;    
    private JoystickControl joystickControl = new JoystickControl();
    private DroneMode droneMode;
    private DroneTelemetry droneTelemetry;
    //private boolean drone_Control_Active = false;

    public Keys(){
        super("Drone Control Default Name");
        setMinimumSize(new Dimension(jFrame_default_width,JFrame_default_height));
        setFocusable(true);
    }

    public Keys(String name){
        super(name);
        setMinimumSize(new Dimension(jFrame_default_width,JFrame_default_height));
        setFocusable(true);
    }

    public void startDrone(){
        droneAction = new DroneControl();
        droneAction.disablePad();
    }

    public void createAndShowGui(){
        this.addComponentstoPage();
        //this.addKeyListener();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void addComponentstoPage(){   
            // Top Layer
        JPanel topLayer_pnl = new JPanel();
        topLayer_pnl.setLayout(new FlowLayout(FlowLayout.CENTER,20,10)); // 20 pixel horizontal spacing and 10 vertical
        jcombo_controllerList = new JComboBox();
        JButton controller_search_butt = new JButton("Look for Controllers");
        JButton controller_start_butt = new JButton("Start Controller Mode");
        JButton update_speed_butt = new JButton("Update Speed");
        JLabel speedLabel  = new JLabel("Speed(min 10, max 100): ");
        JTextField speedTextField = new JTextField("10");
        speedTextField.setPreferredSize(new Dimension(40,30));
        controller_search_butt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                // Controller Lookup button action
                fillControllerList();
                System.out.println("looking for controller");
            }
        });
        controller_start_butt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                if(droneMode== null && droneTelemetry==null){
                    droneMode = new DroneMode(getSelectedControllerName(), joystickControl, displayArea, droneAction);
                    droneTelemetry = new DroneTelemetry(telemetryArea);
                    // this one just works, may(likely) have memory access problem!....
                    Thread t1 = new Thread(droneMode);
                    Thread t2 = new Thread(droneTelemetry);
                    t1.start();
                    t2.start();
                    }else{
                    droneMode.stop();
                    droneTelemetry.stop();
                    droneMode = null;
                    droneTelemetry = null;
                }
            }
        });
        update_speed_butt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                try{
                    int speed  = Integer.parseInt(speedTextField.getText());
                    if(speed<=100 && speed>=10){
                        String toDisplay = droneAction.setSpeed(Integer.parseInt(speedTextField.getText()));
                        //Be careful with memory acess errors with DroneMode Thread
                        sendtoDisplayArea("Command sent to drone: "+ toDisplay);
                    }else{
                        String toDisplay = (speed>100)?"Your speed can't be greater than 100":"Your speed can't be less than 10";
                        sendtoDisplayArea(toDisplay);
                    }
                 }catch(NumberFormatException err){
                    sendtoDisplayArea("Not a number given, speed will be set to 10");
                    String toDisplay = droneAction.setSpeed(10);
                    speedTextField.setText("10");
                        //Be careful with memory acess errors with DroneMode Thread
                    sendtoDisplayArea("Command sent to drone: "+ toDisplay);
                 }catch(Exception err){
                    err.printStackTrace();
                    sendtoDisplayArea("Unknown error on Speed label occured, speed unchanged");
                 }
            }
        });
        topLayer_pnl.add(jcombo_controllerList);
        topLayer_pnl.add(controller_search_butt);
        topLayer_pnl.add(controller_start_butt);
        topLayer_pnl.add(speedLabel);
        topLayer_pnl.add(speedTextField);
        topLayer_pnl.add(update_speed_butt);
        
        //*********************
        // WORK ON DEPRECATING KEYBOARD CONTROL
       JPanel middleLayer_pnl = new JPanel();
       middleLayer_pnl.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        displayArea = new JTextArea();
        displayArea.addKeyListener(this); // Let the the whole (display area) window be the Key listener for keyboard control
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea); // male the display and control area a scroll plane
        displayScrollPane.setPreferredSize(new Dimension(375,500));
        telemetryArea = new JTextArea();
        telemetryArea.setEditable(false);
        JScrollPane telemetryScrollPane = new JScrollPane(telemetryArea);
        telemetryScrollPane.setPreferredSize(new Dimension(375,500));
        middleLayer_pnl.add(displayScrollPane);
        middleLayer_pnl.add(telemetryScrollPane);
        
        //********************* 

        JPanel bottomLayer_pnl = new JPanel();
        bottomLayer_pnl.setLayout(new GridLayout(2,3)); // 2 rows 3 columns
        JButton clearDisplayButton = new JButton("Clear Display Area");
        JButton clearTelemetryButton = new JButton("Clear Telemetry Area");
        JButton startTelloButton = new JButton("Start Tello");
        JButton emergencyTelloButton = new JButton("Emergency Tello");
        JButton landTelloButton = new JButton("Land Tello");
        JButton shutdownTelloButton = new JButton("Stream On (Temp)");
        clearDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                displayArea.setText("");
                displayArea.requestFocusInWindow(); // return focus to window
            }
        });
        clearTelemetryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                telemetryArea.setText("");
                displayArea.requestFocusInWindow();
            }
        });
        startTelloButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                String toDisplay  = droneAction.enterSDKMode();
                //Be careful with memory acess errors with DroneMode Thread
                sendtoDisplayArea("Command sent to drone: "+ toDisplay);
                droneAction.disablePad();
            }
        });
        emergencyTelloButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                String toDisplay = droneAction.emergency();     
                //Be careful with memory acess errors with DroneMode Thread
                sendtoDisplayArea("Command sent to drone: "+ toDisplay);
            }
        });
        landTelloButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                String toDisplay = droneAction.land();
                //Be careful with memory acess errors with DroneMode Thread
                sendtoDisplayArea("Command sent to drone: "+ toDisplay);
            }
        });
        shutdownTelloButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                String toDisplay = droneAction.streamOn();
                //Be careful with memory acess errors with DroneMode Thread
                sendtoDisplayArea("Command sent to drone: "+ toDisplay);
            }
        });
        bottomLayer_pnl.add(clearDisplayButton);
        bottomLayer_pnl.add(clearTelemetryButton);
        bottomLayer_pnl.add(startTelloButton);
        bottomLayer_pnl.add(emergencyTelloButton);
        bottomLayer_pnl.add(landTelloButton);
        bottomLayer_pnl.add(shutdownTelloButton);

        this.setLayout(new BorderLayout());
        this.add(topLayer_pnl,BorderLayout.PAGE_START);
        this.getContentPane().add(middleLayer_pnl,BorderLayout.CENTER);
        this.getContentPane().add(bottomLayer_pnl,BorderLayout.PAGE_END);
    }

        
    public void fillControllerList(){

        jcombo_controllerList.removeAllItems();
        ArrayList<String> controllerList = joystickControl.searchForControllers();
        if(controllerList.isEmpty()){
            addControllerName("No controller Found");
            return;
        }
        for(String controller:controllerList){
            addControllerName(controller);
        }
    }
    
     public int getSelectedControllerName(){
        return jcombo_controllerList.getSelectedIndex();
    }
    
    public void addControllerName(String controllerName){
        jcombo_controllerList.addItem(controllerName);
    }
    
    public void showControllerDisconnected(){
        jcombo_controllerList.removeAllItems();
        jcombo_controllerList.addItem("Controller disconnected!");
    }
    

    @Override // maybe not needed?
    public void setFocusable(boolean b){
        super.setFocusable(b);
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        //displayInfo(e, "KEY TYPED: ");
        droneActionCall(e);
    
    }
     
    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        //displayInfo(e, "KEY PRESSED: ");
        droneActionCall(e);
    }
     
    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        //displayInfo(e, "KEY RELEASED: ");
        droneActionCall(e);
    }

    private void displayInfo(KeyEvent e, String keyStatus){
         
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }
         
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }
         
        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }
         
        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        
        displayArea.append(keyStatus + newline
        + "    " + keyString + newline
        + "    " + modString + newline
        + "    " + actionString + newline
        + "    " + locationString + newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength()); // scroll down window automatically

         StringBuilder out = new StringBuilder("");
        out.append(keyStatus + newline
                + "    " + keyString + newline
                + "    " + modString + newline
                + "    " + actionString + newline
                + "    " + locationString + newline);
        System.out.println(out);
    }

    private void sendtoDisplayArea(String str){
        displayArea.append(str+ newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength()); // scroll down window automatically
    }
    
    private void droneActionCall(KeyEvent e){
        int id = e.getID();
        String keyString = "";
        String actionName = "";
        if (id == KeyEvent.KEY_PRESSED) {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
            try{
                actionName = droneAction.registeredActions(KeyEvent.getKeyText(keyCode));
            }catch(NullPointerException error){
                keyString += "Error Starting drone, please check drone connection";
            }catch(Exception error){
                error.printStackTrace();
            }
        } 

        displayArea.append(actionName+
        "    " + keyString + newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength()); // scroll down window automatically


    }

}

   