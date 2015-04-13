package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawDimensionalBagResource implements VersionableObject
{
    public short referenceId;
    public byte step;
    public byte x;
    public byte y;
    public byte z;
    public static final int SERIALIZED_SIZE = 6;
    
    public RawDimensionalBagResource() {
        super();
        this.referenceId = 0;
        this.step = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.referenceId);
        buffer.put(this.step);
        buffer.put(this.x);
        buffer.put(this.y);
        buffer.put(this.z);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.referenceId = buffer.getShort();
        this.step = buffer.get();
        this.x = buffer.get();
        this.y = buffer.get();
        this.z = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.referenceId = 0;
        this.step = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
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
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("step=").append(this.step).append('\n');
        repr.append(prefix).append("x=").append(this.x).append('\n');
        repr.append(prefix).append("y=").append(this.y).append('\n');
        repr.append(prefix).append("z=").append(this.z).append('\n');
    }
}
