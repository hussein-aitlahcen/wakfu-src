package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;
import org.apache.tools.ant.*;
import java.io.*;

abstract class AnmFrameData
{
    int m_cur;
    
    AnmFrameData() {
        super();
        this.m_cur = 0;
    }
    
    static AnmFrameData create(final ExtendedDataInputStream bitStream) {
        final byte type = bitStream.readByte();
        final int size = bitStream.readInt();
        switch (type) {
            case 1: {
                return new Byte(bitStream.readBytes(size));
            }
            case 2: {
                return new Short(bitStream.readShorts(size));
            }
            case 4: {
                return new Int(bitStream.readInts(size));
            }
            default: {
                return null;
            }
        }
    }
    
    static void write(final OutputBitStream ostream, final TIntArrayList data) throws IOException {
        int max = 0;
        final int count = data.size();
        for (int i = 0; i < count; ++i) {
            if (data.getQuick(i) > max) {
                max = data.getQuick(i);
            }
        }
        if (max < 255) {
            ostream.writeByte((byte)1);
            ostream.writeInt(count);
            for (int i = 0; i < count; ++i) {
                ostream.writeByte((byte)data.getQuick(i));
            }
            return;
        }
        if (max < 65535) {
            ostream.writeByte((byte)2);
            ostream.writeInt(count);
            for (int i = 0; i < count; ++i) {
                ostream.writeShort((short)data.getQuick(i));
            }
            return;
        }
        if (max >= Integer.MAX_VALUE) {
            throw new BuildException("trop de donn\u00e9e");
        }
        ostream.writeByte((byte)4);
        ostream.writeInt(count);
        for (int i = 0; i < count; ++i) {
            ostream.writeInt(data.getQuick(i));
        }
    }
    
    public final void begin(final int offset) {
        this.m_cur = offset;
    }
    
    public abstract int read();
    
    private static class Byte extends AnmFrameData
    {
        final byte[] m_data;
        
        Byte(final byte[] data) {
            super();
            this.m_data = data;
        }
        
        @Override
        public final int read() {
            return this.m_data[this.m_cur++] & 0xFF;
        }
    }
    
    private static class Short extends AnmFrameData
    {
        final short[] m_data;
        
        Short(final short[] data) {
            super();
            this.m_data = data;
        }
        
        @Override
        public final int read() {
            return this.m_data[this.m_cur++] & 0xFFFF;
        }
    }
    
    private static class Int extends AnmFrameData
    {
        final int[] m_data;
        
        Int(final int[] data) {
            super();
            this.m_data = data;
        }
        
        @Override
        public final int read() {
            return this.m_data[this.m_cur++];
        }
    }
}
