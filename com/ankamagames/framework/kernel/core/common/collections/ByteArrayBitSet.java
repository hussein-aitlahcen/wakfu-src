package com.ankamagames.framework.kernel.core.common.collections;

import java.io.*;

public final class ByteArrayBitSet
{
    private static final int BITS_PER_UNIT = 8;
    private static final int BITS_PER_UNIT_DIVISION = 3;
    private byte[] m_bits;
    
    private ByteArrayBitSet() {
        super();
    }
    
    public ByteArrayBitSet(final ByteArrayBitSet byteArrayBitSet) {
        super();
        this.m_bits = new byte[byteArrayBitSet.m_bits.length];
        System.arraycopy(byteArrayBitSet.m_bits, 0, this.m_bits, 0, this.m_bits.length);
    }
    
    public ByteArrayBitSet(final int size) {
        super();
        this.m_bits = new byte[getDataLength(size)];
    }
    
    public ByteArrayBitSet(final int size, final boolean defaultValue) {
        super();
        this.m_bits = new byte[getDataLength(size)];
        this.setAll(defaultValue);
    }
    
    public final boolean get(final int index) {
        return get(this.m_bits, index);
    }
    
    public final void set(final int index, final boolean value) {
        set(this.m_bits, index, value);
    }
    
    public final void setAll(final boolean value) {
        if (value) {
            for (int i = 0; i < this.m_bits.length; ++i) {
                this.m_bits[i] = -1;
            }
        }
        else {
            for (int i = 0; i < this.m_bits.length; ++i) {
                this.m_bits[i] = 0;
            }
        }
    }
    
    private void resize(final int newSize) {
        assert newSize >= this.m_bits.length * 8 : "loosing data in BitSet (oldSize=" + this.m_bits.length + " newSize=" + newSize + ")";
        final byte[] newBits = new byte[(newSize + 7) / 8];
        System.arraycopy(this.m_bits, 0, newBits, 0, this.m_bits.length);
        this.m_bits = newBits;
    }
    
    public final int capacity() {
        return this.m_bits.length * 8;
    }
    
    private static byte bit(final int index) {
        assert index < 8 : "bit index should be < 8 , found : " + index;
        return (byte)(1 << index);
    }
    
    public final byte[] getByteArray() {
        return this.m_bits;
    }
    
    public final void write(final OutputStream outputStream) throws IOException {
        outputStream.write(this.m_bits);
    }
    
    public static ByteArrayBitSet fromByteArray(final byte[] array, final int offset, final int size) {
        final ByteArrayBitSet bitSet = new ByteArrayBitSet();
        System.arraycopy(array, offset, bitSet.m_bits = new byte[size], 0, size);
        return bitSet;
    }
    
    public static ByteArrayBitSet wrap(final byte[] array) {
        final ByteArrayBitSet bitSet = new ByteArrayBitSet();
        bitSet.m_bits = array;
        return bitSet;
    }
    
    public static boolean get(final byte[] bits, final int index) {
        assert index >> 3 < bits.length : "trying to get a bit index=" + index + " but only " + bits.length * 8 + " available.";
        final int unitPosition = index >> 3;
        final int bitPosition = 7 - (index - (unitPosition << 3));
        return (bits[unitPosition] & bit(bitPosition)) != 0x0;
    }
    
    public static void set(final byte[] bits, final int index, final boolean value) {
        assert index >> 3 < bits.length : "trying to set a bit index=" + index + " but only " + bits.length * 8 + " available.";
        final int unitPosition = index >> 3;
        final int bitPosition = 7 - (index - (unitPosition << 3));
        if (value) {
            final int n = unitPosition;
            bits[n] |= bit(bitPosition);
        }
        else {
            final int n2 = unitPosition;
            bits[n2] &= (byte)~bit(bitPosition);
        }
    }
    
    public static int getDataLength(final int size) {
        return size + 7 >> 3;
    }
}
