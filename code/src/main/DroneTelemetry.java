package src.main;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JTextArea;
//import src.main.UdpServer;

public class DroneTelemetry implements Runnable{

    private AtomicBoolean isActive;
    private UdpServer server;
    private JTextArea displayArea;
    static final String newline = System.getProperty("line.separator");

    public DroneTelemetry(JTextArea telemetry){
    isActive = new AtomicBoolean(true);
    displayArea = telemetry;
        try{
            server = new UdpServer();    
        }catch(Exception e){
            sendtoDisplayArea(e.getStackTrace().toString());
            this.stop();
        }
    }

    public void stop(){
        isActive.set(false);
    }

    @Override
    public void run(){
        String msg  = "";

        while(isActive.get()){
            try{
                msg = server.listen();
                sendtoDisplayArea(msg);
            }catch(Exception e){
                sendtoDisplayArea(e.getStackTrace().toString());
                this.stop();
            }

        }
    }

    public void sendtoDisplayArea(String str){
        displayArea.append(str+ newline);
        displayArea.setCaretPosition(displayArea.getDocument().getLength()); // scroll down window automatically
    }

}
