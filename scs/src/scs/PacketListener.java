package scs;

public interface PacketListener {
    void onMessage(byte[] bytes);
    void onServerACK(byte[] bytes);
}
