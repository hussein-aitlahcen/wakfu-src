package com.ankamagames.framework.kernel.core.net.netty.decoder;

import io.netty.buffer.*;
import java.nio.*;

public enum ProtocolAdapter
{
    CLIENT_SERVER {
        @Override
        public ByteBuffer adapt(final ByteBuf buf) {
            final int msgSize = buf.readShort() & 0xFFFF;
            final byte[] rawData = new byte[msgSize - 2];
            buf.readBytes(rawData);
            final ByteBuffer bb = ByteBuffer.allocate(msgSize);
            bb.putShort((short)msgSize);
            bb.put(rawData);
            bb.rewind();
            return bb;
        }
    }, 
    SERVER_PROXY {
        @Override
        public ByteBuffer adapt(final ByteBuf buf) {
            final int msgSize = buf.readShort() & 0xFFFF;
            final short msgType = buf.readShort();
            final byte[] rawData = new byte[msgSize - 4];
            buf.readBytes(rawData);
            final ByteBuffer bb = ByteBuffer.allocate(msgSize);
            bb.putShort((short)msgSize);
            bb.putShort(msgType);
            bb.put(rawData);
            bb.rewind();
            return bb;
        }
    }, 
    INTER_SERVER {
        @Override
        public ByteBuffer adapt(final ByteBuf buf) {
            final int msgSize = buf.readInt() & Integer.MAX_VALUE;
            final short msgType = buf.readShort();
            final byte[] rawData = new byte[msgSize - 6];
            buf.readBytes(rawData);
            final ByteBuffer bb = ByteBuffer.allocate(msgSize);
            bb.putInt(msgSize);
            bb.putShort(msgType);
            bb.put(rawData);
            bb.rewind();
            return bb;
        }
    };
    
    public abstract ByteBuffer adapt(final ByteBuf p0);
}
