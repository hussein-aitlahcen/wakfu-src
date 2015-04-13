package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawRentInfo implements VersionableObject
{
    public int type;
    public long duration;
    public long count;
    public static final int SERIALIZED_SIZE = 20;
    
    public RawRentInfo() {
        super();
        this.type = 0;
        this.duration = 0L;
        this.count = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.type);
        buffer.putLong(this.duration);
        buffer.putLong(this.count);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.type = buffer.getInt();
        this.duration = buffer.getLong();
        this.count = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.type = 0;
        this.duration = 0L;
        this.count = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 20;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("type=").append(this.type).append('\n');
        repr.append(prefix).append("duration=").append(this.duration).append('\n');
        repr.append(prefix).append("count=").append(this.count).append('\n');
    }
}
