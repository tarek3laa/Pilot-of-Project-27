package sample.subsystem;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subsystem {
    private static Map<Integer, Pair<String, List<String>>> subsystem;

    public Subsystem() {

    }

    public static void init() {
        subsystem = new HashMap<>();
        List<String> obc = new ArrayList<>();
        obc.add("on board time");
        obc.add("signal Quality");
        subsystem.put(Constants.OBC_APID, new Pair<>("On-board computer", obc));


        List<String> adcs = new ArrayList<>();
        adcs.add("current coordinate");
        adcs.add("switch ADCS on");
        adcs.add("switch ADCS off");
        subsystem.put(Constants.ADCS_APID, new Pair<>("ADCS", adcs));
        List<String> power = new ArrayList<>();
        power.add("power reading");
        subsystem.put(Constants.POWER_APID, new Pair<>("power", power));
        List<String> thermal = new ArrayList<>();
        thermal.add("get temperature");
        subsystem.put(Constants.THERMAL_APID, new Pair<>("thermal", thermal));
    }

    public static Map<Integer, Pair<String, List<String>>> getSubsystem() {
        return subsystem;
    }
}
