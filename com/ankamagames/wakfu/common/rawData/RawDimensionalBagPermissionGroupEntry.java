package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawDimensionalBagPermissionGroupEntry implements VersionableObject
{
    public byte groupType;
    public byte rights;
    public static final int SERIALIZED_SIZE = 2;
    
    public RawDimensionalBagPermissionGroupEntry() {
        super();
        this.groupType = 0;
        this.rights = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.groupType);
        buffer.put(this.rights);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.groupType = buffer.get();
        this.rights = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.groupType = 0;
        this.rights = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 2;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("groupType=").append(this.groupType).append('\n');
        repr.append(prefix).append("rights=").append(this.rights).append('\n');
    }
}
