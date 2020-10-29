package sample;

public interface Listener {
    void onMessage(byte[] bytes);

    void logMessage(String message);
}
