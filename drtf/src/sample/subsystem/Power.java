package sample.subsystem;

import java.util.ArrayList;

public class Power extends SubSystem {
    public Power() {
        commands = new ArrayList<>();
        commands.add(new Command(0x1, "power reading", false, false, 0, 100));
    }
}
