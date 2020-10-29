package sample.subsystem;

import javafx.util.Pair;

import java.util.ArrayList;

public class Thermal extends SubSystem {
    public Thermal() {

    }

    @Override
    public Object getValue(byte[] data, int id) {

        if (id == 0x1) {
            double val = data[0];

            if (val >= 40 && val <= 80) {
                return new Pair<>("temperature: " + val, "green");
            } else return new Pair<>("temperature: " + val, "red");
        }
        return null;
    }

}
