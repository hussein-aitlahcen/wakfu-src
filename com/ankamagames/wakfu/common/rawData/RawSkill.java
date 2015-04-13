package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawSkill implements VersionableObject
{
    public int referenceId;
    public short level;
    public long xp;
    public static final int SERIALIZED_SIZE = 14;
    
    public RawSkill() {
        super();
        this.referenceId = 0;
        this.level = 0;
        this.xp = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.referenceId);
        buffer.putShort(this.level);
        buffer.putLong(this.xp);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.referenceId = buffer.getInt();
        this.level = buffer.getShort();
        this.xp = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.referenceId = 0;
        this.level = 0;
        this.xp = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 14;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("level=").append(this.level).append('\n');
        repr.append(prefix).append("xp=").append(this.xp).append('\n');
    }
}
