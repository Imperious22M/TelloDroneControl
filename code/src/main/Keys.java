package src.main;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.lang.StringBuilder;
import java.util.ArrayList;

import javax.swing.border.Border;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

//This class is the JFrame window, everything graphical gets added to it
public class Keys extends JFrame implements KeyListener{ // class for GUI of control center

    static final String newline = System.getProperty("line.separator");
    private JTextArea displayArea;
    private JTextArea telemetryArea;
    //Actions droneAction; // class where all drone actions are defined
    private Control droneAction;
    private JComboBox jcombo_controllerList;
    private ArrayList<Controller> foundControllers;

    public Keys(){
        setFocusable(true);
    }

    public Keys(String name){
        super(name);
        setMinimumSize(new Dimension(500,600));
        foundControllers = new ArrayList<>();
        setFocusable(true);
    }

    public void startDrone(){
        //droneAction = new Actions();
        droneAction = new Control();
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
            // add jpanel
        JPanel topLayer_pnl = new JPanel();
        topLayer_pnl.setLayout(new FlowLayout(FlowLayout.CENTER,20,10)); // 20 pixel horizontal spacing and 10 vertical
        jcombo_controllerList = new JComboBox();
        topLayer_pnl.add(jcombo_controllerList);
        JButton controller_search_butt = new JButton("Look for Controllers");
        controller_search_butt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                searchForControllers();
                System.out.println("looking for controller");
            }
        });
        topLayer_pnl.add(controller_search_butt);
        
       JPanel middleLayer_pnl = new JPanel();
       middleLayer_pnl.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        displayArea = new JTextArea();
        displayArea.addKeyListener(this); // Let the the whole (this object) window be the Listener for the control/display area
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea); // male the display and control area a scroll plane
        displayScrollPane.setPreferredSize(new Dimension(375,325));
        telemetryArea = new JTextArea();
        telemetryArea.setEditable(false);
        JScrollPane telemetryScrollPane = new JScrollPane(telemetryArea);
        telemetryScrollPane.setPreferredSize(new Dimension(375,325));
        middleLayer_pnl.add(displayScrollPane);
        middleLayer_pnl.add(telemetryScrollPane);
        
        JPanel bottomLayer_pnl = new JPanel();
        bottomLayer_pnl.setLayout(new GridLayout(2,3));
        JButton clearDisplayButton = new JButton("Clear Display Area");
        JButton clearTelemetryButton = new JButton("Clear Telemetry Area");
        JButton startTelloButton = new JButton("Start Tello");
        JButton emergencyTelloButton = new JButton("Emergency Tello");
        JButton landTelloButton = new JButton("Land Tello");
        JButton shutdownTelloButton = new JButton("Shutdown Tello");
        clearDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e ){
                displayArea.setText("");
                displayArea.requestFocusInWindow(); // return focus to window
            }
        });
        //Add rest of listeners here

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

    public void searchForControllers(){
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];
            
            if (
                    controller.getType() == Controller.Type.STICK || 
                    controller.getType() == Controller.Type.GAMEPAD || 
                    controller.getType() == Controller.Type.WHEEL ||
                    controller.getType() == Controller.Type.FINGERSTICK
               )
            {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                
                // Add new controller to the list on the window.
                addControllerName(controller.getName() + " - " + controller.getType().toString() + " type");
                System.out.println(controller.getName() + " - " + controller.getType().toString() + " type");
            }
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

   