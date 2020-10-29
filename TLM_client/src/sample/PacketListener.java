package sample;


import sample.packet.Header;

public interface PacketListener {
    void onMessage(Header header, byte[]data);
}
