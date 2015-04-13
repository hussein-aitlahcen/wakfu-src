package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawItemizableSynchronisationData implements VersionableObject
{
    public int positionX;
    public int positionY;
    public short positionZ;
    public byte direction;
    public long ownerId;
    public static final int SERIALIZED_SIZE = 19;
    
    public RawItemizableSynchronisationData() {
        super();
        this.positionX = 0;
        this.positionY = 0;
        this.positionZ = 0;
        this.direction = 0;
        this.ownerId = 0L;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.positionX);
        buffer.putInt(this.positionY);
        buffer.putShort(this.positionZ);
        buffer.put(this.direction);
        buffer.putLong(this.ownerId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.positionX = buffer.getInt();
        this.positionY = buffer.getInt();
        this.positionZ = buffer.getShort();
        this.direction = buffer.get();
        this.ownerId = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.positionX = 0;
        this.positionY = 0;
        this.positionZ = 0;
        this.direction = 0;
        this.ownerId = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 19;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("positionX=").append(this.positionX).append('\n');
        repr.append(prefix).append("positionY=").append(this.positionY).append('\n');
        repr.append(prefix).append("positionZ=").append(this.positionZ).append('\n');
        repr.append(prefix).append("direction=").append(this.direction).append('\n');
        repr.append(prefix).append("ownerId=").append(this.ownerId).append('\n');
    }
}
