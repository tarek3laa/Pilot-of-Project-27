package sample;

import sample.packet.Header;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TlmServerStream {
    private DataInputStream reader;
    private final Listener listener;
    private OutputStream tlmClientOutStream;
    private Socket drtf;
    private DataOutputStream scs;

    public DataOutputStream getScs() {
        return scs;
    }

    public TlmServerStream(Listener listener) {
        this.listener = listener;
    }


    protected void connectingWithSCS() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(9090);
                Socket scs = serverSocket.accept();
                this.scs = new DataOutputStream(scs.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void connectingWithClient() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6060);
                Socket tlmClientSocket = serverSocket.accept();
                listener.logMessage("***client connected***");
                tlmClientOutStream = tlmClientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public OutputStream getTlmClientOutStream() {
        return tlmClientOutStream;
    }

    protected void connectingWithSimulator() {

        new Thread(() -> {

            try {
                ServerSocket serverSocket = new ServerSocket(5050);
                drtf = serverSocket.accept();
                this.reader = new DataInputStream(drtf.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();

            }
            receiveFromSimulator();
        }).start();

    }

    protected void receiveFromSimulator() {
        new Thread(() -> {

            while (true) {
                byte[] header = new byte[6];
                try {
                    reader.readFully(header);
                    Header header1 = new Header(header);
                    int length = Header.getIntVal(header1.getLength());

                    System.out.println();
                    byte[] data = new byte[length + 1];
                    reader.readFully(data);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listener.logMessage("*** new packet has been received ***");
                    listener.onMessage(header1, data);
                } catch (EOFException e) {
                    try {
                        drtf.close();
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
