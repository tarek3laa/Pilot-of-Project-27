package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import sample.packet.Header;
import sample.packet.Packet;
import sample.subsystem.SubSystem;

import java.net.URL;
import java.util.*;

public class Controller implements Listener, Initializable {
    private DRTFStream drtfStream;
    @FXML
    private ListView<String> listView;
    private ObservableList<String> sensorsObservableList;
    private final List<String> commands;

    public Controller() {
        sensorsObservableList = FXCollections.observableArrayList();
        drtfStream = new DRTFStream(this);
        drtfStream.connectingWithServer();
        drtfStream.connectingWithSCS();
        commands = new ArrayList<>();

        commands.add("get on-board time");
        commands.add("get signal Quality");
        commands.add("get current coordinate");

        commands.add("switch ADCS on");
        commands.add("switch ADCS off");
        commands.add("get power reading");

        commands.add("get temperature");
    }

    @Override
    public synchronized void onMessage(byte[] bytes) {
        Packet packet = new Packet(bytes);
        int data = packet.getData()[0];
        Header header = packet.getHeader();


        Platform.runLater(() -> {
            sensorsObservableList.add("SCS: " + commands.get(data - 1));
            sensorsObservableList.add("\n");
        });
        try {
            int apid = Header.getIntVal(header.getApid());
            SubSystem subSystem = SubSystem.getSubsystem(apid);
            int commandId = Header.getIntVal(header.getPacketSeq());
            byte[] tlmPacket = creatTLMPacket(subSystem.getSensorValue(subSystem.getCommandByID(commandId)));


            Packet packet1 = new Packet(new Header(0x0, 0x1, 0x0, apid, 0x0, commandId, tlmPacket.length - 1), tlmPacket);
            drtfStream.getServerSender().write(packet1.getPacket(), 0, 6 + tlmPacket.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] creatTLMPacket(Object data) {
        byte[] bytesData = new byte[0];
        if (data instanceof String) {
            String s = (String) data;
            bytesData = s.getBytes();
        } else if (data instanceof Double) {

            bytesData = new byte[1];
            bytesData[0] = (byte) ((double) data);
        } else if (data instanceof Integer) {
            bytesData = new byte[1];
            bytesData[0] = (byte) ((int) data);
        }
        return bytesData;
    }

    @Override
    public void logMessage(String message) {
        System.out.println(message);
        Platform.runLater(() -> {
            sensorsObservableList.add(message);
        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setItems(sensorsObservableList);

    }
}
