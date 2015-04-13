package org.apache.tools.zip;

public final class ZipShort implements Cloneable
{
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private final int value;
    
    public ZipShort(final int value) {
        super();
        this.value = value;
    }
    
    public ZipShort(final byte[] bytes) {
        this(bytes, 0);
    }
    
    public ZipShort(final byte[] bytes, final int offset) {
        super();
        this.value = getValue(bytes, offset);
    }
    
    public byte[] getBytes() {
        final byte[] result = { (byte)(this.value & 0xFF), (byte)((this.value & 0xFF00) >> 8) };
        return result;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public static byte[] getBytes(final int value) {
        final byte[] result = { (byte)(value & 0xFF), (byte)((value & 0xFF00) >> 8) };
        return result;
    }
    
    public static int getValue(final byte[] bytes, final int offset) {
        int value = bytes[offset + 1] << 8 & 0xFF00;
        value += (bytes[offset] & 0xFF);
        return value;
    }
    
    public static int getValue(final byte[] bytes) {
        return getValue(bytes, 0);
    }
    
    public boolean equals(final Object o) {
        return o != null && o instanceof ZipShort && this.value == ((ZipShort)o).getValue();
    }
    
    public int hashCode() {
        return this.value;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
    
    public String toString() {
        return "ZipShort value: " + this.value;
    }
}
