package org.apache.tools.zip;

public final class ZipLong implements Cloneable
{
    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 16711680;
    private static final int BYTE_2_SHIFT = 16;
    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 4278190080L;
    private static final int BYTE_3_SHIFT = 24;
    private final long value;
    public static final ZipLong CFH_SIG;
    public static final ZipLong LFH_SIG;
    public static final ZipLong DD_SIG;
    static final ZipLong ZIP64_MAGIC;
    
    public ZipLong(final long value) {
        super();
        this.value = value;
    }
    
    public ZipLong(final byte[] bytes) {
        this(bytes, 0);
    }
    
    public ZipLong(final byte[] bytes, final int offset) {
        super();
        this.value = getValue(bytes, offset);
    }
    
    public byte[] getBytes() {
        return getBytes(this.value);
    }
    
    public long getValue() {
        return this.value;
    }
    
    public static byte[] getBytes(final long value) {
        final byte[] result = { (byte)(value & 0xFFL), (byte)((value & 0xFF00L) >> 8), (byte)((value & 0xFF0000L) >> 16), (byte)((value & 0xFF000000L) >> 24) };
        return result;
    }
    
    public static long getValue(final byte[] bytes, final int offset) {
        long value = bytes[offset + 3] << 24 & 0xFF000000L;
        value += (bytes[offset + 2] << 16 & 0xFF0000);
        value += (bytes[offset + 1] << 8 & 0xFF00);
        value += (bytes[offset] & 0xFF);
        return value;
    }
    
    public static long getValue(final byte[] bytes) {
        return getValue(bytes, 0);
    }
    
    public boolean equals(final Object o) {
        return o != null && o instanceof ZipLong && this.value == ((ZipLong)o).getValue();
    }
    
    public int hashCode() {
        return (int)this.value;
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
        return "ZipLong value: " + this.value;
    }
    
    static {
        CFH_SIG = new ZipLong(33639248L);
        LFH_SIG = new ZipLong(67324752L);
        DD_SIG = new ZipLong(134695760L);
        ZIP64_MAGIC = new ZipLong(4294967295L);
    }
}
