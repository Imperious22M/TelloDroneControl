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

    public String sendAndReceive(Inet4Address address, String msg) throws Exception{
        //byte[] buf = new byte[1024];
        final DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length , address, this.port)
        //final DatagramPacket receivPacket = new DatagramPacket(buf, buf.length);
        String ret = "";

        udpSocket.send(sendPacket);

        ret = this.listen();

    }

    public static void main(String[] args) throws Exception{
        UdpServer ser = new UdpServer(8889);
        
        // test send and recieve of tello battery message (send and receive, amke sure ip and port is correct)        

        while(true){
            System.out.println(ser.listen());
        }
    }


}
