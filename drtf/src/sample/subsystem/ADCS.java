package sample.subsystem;

import java.util.ArrayList;
import java.util.Random;

public class ADCS extends SubSystem {

    public ADCS() {
        commands = new ArrayList<>();
        commands.add(new Command(0x1, "current coordinate", false, false, 0, 0));
        commands.add(new Command(0x2, "switch ADCS on", true, false, 0, 0));
        commands.add(new Command(0x3, "switch ADCS off", true, false, 0, 0));
    }

    @Override
    public Object getSensorValue(Command command) {
        if (command.getId() == 0x1) {

            double longitude = (double) super.getSensorValue(new Command(0x0, "", false, true, -85, 85));
            double latitude = (double) super.getSensorValue(new Command(0x0, "", false, true, -180, 180));
            return longitude + " , " + latitude;
        } else
            return super.getSensorValue(command);
    }
}
