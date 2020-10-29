package scs;

import scs.packet.Packet;

import java.io.*;
import java.net.Socket;

public class ScsStream {

    private final String serverName;
    private final int serverPort;
    private Socket scsSocket;
    private OutputStream socketOutputStream;
    private DataInputStream reader;
    private PacketListener listener;

    public ScsStream(String serverName, int serverPort, PacketListener listener) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.listener = listener;

    }


    protected boolean connectWithDRTF() {
        try {
            this.scsSocket = new Socket(serverName, serverPort);
            this.socketOutputStream = scsSocket.getOutputStream();

            reader = new DataInputStream(scsSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean connectWithServer() {

        try {
            Socket socket = new Socket("localhost", 9090);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            new Thread(() -> {
                while (true) {
                    byte[] sererACK = new byte[7];
                    try {
                        inputStream.readFully(sererACK);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listener.onServerACK(sererACK);
                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }

    protected void send(Packet packet) throws IOException {
        socketOutputStream.write(packet.getPacket(), 0, 7);
        socketOutputStream.flush();
    }

    protected void receive() {
        new Thread(() -> {
            while (true) {
                byte[] message = new byte[7];
                try {
                    reader.readFully(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listener.onMessage(message);

                } catch (EOFException e) {
                    try {
                        scsSocket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
