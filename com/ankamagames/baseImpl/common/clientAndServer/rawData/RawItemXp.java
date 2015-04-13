package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawItemXp implements VersionableObject
{
    public int definitionId;
    public long xp;
    public static final int SERIALIZED_SIZE = 12;
    
    public RawItemXp() {
        super();
        this.definitionId = 0;
        this.xp = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.definitionId);
        buffer.putLong(this.xp);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.definitionId = buffer.getInt();
        this.xp = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.definitionId = 0;
        this.xp = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 12;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("definitionId=").append(this.definitionId).append('\n');
        repr.append(prefix).append("xp=").append(this.xp).append('\n');
    }
}
