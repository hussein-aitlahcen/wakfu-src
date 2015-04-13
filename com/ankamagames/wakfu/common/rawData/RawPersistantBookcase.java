package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawPersistantBookcase extends AbstractRawPersistantData implements VersionableObject
{
    public final RawBookcase content;
    public static final byte VIRTUAL_ID = 3;
    
    public RawPersistantBookcase() {
        super();
        this.content = new RawBookcase();
    }
    
    @Override
    public byte getVirtualId() {
        return 3;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean content_ok = this.content.serialize(buffer);
        return content_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean content_ok = this.content.unserialize(buffer);
        return content_ok;
    }
    
    @Override
    public void clear() {
        this.content.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += this.content.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    @Override
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("content=...\n");
        this.content.internalToString(repr, prefix + "  ");
    }
}
