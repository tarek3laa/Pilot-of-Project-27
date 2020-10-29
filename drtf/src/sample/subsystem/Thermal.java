package sample.subsystem;

import java.util.ArrayList;

public class Thermal extends SubSystem {
    public Thermal() {
        commands = new ArrayList<>();
        commands.add(new Command(0x1, "get temperature", false, true, 0, 100));
    }

}
