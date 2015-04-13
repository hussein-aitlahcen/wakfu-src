package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawCompanionInfo implements VersionableObject
{
    public long xp;
    public static final int SERIALIZED_SIZE = 8;
    
    public RawCompanionInfo() {
        super();
        this.xp = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.xp);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.xp = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.xp = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 8;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("xp=").append(this.xp).append('\n');
    }
}
