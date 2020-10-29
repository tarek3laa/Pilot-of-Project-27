package sample.subsystem;

import sample.Constants;

import java.util.HashMap;

public abstract class SubSystem {
    private final static HashMap<Integer, SubSystem> hashMap = new HashMap<>();

    protected SubSystem() {
    }

    public static void init() {
        hashMap.put(Constants.OBC_APID, new OBC());
        hashMap.put(Constants.ADCS_APID, new ADCS());
        hashMap.put(Constants.THERMAL_APID, new Thermal());
        hashMap.put(Constants.POWER_APID, new Power());

    }

    public static SubSystem getSubsystem(int apid) {
        System.out.println(apid);
        System.out.println(hashMap.size());
        return hashMap.get(apid);
    }

    public abstract Object getValue(byte[] data, int id);


}
