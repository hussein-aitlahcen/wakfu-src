package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawDimensionalBagStorageBox implements VersionableObject
{
    public byte[] rawStorageBox;
    
    public RawDimensionalBagStorageBox() {
        super();
        this.rawStorageBox = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.rawStorageBox != null) {
            if (this.rawStorageBox.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.rawStorageBox.length);
            buffer.put(this.rawStorageBox);
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int rawStorageBox_size = buffer.getShort() & 0xFFFF;
        if (rawStorageBox_size > 0) {
            buffer.get(this.rawStorageBox = new byte[rawStorageBox_size]);
        }
        else {
            this.rawStorageBox = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.rawStorageBox = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.rawStorageBox != null) ? this.rawStorageBox.length : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("rawStorageBox=(").append((this.rawStorageBox != null) ? this.rawStorageBox.length : 0).append(" bytes)\n");
    }
}
