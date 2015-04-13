package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawGemControlledRoom extends RawSpecificRooms implements VersionableObject
{
    public boolean primaryGemLocked;
    public int primaryGemitemRefId;
    public long primaryGemUniqueId;
    public int secondaryGemitemRefId;
    public long secondaryGemUniqueId;
    public static final byte VIRTUAL_ID = 0;
    public static final int SERIALIZED_SIZE = 25;
    
    public RawGemControlledRoom() {
        super();
        this.primaryGemLocked = false;
        this.primaryGemitemRefId = 0;
        this.primaryGemUniqueId = 0L;
        this.secondaryGemitemRefId = 0;
        this.secondaryGemUniqueId = 0L;
    }
    
    @Override
    public byte getVirtualId() {
        return 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put((byte)(this.primaryGemLocked ? 1 : 0));
        buffer.putInt(this.primaryGemitemRefId);
        buffer.putLong(this.primaryGemUniqueId);
        buffer.putInt(this.secondaryGemitemRefId);
        buffer.putLong(this.secondaryGemUniqueId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.primaryGemLocked = (buffer.get() == 1);
        this.primaryGemitemRefId = buffer.getInt();
        this.primaryGemUniqueId = buffer.getLong();
        this.secondaryGemitemRefId = buffer.getInt();
        this.secondaryGemUniqueId = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.primaryGemLocked = false;
        this.primaryGemitemRefId = 0;
        this.primaryGemUniqueId = 0L;
        this.secondaryGemitemRefId = 0;
        this.secondaryGemUniqueId = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 25;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    @Override
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("primaryGemLocked=").append(this.primaryGemLocked).append('\n');
        repr.append(prefix).append("primaryGemitemRefId=").append(this.primaryGemitemRefId).append('\n');
        repr.append(prefix).append("primaryGemUniqueId=").append(this.primaryGemUniqueId).append('\n');
        repr.append(prefix).append("secondaryGemitemRefId=").append(this.secondaryGemitemRefId).append('\n');
        repr.append(prefix).append("secondaryGemUniqueId=").append(this.secondaryGemUniqueId).append('\n');
    }
}
