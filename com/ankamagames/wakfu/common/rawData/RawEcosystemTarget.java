package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawEcosystemTarget implements VersionableObject
{
    public int referenceId;
    public int min;
    public int max;
    public static final int SERIALIZED_SIZE = 12;
    
    public RawEcosystemTarget() {
        super();
        this.referenceId = 0;
        this.min = 0;
        this.max = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.referenceId);
        buffer.putInt(this.min);
        buffer.putInt(this.max);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.referenceId = buffer.getInt();
        this.min = buffer.getInt();
        this.max = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.referenceId = 0;
        this.min = 0;
        this.max = 0;
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
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("min=").append(this.min).append('\n');
        repr.append(prefix).append("max=").append(this.max).append('\n');
    }
}
