package scs.packet;

public class Packet {

    private Header header;
    private byte[] data;

    public Packet() {
        header = new Header();
        data = new byte[1];
    }

    public Packet(byte[] bytes) {
        fromBytes(bytes);
    }

    public Packet(Header header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    public byte[] getPacket() {
        byte[] headerBytes = header.getHeaderBytes();
        byte[] result = new byte[headerBytes.length + data.length];

        System.arraycopy(headerBytes, 0, result, 0, headerBytes.length);
        System.arraycopy(data, 0, result, headerBytes.length, data.length);
        return result;
    }

    private void fromBytes(byte[] bytes) {

        byte[] header = new byte[6];
        System.arraycopy(bytes, 0, header, 0, 6);

        this.header = new Header(header);
        this.data = new byte[1];

        this.data[0] = bytes[6];
    }

    public Header getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {

        int data = this.data[0];

        return data == 1 ? "DRTF : commands received" : "DRTF : there is an error";
    }
}