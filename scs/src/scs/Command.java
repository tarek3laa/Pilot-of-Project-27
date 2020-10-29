package scs;

public class Command {
    private String name;
    private int id;
    private int subsystemApid;

    public Command(String name, int id, int subsystemApid) {
        this.name = name;
        this.id = id;
        this.subsystemApid = subsystemApid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getSubsystemApid() {
        return subsystemApid;
    }
}
