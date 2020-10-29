package scs.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Header {

    // version 000                   3 bit     //  7
    //type id   1 for command        1 bit     //  1
    // flag 0                         1 bit    //  1
    // apid ====>                    11 bit    // 2047

    //*************** 2 byte *****************


    ///    16 8 4 2 1
    // seq flag 2 bit                                00    //            3
    // packet seq count    14 bit      command index      // 16383
    //  ************ 2 byte *********************

    // length                  length - 1
    private boolean[] version, typeId, flag, apid, segFlag, packetSeq, length;
    private List<boolean[]> header;

    public Header() {
        init();

    }

    private void init() {
        version = new boolean[3];
        typeId = new boolean[1];
        flag = new boolean[1];
        apid = new boolean[11];
        segFlag = new boolean[2];
        packetSeq = new boolean[14];
        length = new boolean[16];

        header = new ArrayList<>();

        header.add(version);
        header.add(typeId);
        header.add(flag);
        header.add(apid);
        header.add(segFlag);
        header.add(packetSeq);
        header.add(length);

    }

    public Header(int version, int typeId, int flag, int apid, int segFlag, int packetSeq, int length) {
        init();
        convert(version, this.version);
        convert(typeId, this.typeId);
        convert(flag, this.flag);
        convert(apid, this.apid);
        convert(segFlag, this.segFlag);
        convert(packetSeq, this.packetSeq);
        convert(length, this.length);
    }

    public Header(byte[] bytes) {
        init();
        fromBytes(bytes);
    }

    private void convert(int hex, boolean[] bits) {
        String bitString = Integer.toString(hex, 2);
        for (int i = bitString.length() - 1, j = bits.length - 1; i >= 0; i--, j--) {
            bits[j] = bitString.charAt(i) == '1';
        }
    }

    public List<boolean[]> getHeader() {
        return header;
    }

    public String getHeaderBinary() {
        String binary = "";
        for (int i = header.size() - 1; i >= 0; i--) {

            for (int j = 0; j < header.get(i).length; j++) {
                binary += header.get(i)[j] ? "1" : "0";
            }
        }
        return binary;
    }

    public byte[] getHeaderBytes() {
        byte[] headerBytes = new byte[6];
        long l = Long.parseLong(getHeaderBinary(), 2);
        byte[] bytes = ByteBuffer.allocate(8).putLong(l).array();
        if (bytes.length - 2 >= 0) System.arraycopy(bytes, 2, headerBytes, 0, bytes.length - 2);

        return headerBytes;
    }

    private void fromBytes(byte[] header) {
        byte[] bytes = new byte[8];

        System.arraycopy(header, 0, bytes, 2, header.length);
        bytes[0] = 0;
        bytes[1] = 0;
        long l = ByteBuffer.wrap(bytes).getLong();
        fromLong(l);
    }

    private void fromLong(long l) {
        String bits = Long.toString(l, 2);
        int j = bits.length() - 1;
        for (int i = 0; i < this.header.size(); i++) {
            for (int k = this.header.get(i).length - 1; k >= 0; k--) {
                if (j < 0) return;
                this.header.get(i)[k] = bits.charAt(j) == '1';
                j--;
            }
        }

    }

    public boolean[] getVersion() {
        return version;
    }

    public boolean[] getTypeId() {
        return typeId;
    }

    public boolean[] getFlag() {
        return flag;
    }

    public boolean[] getApid() {
        return apid;
    }

    public boolean[] getSegFlag() {
        return segFlag;
    }

    public boolean[] getPacketSeq() {
        return packetSeq;
    }

    public boolean[] getLength() {
        return length;
    }

    public static int getIntVal(boolean[] bits) {
        int value = 0;
        int j = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            value += bits[i] ? Math.pow(2, j) : 0;
            j++;
        }
        return value;
    }
}
