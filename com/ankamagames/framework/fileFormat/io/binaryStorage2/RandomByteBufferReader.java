package com.ankamagames.framework.fileFormat.io.binaryStorage2;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RandomByteBufferReader extends Randomizer
{
    private final ByteBuffer m_buffer;
    
    public RandomByteBufferReader(final ByteBuffer buffer, final int mult, final int add) {
        super(mult, add);
        this.m_buffer = buffer;
    }
    
    public long position() {
        return this.m_buffer.position();
    }
    
    protected void position(final int position, final byte seed) {
        this.m_seed = seed;
        this.m_buffer.position(position);
    }
    
    public byte get() {
        this.inc();
        return (byte)(this.m_buffer.get() - this.m_seed);
    }
    
    public boolean readBoolean() {
        this.inc();
        return this.m_buffer.get() - this.m_seed != 0;
    }
    
    public short getShort() {
        this.inc();
        return (short)(this.m_buffer.getShort() - this.m_seed);
    }
    
    public float getFloat() {
        this.inc();
        return this.m_buffer.getFloat();
    }
    
    public int getInt() {
        this.inc();
        return this.m_buffer.getInt() - this.m_seed;
    }
    
    public double getDouble() {
        this.inc();
        return this.m_buffer.getDouble();
    }
    
    public long getLong() {
        this.inc();
        return this.m_buffer.getLong() - this.m_seed;
    }
    
    public String readUTF8() {
        final int size = this.getInt();
        final byte[] data = new byte[size];
        this.m_buffer.get(data);
        return StringUtils.fromUTF8(data);
    }
    
    public byte[] readByteArray() {
        final int size = this.getInt();
        final byte[] data = new byte[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.get();
        }
        return data;
    }
    
    public int[] readIntArray() {
        final int size = this.getInt();
        final int[] data = new int[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.getInt();
        }
        return data;
    }
    
    public short[] readShortArray() {
        final int size = this.getInt();
        final short[] data = new short[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.getShort();
        }
        return data;
    }
    
    public float[] readFloatArray() {
        final int size = this.getInt();
        final float[] data = new float[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.getFloat();
        }
        return data;
    }
    
    public String[] readStringArray() {
        final int size = this.getInt();
        final String[] data = new String[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.readUTF8();
        }
        return data;
    }
    
    public long[] readLongArray() {
        final int size = this.getInt();
        final long[] data = new long[size];
        for (int i = 0; i < size; ++i) {
            data[i] = this.getLong();
        }
        return data;
    }
}
