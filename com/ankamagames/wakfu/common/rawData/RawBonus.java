package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawBonus implements VersionableObject
{
    public int bonusId;
    public short level;
    public static final int SERIALIZED_SIZE = 6;
    
    public RawBonus() {
        super();
        this.bonusId = 0;
        this.level = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.bonusId);
        buffer.putShort(this.level);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.bonusId = buffer.getInt();
        this.level = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.bonusId = 0;
        this.level = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 6;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("bonusId=").append(this.bonusId).append('\n');
        repr.append(prefix).append("level=").append(this.level).append('\n');
    }
}
