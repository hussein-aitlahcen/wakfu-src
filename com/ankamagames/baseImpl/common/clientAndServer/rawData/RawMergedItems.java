package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawMergedItems implements VersionableObject
{
    public int version;
    public byte[] items;
    
    public RawMergedItems() {
        super();
        this.version = 0;
        this.items = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.version);
        if (this.items != null) {
            if (this.items.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.items.length);
            buffer.put(this.items);
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.version = buffer.getInt();
        final int items_size = buffer.getShort() & 0xFFFF;
        if (items_size > 0) {
            buffer.get(this.items = new byte[items_size]);
        }
        else {
            this.items = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.version = 0;
        this.items = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 4;
        size += 2;
        size += ((this.items != null) ? this.items.length : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("version=").append(this.version).append('\n');
        repr.append(prefix).append("items=(").append((this.items != null) ? this.items.length : 0).append(" bytes)\n");
    }
}
