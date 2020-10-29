package sample.subsystem;

import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OBC extends SubSystem {
    public OBC() {
    }

    @Override
    public Object getValue(byte[] data, int id) {
        if (id == 0x1) {
            return "time : " + new String(data);
        } else {
            int value = data[0];
            if (value >= 80) return new Pair("signal quality : very good ( " + value + " %)", "green");
            else if (value >= 60) return new Pair("signal quality : good ( " + value + " %)", "yellow");
            else return new Pair<>("signal quality : bad ( " + value + " %)", "red");
        }
    }
}
