package com.ankamagames.framework.fileFormat.io;

import java.util.zip.*;
import java.io.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class OutputBitStream
{
    private OutputStream m_stream;
    private ByteArrayOutputStream m_memoryStream;
    private int m_bitBuffer;
    private int m_bitCursor;
    private boolean m_compressed;
    private long m_offset;
    private boolean m_isMemoryStream;
    
    public OutputBitStream(final OutputStream stream) {
        super();
        this.m_compressed = false;
        this.m_stream = stream;
    }
    
    public OutputBitStream() {
        super();
        this.m_compressed = false;
        this.m_memoryStream = new ByteArrayOutputStream();
        this.m_stream = this.m_memoryStream;
        this.m_isMemoryStream = true;
    }
    
    public OutputBitStream(final int capacity) {
        super();
        this.m_compressed = false;
        this.m_memoryStream = new ByteArrayOutputStream(capacity);
        this.m_stream = this.m_memoryStream;
        this.m_isMemoryStream = true;
    }
    
    public byte[] getData() {
        if (!this.m_isMemoryStream) {
            throw new IllegalStateException("Use this method only with memory streams!");
        }
        try {
            this.m_stream.close();
        }
        catch (IOException ex) {}
        return this.m_memoryStream.toByteArray();
    }
    
    public static int getFPBitsLength(final double value) {
        if (value == 0.0) {
            return 1;
        }
        final long fpBits = (long)(value * 65536.0);
        return getSignedBitsLength(fpBits);
    }
    
    public long getOffset() {
        return this.m_offset;
    }
    
    public static int getSignedBitsLength(final long value) {
        int nBits;
        if (value == 0L) {
            nBits = 0;
        }
        else {
            nBits = (int)(Math.floor(Math.log(Math.abs(value)) / Math.log(2.0)) + 2.0);
        }
        return nBits;
    }
    
    public static int getUnsignedBitsLength(final long value) {
        if (value < 1L) {
            return 0;
        }
        return (int)(Math.floor(Math.log(value) / Math.log(2.0)) + 1.0);
    }
    
    public void align() throws IOException {
        if (this.m_bitCursor > 0) {
            this.m_stream.write(this.m_bitBuffer);
            ++this.m_offset;
            this.m_bitCursor = 0;
            this.m_bitBuffer = 0;
        }
    }
    
    public void close() throws IOException {
        this.align();
        this.m_stream.close();
    }
    
    public void enableCompression() {
        if (!this.m_compressed) {
            this.m_stream = new BufferedOutputStream(new DeflaterOutputStream(this.m_stream, new Deflater(9)));
            this.m_compressed = true;
        }
    }
    
    public void flush() throws IOException {
        this.m_stream.flush();
    }
    
    public void writeBooleanBit(final boolean value) throws IOException {
        this.writeUnsignedBits(value ? 1 : 0, 1);
    }
    
    public void writeBytes(final byte[] buffer) throws IOException {
        this.align();
        if (buffer == null) {
            return;
        }
        this.m_stream.write(buffer);
        this.m_offset += buffer.length;
    }
    
    public void writeDouble(final double value) throws IOException {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putDouble(value);
        this.writeBytes(bb.array());
    }
    
    public void writeFP16(final double value) throws IOException {
        this.writeShort((short)(value * 256.0));
    }
    
    public void writeDecimalFromBits(final double value, final int nBits) throws IOException {
        final long fpBits = (long)(value * 65536.0);
        this.writeSignedBits(fpBits, nBits);
    }
    
    public void writeFloat(final float value) throws IOException {
        this.writeInt(Float.floatToIntBits(value));
    }
    
    public void writeFloat16(final float value) throws IOException {
        final int bits32 = Float.floatToIntBits(value);
        final int sign = Math.abs((bits32 & Integer.MIN_VALUE) >> 31);
        final int exponent32 = (bits32 & 0x7F800000) >> 23;
        final int mantissa32 = bits32 & 0x7FFFFF;
        int exponent2 = 0;
        if (exponent32 != 0) {
            if (exponent32 == 255) {
                exponent2 = 31;
            }
            else {
                exponent2 = exponent32 - 127 + 15;
            }
        }
        int mantissa2 = 0;
        if (exponent2 < 0) {
            exponent2 = 0;
        }
        else if (exponent2 > 31) {
            exponent2 = 31;
        }
        else {
            mantissa2 = mantissa32 >> 13;
        }
        int bits2 = sign << 15;
        bits2 |= exponent2 << 10;
        bits2 |= mantissa2;
        this.writeUnsignedShort(bits2);
    }
    
    public void writeShort(final short value) throws IOException {
        this.align();
        this.m_stream.write(value & 0xFF);
        this.m_stream.write(value >> 8);
        this.m_offset += 2L;
    }
    
    public void writeInt(final int value) throws IOException {
        this.align();
        this.m_stream.write(value & 0xFF);
        this.m_stream.write(value >> 8);
        this.m_stream.write(value >> 16);
        this.m_stream.write(value >> 24);
        this.m_offset += 4L;
    }
    
    public void writeLong(final long value) throws IOException {
        this.align();
        this.m_stream.write((byte)(value & 0xFFL));
        this.m_stream.write((byte)(value >> 8));
        this.m_stream.write((byte)(value >> 16));
        this.m_stream.write((byte)(value >> 24));
        this.m_stream.write((byte)(value >> 32));
        this.m_stream.write((byte)(value >> 40));
        this.m_stream.write((byte)(value >> 48));
        this.m_stream.write((byte)(value >> 56));
        this.m_offset += 8L;
    }
    
    public void writeByte(final byte value) throws IOException {
        this.align();
        this.m_stream.write(value);
        ++this.m_offset;
    }
    
    public void writeSignedBits(final long value, final int nBits) throws IOException {
        final int bitsNeeded = getSignedBitsLength(value);
        if (nBits < bitsNeeded) {
            throw new IOException("At least " + bitsNeeded + " bits needed for representation of " + value);
        }
        this.writeInteger(value, nBits);
    }
    
    public void writeString(final String string) throws IOException {
        this.writeBytes(StringUtils.toUTF8(string));
        this.m_stream.write(0);
        ++this.m_offset;
    }
    
    public void writeUnsignedShort(final int value) throws IOException {
        this.align();
        this.m_stream.write(value & 0xFF);
        this.m_stream.write(value >> 8);
        this.m_offset += 2L;
    }
    
    public void writeUnsignedInt(final long value) throws IOException {
        this.align();
        this.m_stream.write((int)(value & 0xFFL));
        this.m_stream.write((int)(value >> 8));
        this.m_stream.write((int)(value >> 16));
        this.m_stream.write((int)(value >> 24));
        this.m_offset += 4L;
    }
    
    public void writeUnsignedByte(final short value) throws IOException {
        this.align();
        this.m_stream.write(value);
        ++this.m_offset;
    }
    
    public void writeUnsignedBits(final long value, final int nBits) throws IOException {
        final int bitsNeeded = getUnsignedBitsLength(value);
        if (nBits < bitsNeeded) {
            throw new IOException("At least " + bitsNeeded + " bits needed for representation of " + value + ". Used bits: " + nBits);
        }
        this.writeInteger(value, nBits);
    }
    
    private void writeInteger(final long value, final int nBits) throws IOException {
        for (int bitsLeft = nBits; bitsLeft > 0; --bitsLeft) {
            ++this.m_bitCursor;
            if ((1L << bitsLeft - 1 & value) != 0x0L) {
                this.m_bitBuffer |= 1 << 8 - this.m_bitCursor;
            }
            if (this.m_bitCursor == 8) {
                this.m_stream.write(this.m_bitBuffer);
                ++this.m_offset;
                this.m_bitCursor = 0;
                this.m_bitBuffer = 0;
            }
        }
    }
    
    public void writeBytes(final byte[] buffer, final int offset, final int length) throws IOException {
        this.align();
        if (buffer == null) {
            return;
        }
        this.m_stream.write(buffer, offset, length);
        this.m_offset += length;
    }
}
