package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawShortcut implements VersionableObject
{
    public byte type;
    public long targetUniqueId;
    public int targetReferenceId;
    public int targetGfxId;
    public static final int SERIALIZED_SIZE = 17;
    
    public RawShortcut() {
        super();
        this.type = 0;
        this.targetUniqueId = 0L;
        this.targetReferenceId = 0;
        this.targetGfxId = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.type);
        buffer.putLong(this.targetUniqueId);
        buffer.putInt(this.targetReferenceId);
        buffer.putInt(this.targetGfxId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.type = buffer.get();
        this.targetUniqueId = buffer.getLong();
        this.targetReferenceId = buffer.getInt();
        this.targetGfxId = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.type = 0;
        this.targetUniqueId = 0L;
        this.targetReferenceId = 0;
        this.targetGfxId = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 17;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("type=").append(this.type).append('\n');
        repr.append(prefix).append("targetUniqueId=").append(this.targetUniqueId).append('\n');
        repr.append(prefix).append("targetReferenceId=").append(this.targetReferenceId).append('\n');
        repr.append(prefix).append("targetGfxId=").append(this.targetGfxId).append('\n');
    }
}
