package scs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import scs.packet.Header;
import scs.packet.Packet;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import java.util.List;
import java.util.ResourceBundle;


/**
 * this class is responsible for handle the actions that happens on the screen
 */
public class ScsScreenController implements PacketListener, Initializable {

    // screen components
    @FXML
    private ListView<String> ackList;
    @FXML
    private ListView<CheckBox> commandCheckBoxList;
    @FXML
    private Button sendButton;

    private ScsStream stream;

    private final List<Command> commandList;

    private ObservableList<String> ack;

    protected ScsScreenController() {
        ack = FXCollections.observableArrayList();
        commandList = new ArrayList<>();
        commandList.add(new Command("get on-board time ", 0x1, Constants.OBC_APID));
        commandList.add(new Command("get signal Quality", 0x2, Constants.OBC_APID));
        commandList.add(new Command("get current coordinate", 0x1, Constants.ADCS_APID));
        commandList.add(new Command("switch ADCS on", 0x2, Constants.ADCS_APID));
        commandList.add(new Command("switch ADCS off", 0x3, Constants.ADCS_APID));
        commandList.add(new Command("get power reading", 0x1, Constants.POWER_APID));
        commandList.add(new Command("get temperature", 0x1, Constants.THERMAL_APID));


        stream = new ScsStream("localhost", 4040, this);

    }

    protected void onStart() {


        commandCheckBoxList.setItems(getCommands());

        stream.connectWithDRTF();
        stream.connectWithServer();
        stream.receive();

        sendButton.setOnMouseClicked(mouseEvent -> {
            ObservableList<CheckBox> list = commandCheckBoxList.getItems();
            for (int i = 0; i < list.size(); i++) {
                CheckBox checkBox = list.get(i);
                if (checkBox.isSelected()) {

                    Header header = new Header(0x0, 0x1, 0x0, commandList.get(i).getSubsystemApid(), 0x0, commandList.get(i).getId(), 0);
                    Packet packet = new Packet(header, ByteBuffer.allocate(1).put((byte) (i + 1)).array());

                    try {
                        stream.send(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * this method is responsible for creating the check boxes
     *
     * @return observableList contains list of check boxes
     */
    private ObservableList<CheckBox> getCommands() {
        ObservableList<CheckBox> observableList = FXCollections.observableArrayList();
        for (Command command : commandList) {
            observableList.add(new CheckBox(command.getName()));
        }

        return observableList;
    }

    private void setACK(String message) {
        Platform.runLater(() -> ack.add(message));

    }

    @Override
    public void onMessage(byte[] bytes) {
        Packet packet = new Packet(bytes);
        System.out.println(packet.toString());
        setACK(packet.toString());
    }

    @Override
    public void onServerACK(byte[] bytes) {
        Packet packet = new Packet(bytes);
        Header header = packet.getHeader();
        int id = Header.getIntVal(header.getPacketSeq());
        int apid = Header.getIntVal(header.getApid());
        ObservableList<CheckBox> list = commandCheckBoxList.getItems();
        int data = packet.getData()[0];
        String color = (data == 1) ? "green" : "red";

        for (int i = 0; i < commandList.size(); i++) {
            if (commandList.get(i).getId() == id && commandList.get(i).getSubsystemApid()==apid){
                list.get(i).setStyle("-fx-background-color: " + color);
            }
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ackList.setItems(ack);
    }
}
