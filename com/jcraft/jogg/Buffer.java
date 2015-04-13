package com.jcraft.jogg;

public class Buffer
{
    private static final int[] MASK;
    private int ptr;
    private byte[] buffer;
    private int endbit;
    private int endbyte;
    private int storage;
    
    public void read(final byte[] s, final int bytes) {
        for (int i = 0; i < bytes; ++i) {
            s[i] = (byte)this.read(8);
        }
    }
    
    public void wrap(final byte[] buf, final int start, final int bytes) {
        this.ptr = start;
        this.buffer = buf;
        final boolean b = false;
        this.endbyte = (b ? 1 : 0);
        this.endbit = (b ? 1 : 0);
        this.storage = bytes;
    }
    
    public int look(int bits) {
        final int m = Buffer.MASK[bits];
        bits += this.endbit;
        if (this.endbyte + 4 >= this.storage && this.endbyte + (bits - 1) / 8 >= this.storage) {
            return -1;
        }
        final int ret = this.look_ret(bits);
        return m & ret;
    }
    
    private int look_ret(final int bits) {
        int ret = (this.buffer[this.ptr] & 0xFF) >>> this.endbit;
        if (bits <= 8) {
            return ret;
        }
        ret |= (this.buffer[this.ptr + 1] & 0xFF) << 8 - this.endbit;
        if (bits > 16) {
            ret |= (this.buffer[this.ptr + 2] & 0xFF) << 16 - this.endbit;
            if (bits > 24) {
                ret |= (this.buffer[this.ptr + 3] & 0xFF) << 24 - this.endbit;
                if (bits > 32 && this.endbit != 0) {
                    ret |= (this.buffer[this.ptr + 4] & 0xFF) << 32 - this.endbit;
                }
            }
        }
        return ret;
    }
    
    public void adv(int bits) {
        bits += this.endbit;
        this.ptr += bits / 8;
        this.endbyte += bits / 8;
        this.endbit = (bits & 0x7);
    }
    
    public int read(final int bits) {
        final int m = Buffer.MASK[bits];
        final int bits2 = bits + this.endbit;
        final int bit_div_8 = bits2 / 8;
        if (this.endbyte + 4 >= this.storage && this.endbyte + (bits2 - 1) / 8 >= this.storage) {
            this.ptr += bit_div_8;
            this.endbyte += bit_div_8;
            this.endbit = (bits2 & 0x7);
            return -1;
        }
        int ret = this.look_ret(bits2);
        ret &= m;
        this.ptr += bit_div_8;
        this.endbyte += bit_div_8;
        this.endbit = (bits2 & 0x7);
        return ret;
    }
    
    public int read1() {
        final int ret = (this.endbyte >= this.storage) ? -1 : (this.buffer[this.ptr] >> this.endbit & 0x1);
        ++this.endbit;
        if (this.endbit > 7) {
            this.endbit = 0;
            ++this.ptr;
            ++this.endbyte;
        }
        return ret;
    }
    
    static {
        MASK = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1 };
    }
}
