package src.main;

public class Main {
   
    public static  void main(String[] args) throws InterruptedException {
        // Creates the GUI (keys) object and calls it
        Keys  control = new Keys("Tello Control");
        control.startDrone();
        control.createAndShowGui();
    }

   
}
