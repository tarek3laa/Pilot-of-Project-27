package sample;


import sample.packet.Header;
import sample.packet.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DRTFStream {


    private DataInputStream reader;
    private OutputStream serverSender;
    private final Listener listener;
    private DataOutputStream scsSend;

    private Socket tlmClientSocket;

    public DRTFStream(Listener listener) {
        this.listener = listener;

    }

    protected void connectingWithSCS() {
        new Thread(() -> {
            try {

                ServerSocket serverSocket = new ServerSocket(4040);
                listener.logMessage("*** Waiting SCS ...... ***");


                tlmClientSocket = serverSocket.accept();
                listener.logMessage("***Connected with SCS***\n\n\n");

                reader = new DataInputStream(tlmClientSocket.getInputStream());
                scsSend = new DataOutputStream(tlmClientSocket.getOutputStream());

                receiveFromSCS();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }

    public OutputStream getServerSender() {
        return serverSender;
    }

    protected void connectingWithServer() {
        try {

            Socket socket = new Socket("localhost", 5050);
            serverSender = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener.logMessage("***connected with server***");
    }

    protected void receiveFromSCS() {

        new Thread(() -> {
            while (true) {
                byte[] message = new byte[7];
                try {
                    reader.readFully(message);
                } catch (EOFException e) {
                    try {
                        tlmClientSocket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } catch (IOException e) {
                    sendACK((byte) 0x0);
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sendACK((byte) 0x1);
                listener.onMessage(message);

            }
        }).start();
    }

    private void sendACK(byte b) {

        Header header = new Header(0x0, 0x0, 0x0, 0x3f, 0x0, 0x0, 0x0);
        Packet packet = new Packet(header, ByteBuffer.allocate(1).put((b)).array());

        try {
            scsSend.write(packet.getPacket(), 0, 7);
            scsSend.flush();
        } catch (EOFException e) {

        } catch (SocketException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
