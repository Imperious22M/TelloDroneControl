package src.main;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.DirectAndRawInputEnvironmentPlugin;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class JoystickControl {
    
    private ArrayList<Controller> foundControllers;
    private boolean ControllersAvailable = false;

    public JoystickControl(){
        foundControllers = new ArrayList<>();
    }

    public Controller[] getRawControllerList(){
        DirectAndRawInputEnvironmentPlugin directEnv = new DirectAndRawInputEnvironmentPlugin();
        Controller[] controllers = directEnv.getControllers();
        return controllers;
    }

    public ArrayList<String> searchForControllers(){
        ArrayList<String> stringArray = new ArrayList<>();
        foundControllers = new ArrayList<>(); 
        Controller[] controllers = getRawControllerList();
        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];
            
            if (
                    controller.getType() == Controller.Type.STICK || 
                    controller.getType() == Controller.Type.GAMEPAD || 
                    controller.getType() == Controller.Type.WHEEL ||
                    controller.getType() == Controller.Type.FINGERSTICK
               )
            {
                // Add new controller to the array list of all controllers.
                foundControllers.add(controller);
                stringArray.add(controller.getName() + " - " + controller.getType().toString() + " type");
                
                System.out.println(controller.getName() + " - " + controller.getType().toString() + " type");
            }
        }
        if(!foundControllers.isEmpty()){
            ControllersAvailable = true;
        }
        System.out.print(foundControllers.toString());
        return stringArray;
    }

    public Dictionary<String, String> getPollSelectedController(int selectedControllerIndex){
        Dictionary<String, String> valueDictionary = new Hashtable<String,String>();
        
        if(ControllersAvailable==true)
        {
            // Currently selected controller
            Controller controller = foundControllers.get(selectedControllerIndex);
            String ControllerAction = "";
            // Poll controller for current data, and return empty if controller is disconneted
            if( !controller.poll() ){
                valueDictionary = new Hashtable<String, String>();
                return valueDictionary;
            }
            
            // X axis and Y axis
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;
            
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                Identifier componentIdentifier = component.getIdentifier();
                
               // Buttons
                //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    // Button index
                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();
                    if(isItPressed){
                        valueDictionary.put("Button "+buttonIndex, "1");
                    }else{
                        valueDictionary.put("Button "+buttonIndex, "0");
                    }
                        // We know that this component was button so we can skip to next component.
                    continue;
                }
                
                // Hat switch
                if(componentIdentifier == Component.Identifier.Axis.POV){
                    float hatSwitchPosition = component.getPollData();
                    valueDictionary.put("Hat", String.valueOf(hatSwitchPosition));
                    // We know that this component was hat switch so we can skip to next component.
                    continue;
                }
                
                // Axes
                if(component.isAnalog()){
                    float axisValue = component.getPollData();
                    int axisValueInPercentage = getAxisValueInPercentage(axisValue);
                    
                    // X axis
                    if(componentIdentifier == Component.Identifier.Axis.X){
                        xAxisPercentage = axisValueInPercentage;
                        valueDictionary.put("X Axis", String.valueOf(xAxisPercentage));
                        continue; // Go to next component.
                    }
                    // Y axis
                    if(componentIdentifier == Component.Identifier.Axis.Y){
                        yAxisPercentage = axisValueInPercentage;
                        valueDictionary.put("Y Axis", String.valueOf(yAxisPercentage));
                        continue; // Go to next component.
                    }

                    // All other Axis
                    valueDictionary.put(component.getName(), String.valueOf(axisValueInPercentage));
                    
                }
               
            }

            // end of component for loop
        }
        return valueDictionary;
    }

    // Represents the given axis as a percentage
    public int getAxisValueInPercentage(float axisValue)
    {
        //return (int)(((2 - (1 - axisValue)) * 100) / 2); // this one converts it from 0-100 with a center at 50 when x = 0
        return (int)(-1*axisValue*100); // returns a value 101<x<101
    }
    // Above three functions are not neccessary delete if not

}
