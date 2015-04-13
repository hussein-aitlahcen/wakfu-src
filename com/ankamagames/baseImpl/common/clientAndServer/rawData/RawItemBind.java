package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawItemBind implements VersionableObject
{
    public byte type;
    public long data;
    public static final int SERIALIZED_SIZE = 9;
    
    public RawItemBind() {
        super();
        this.type = 0;
        this.data = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.type);
        buffer.putLong(this.data);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.type = buffer.get();
        this.data = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.type = 0;
        this.data = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 9;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("type=").append(this.type).append('\n');
        repr.append(prefix).append("data=").append(this.data).append('\n');
    }
}
