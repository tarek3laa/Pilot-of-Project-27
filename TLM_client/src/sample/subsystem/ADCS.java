package sample.subsystem;

import javafx.scene.web.HTMLEditorSkin;
import javafx.util.Pair;

import java.util.ArrayList;

public class ADCS extends SubSystem {

    public ADCS() {

    }

    @Override
    public Object getValue(byte[] data, int id) {
        switch (id) {
            case 0x1:
                return new String(data);
            case 0x2:
                int val = data[0];
                return val == 1 ? new Pair<>("ADCS switched on successfully", "green") : new Pair<>("Failed to switch ADCS on", "red");
            case 0x3:
                val = data[0];
                return val == 1 ? new Pair<>("ADCS switched off successfully", "green") : new Pair<>("Failed to switch ADCS off", "red");

        }
        return null;
    }


}
