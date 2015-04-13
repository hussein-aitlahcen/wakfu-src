package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawPersistantKrosmozGameCollection extends AbstractRawPersistantData implements VersionableObject
{
    public static final byte VIRTUAL_ID = 5;
    public static final int SERIALIZED_SIZE = 0;
    
    @Override
    public byte getVirtualId() {
        return 5;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 0;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    @Override
    public final void internalToString(final StringBuilder repr, final String prefix) {
    }
}
