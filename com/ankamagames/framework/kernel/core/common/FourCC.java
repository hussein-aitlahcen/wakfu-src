package com.ankamagames.framework.kernel.core.common;

public final class FourCC
{
    public static final FourCC RAW;
    public static final FourCC TGA;
    public static final FourCC TGAM;
    public static final FourCC PNG;
    public static final FourCC BMP;
    public static final FourCC JPG;
    private final int m_id;
    
    public FourCC(final int id) {
        super();
        this.m_id = id;
    }
    
    public FourCC(final String string) {
        super();
        this.m_id = stringToID(string);
    }
    
    public static int stringToID(final String string) {
        final byte[] array = string.getBytes();
        int id = 0;
        if (array.length == 0) {
            return id;
        }
        if (array.length >= 1) {
            id |= Caster.toUnsignedByte(array[0]);
        }
        if (array.length >= 2) {
            id |= Caster.toUnsignedByte(array[1]) << 8;
        }
        if (array.length >= 3) {
            id |= Caster.toUnsignedByte(array[2]) << 16;
        }
        if (array.length >= 4) {
            id |= Caster.toUnsignedByte(array[3]) << 24;
        }
        return id;
    }
    
    public static String idToString(final int id) {
        final byte[] array = { (byte)(id & 0xFF), (byte)(id >> 8 & 0xFF), (byte)(id >> 16 & 0xFF), (byte)(id >> 24) };
        return new String(array);
    }
    
    @Override
    public String toString() {
        return idToString(this.m_id);
    }
    
    public int getID() {
        return this.m_id;
    }
    
    static {
        RAW = new FourCC("RAW");
        TGA = new FourCC("TGA");
        TGAM = new FourCC("TGAM");
        PNG = new FourCC("PNG");
        BMP = new FourCC("BMP");
        JPG = new FourCC("JPG");
    }
}
