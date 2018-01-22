package sample;

public class Commend {

    byte type;
    byte value;

    public Commend(byte type, byte value) {
        this.type = type;
        this.value = value;
    }

    public byte getType() {

        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
