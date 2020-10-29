package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import sample.packet.Header;
import sample.subsystem.SubSystem;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable, PacketListener {

    @FXML
    private ListView<Label> listView_OBC, listView_power, listView_thermal, listView_orbit;
    private ObservableList<Label> OBCObservableList, powerObservableList, thermalObservableList, ADCSObservableList;
    private Map<Integer, ObservableList<Label>> integerObservableListMap;

    public Controller() {
        TlmClientSocket tlmClientSocket = new TlmClientSocket("localhost", 6060, this);
        if (tlmClientSocket.Connect()) {
            tlmClientSocket.receive();
        }
        integerObservableListMap = new HashMap<>();


        OBCObservableList = FXCollections.observableArrayList(new Label("OBC"));
        powerObservableList = FXCollections.observableArrayList(new Label("power subsystem"));
        thermalObservableList = FXCollections.observableArrayList(new Label("thermal subsystem"));
        ADCSObservableList = FXCollections.observableArrayList(new Label("ADCS"));


        integerObservableListMap.put(Constants.OBC_APID, OBCObservableList);
        integerObservableListMap.put(Constants.POWER_APID, powerObservableList);
        integerObservableListMap.put(Constants.ADCS_APID, ADCSObservableList);
        integerObservableListMap.put(Constants.THERMAL_APID, thermalObservableList);



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView_OBC.setItems(OBCObservableList);
        listView_power.setItems(powerObservableList);
        listView_thermal.setItems(thermalObservableList);
        listView_orbit.setItems(ADCSObservableList);

    }

    @Override
    public void onMessage(Header header, byte[] data) {
        System.out.println("done");
        int apid = Header.getIntVal(header.getApid());
        int id = Header.getIntVal(header.getPacketSeq());
        SubSystem subSystem = SubSystem.getSubsystem(apid);
        Object o = subSystem.getValue(data, id);
        if (o instanceof Pair) {
            setSensorData(integerObservableListMap.get(apid), ((Pair<String, String>) o).getKey(), ((Pair<String, String>) o).getValue());
        } else
            setSensorData(integerObservableListMap.get(apid), (String) o, null);

    }

    private void setSensorData(ObservableList<Label> observableList, String data, String color) {
        Label label = new Label(data);
        label.setPrefWidth(450);
        if (color != null)
            label.setStyle("-fx-background-color: " + color);
        label.setAlignment(Pos.CENTER);
        Platform.runLater(() -> observableList.add(label));
    }
}
