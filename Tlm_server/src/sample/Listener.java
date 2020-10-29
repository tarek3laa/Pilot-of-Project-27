package sample;

import sample.packet.Header;

public interface Listener {
    void onMessage(Header header1, byte[] bytes);

    void logMessage(String message);
}
