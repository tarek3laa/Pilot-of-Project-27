package sample.subsystem;

import javafx.util.Pair;

import java.util.ArrayList;

public class Power extends SubSystem {
    public Power() {

    }

    @Override
    public Object getValue(byte[] data, int id) {
        if (id == 0x1) {
            int value = data[0];
            if (value >= 70) return new Pair("power: very good ( " + value + " %)", "green");
            else if (value >= 50) return new Pair("power : good ( " + value + " %)", "yellow");
            else return new Pair<>("power : bad ( " + value + " %)", "red");
        }
        return null;
    }
}
