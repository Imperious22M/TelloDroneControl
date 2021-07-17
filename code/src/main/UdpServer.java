package src.main;

import com.tello.logger.Logger;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UdpServer {
    private final String host; // The Tello
    private final int hostPort;
    private final int clientPort;
    private InetAddress address;
    private DatagramSocket socket;

    public UdpServer(){ //Default socket set for listening to Tello telemetry
        this.host = "192.168.10.1";
        this.clientPort = 8890;
        this.hostPort = 0000;

        try{
            this.address = InetAddress.getByName(host);
            this.socket = new DatagramSocket(clientPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public UdpServer(String host, int hostPort, int clientPort) {
        this.host = host;
        this.hostPort = hostPort;
        this.clientPort = clientPort;

        try {
            this.address = InetAddress.getByName(host);
            this.socket = new DatagramSocket(clientPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.connect();
    }

    /**
     * Close connection
     * THIS METHODS NEEDS TO BE CALLED WHEN EXITING
     */
    public void close() {
        socket.close();
    }

    private void connect() {
        this.socket.connect(this.address, this.hostPort);
    }

    public String sendAndReceiveCommand(String command, int length) {
        this.sendCommand(command);
        return this.receiveMessage(length);
    }

    public String sendAndReceiveCommand(String command) {
        this.sendCommand(command);
        return this.receiveMessage();
    }

    public boolean confirmationCommand(String command) {
        String response = sendAndReceiveCommand(command);
        System.out.println(response.length());
        if(response.equalsIgnoreCase("ok")) {
            return true;
        }
        return false;
    }

    public void sendCommand(String command) {
        if(this.socket.isConnected()) {
            if(command != null) {
                if(command.length() > 0) {
                    final byte[] data = command.getBytes();
                    final DatagramPacket packet = new DatagramPacket(data, data.length, address, hostPort);
                    try {
                        Logger.INSTANCE.information("Sending tello command: " + command);
                        socket.send(packet);
                    } catch (IOException e) {
                        Logger.INSTANCE.error("Error when sending packet" + host + ":" + hostPort, e);
                    }
                }else {
                    Logger.INSTANCE.error("Command is empty!");
                }
            }else {
                Logger.INSTANCE.error("Command is null!");
            }
        }else {
            Logger.INSTANCE.error("UDP Socket is not connected! " + host + ":" + hostPort);
        }
    }

    public String receiveMessage() {
        return this.receiveMessage(1024);
    }

    public String receiveMessage(int length) {
        byte[] receiveData = new byte[1024];
        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            Logger.INSTANCE.information("Waiting for response... Length: " + length);
            socket.receive(receivePacket);
            return trimExecutionResponse(receiveData, receivePacket);
        } catch (IOException e) {
            Logger.INSTANCE.error("Error when receiving packet" + host + ":" + hostPort, e);
        }
        return null;
    }

    private String trimExecutionResponse(byte[] response, DatagramPacket receivePacket) {
        response = Arrays.copyOf(response, receivePacket.getLength());
        return new String(response, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception{
        UdpServer ser = new UdpServer("192.168.10.1",8889,62704); // port working was 62704
        // test send and recieve of tello battery message (send and receive, make sure ip and port is correct)        
        System.out.println(InetAddress.getAllByName("10.0.0.1")[0].toString());
       
        System.out.println( ser.sendAndReceiveCommand("command"));
        System.out.println( ser.sendAndReceiveCommand("speed 100"));
        System.out.println( ser.sendAndReceiveCommand("battery?"));
        System.out.println( ser.sendAndReceiveCommand("sdk?"));
       System.out.println( ser.sendAndReceiveCommand("time?"));
       System.out.println( ser.sendAndReceiveCommand("wifi?"));
       System.out.println( ser.sendAndReceiveCommand("speed?"));
       
       /*
        while(true){
            System.out.println(ser.listen());
        }
        */
    }



}
