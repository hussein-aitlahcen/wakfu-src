package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawCategoryAvailablePoints implements VersionableObject
{
    public int categoryId;
    public short availablePoints;
    public static final int SERIALIZED_SIZE = 6;
    
    public RawCategoryAvailablePoints() {
        super();
        this.categoryId = 0;
        this.availablePoints = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.categoryId);
        buffer.putShort(this.availablePoints);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.categoryId = buffer.getInt();
        this.availablePoints = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.categoryId = 0;
        this.availablePoints = 0;
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
        repr.append(prefix).append("categoryId=").append(this.categoryId).append('\n');
        repr.append(prefix).append("availablePoints=").append(this.availablePoints).append('\n');
    }
}
