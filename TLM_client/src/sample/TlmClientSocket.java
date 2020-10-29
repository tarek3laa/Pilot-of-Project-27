package sample;

import sample.packet.Header;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TlmClientSocket {
    private final String serverName;
    private final int serverPort;
    private Socket scsSocket;
    private DataInputStream reader;
    private PacketListener listener;

    public TlmClientSocket(String serverName, int serverPort, PacketListener listener) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.listener = listener;

    }


    protected boolean Connect() {
        try {
            this.scsSocket = new Socket(serverName, serverPort);
            reader = new DataInputStream(scsSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void receive() {
        new Thread(() -> {
            while (true) {

                byte[] header = new byte[6];
                try {
                    reader.readFully(header);
                    System.out.println("header");
                    Header header1 = new Header(header);
                    int length = Header.getIntVal(header1.getLength());

                    System.out.println();
                    byte[] data = new byte[length + 1];
                    reader.readFully(data);
                    System.out.println("data");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listener.onMessage(header1, data);

                } catch (IOException e) {

                }
            }
        }).start();
    }
}
