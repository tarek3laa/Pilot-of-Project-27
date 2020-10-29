package sample.subsystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OBC extends SubSystem {
    public OBC() {
        commands = new ArrayList<>();
        commands.add(new Command(0x1, "on board time", false, false, 0, 0));
        commands.add(new Command(0x2, "signal Quality", false, false, 0, 100));
    }

    @Override
    public Object getSensorValue(Command command) {
        if (command.getId() == 0x1) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            return formatter.format(date);
        } else
            return super.getSensorValue(command);
    }
}
