package src.main;

import java.net.*;

import java .io.IOException;

public class UdpServer {
    private int port;
    private DatagramSocket udpSocket;

    public UdpServer() throws SocketException,IOException{
        this.port = 8890;
        udpSocket = new DatagramSocket(this.port);
    }
    public UdpServer(int port) throws SocketException,IOException{
        this.port = port;
        udpSocket  = new DatagramSocket(this.port);
    }

    public String listen() throws Exception{
        byte[] buf = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String retMsg  = "";

        udpSocket.receive(packet);
        
        retMsg = new String(packet.getData());

        return retMsg;
    }

    public String sendAndReceive(InetAddress address, String msg) throws Exception{
        //byte[] buf = new byte[1024];
        final DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length , address, 8889);
        //final DatagramPacket receivPacket = new DatagramPacket(buf, buf.length);
        String ret = "";

        udpSocket.send(sendPacket);

        ret = this.listen();
        return ret;
    }

    public static void main(String[] args) throws Exception{
        UdpServer ser = new UdpServer(62704); // port working was 62704
        // test send and recieve of tello battery message (send and receive, make sure ip and port is correct)        
        System.out.println(InetAddress.getAllByName("10.0.0.1")[0].toString());
       
        System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "command"));
        System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "speed 100"));
        System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "battery?"));
        System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "sdk?"));
       System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "time?"));
       System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "wifi?"));
       System.out.println( ser.sendAndReceive(InetAddress.getAllByName("192.168.10.1")[0], "speed?"));
       
       /*
        while(true){
            System.out.println(ser.listen());
        }
        */
    }


}
