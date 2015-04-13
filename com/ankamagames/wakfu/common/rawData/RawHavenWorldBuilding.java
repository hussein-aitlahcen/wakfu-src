package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawHavenWorldBuilding implements VersionableObject
{
    public long uid;
    public long creationDate;
    public short id;
    public short x;
    public short y;
    public int equippedSkinItemId;
    public static final int SERIALIZED_SIZE = 26;
    
    public RawHavenWorldBuilding() {
        super();
        this.uid = 0L;
        this.creationDate = 0L;
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.equippedSkinItemId = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.uid);
        buffer.putLong(this.creationDate);
        buffer.putShort(this.id);
        buffer.putShort(this.x);
        buffer.putShort(this.y);
        buffer.putInt(this.equippedSkinItemId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.uid = buffer.getLong();
        this.creationDate = buffer.getLong();
        this.id = buffer.getShort();
        this.x = buffer.getShort();
        this.y = buffer.getShort();
        this.equippedSkinItemId = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.uid = 0L;
        this.creationDate = 0L;
        this.id = 0;
        this.x = 0;
        this.y = 0;
        this.equippedSkinItemId = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 26;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("uid=").append(this.uid).append('\n');
        repr.append(prefix).append("creationDate=").append(this.creationDate).append('\n');
        repr.append(prefix).append("id=").append(this.id).append('\n');
        repr.append(prefix).append("x=").append(this.x).append('\n');
        repr.append(prefix).append("y=").append(this.y).append('\n');
        repr.append(prefix).append("equippedSkinItemId=").append(this.equippedSkinItemId).append('\n');
    }
}
