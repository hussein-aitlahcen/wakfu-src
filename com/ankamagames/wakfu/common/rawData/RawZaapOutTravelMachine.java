package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawZaapOutTravelMachine implements VersionableObject
{
    public long guildId;
    public static final int SERIALIZED_SIZE = 8;
    
    public RawZaapOutTravelMachine() {
        super();
        this.guildId = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.guildId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.guildId = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.guildId = 0L;
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
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
    }
}
