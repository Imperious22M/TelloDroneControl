package src.main;

public class Main {
   
    public static  void main(String[] args) throws InterruptedException {
        Keys  control = new Keys("Tello Control");
        control.startDrone();
        control.createAndShowGui();
    }

   
}
