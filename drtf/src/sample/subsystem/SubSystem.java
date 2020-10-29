package sample.subsystem;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class SubSystem {
    private final static HashMap<Integer, SubSystem> hashMap = new HashMap<>();
    protected List<Command> commands;

    protected SubSystem() {
    }

    public static void init() {
        hashMap.put(0x1, new OBC());
        hashMap.put(0x2, new ADCS());
        hashMap.put(0x3, new Thermal());
        hashMap.put(0x4, new Power());

    }

    public static SubSystem getSubsystem(int apid) {
        System.out.println(apid);
        System.out.println(hashMap.size());
        return hashMap.get(apid);
    }

    public Command getCommandByID(int id) {
        return commands.get(id - 1);
    }


    public Object getSensorValue(Command command) {
        Random random = new Random();
        if (command.isBinary()) {
            return random.nextInt(2);
        } else if (command.isHaveFraction()) {
            return random.nextDouble() * (command.getHigh() - command.getLow() + 1) + command.getLow();
        } else {
            return random.nextInt((int) (command.getHigh() - command.getLow() + 1)) + command.getLow();
        }
    }

}
