package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import sample.packet.Header;
import sample.packet.Packet;
import sample.subsystem.Subsystem;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Listener, Initializable {

    @FXML
    private ListView<String> listView;
    private ObservableList<String> sensorsObservableList;
    private TlmServerStream tlmServerSocket;

    public Controller() {

        sensorsObservableList = FXCollections.observableArrayList();
        tlmServerSocket = new TlmServerStream(this);
        tlmServerSocket.connectingWithClient();
        tlmServerSocket.connectingWithSimulator();
        tlmServerSocket.connectingWithSCS();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setItems(sensorsObservableList);
    }

    @Override
    public void logMessage(String message) {
        Platform.runLater(() -> sensorsObservableList.add(message));

    }

    @Override
    public void onMessage(Header header, byte[] data) {
        byte[] ack = new byte[1];


        if (data.length == Header.getIntVal(header.getLength()) + 1) {
            ack[0] = (byte) 1;

        } else {
            ack[0] = 0;
        }
        Packet packet = new Packet(header, ack);
        try {
            tlmServerSocket.getScs().write(packet.getPacket());
        } catch (IOException e) {

        }

        Map<Integer, Pair<String, List<String>>> subsystem = Subsystem.getSubsystem();
        logMessage("************************** header ****************************");
        Pair<String, List<String>> stringListPair = subsystem.get(Header.getIntVal(header.getApid()));
        logMessage(stringListPair.getKey() + "  Subsystem");
        logMessage(stringListPair.getValue().get(Header.getIntVal(header.getPacketSeq()) - 1));
        logMessage("sending data to tlm client .... ");

        try {
            tlmServerSocket.getTlmClientOutStream().write(new Packet(header, data).getPacket(), 0, 6 + data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
