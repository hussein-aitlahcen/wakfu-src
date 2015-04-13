package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawGiftItem implements VersionableObject
{
    public String guid;
    public int referenceId;
    public short quantity;
    
    public RawGiftItem() {
        super();
        this.guid = null;
        this.referenceId = 0;
        this.quantity = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.guid != null) {
            final byte[] serialized_guid = StringUtils.toUTF8(this.guid);
            if (serialized_guid.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_guid.length);
            buffer.put(serialized_guid);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.referenceId);
        buffer.putShort(this.quantity);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int guid_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_guid = new byte[guid_size];
        buffer.get(serialized_guid);
        this.guid = StringUtils.fromUTF8(serialized_guid);
        this.referenceId = buffer.getInt();
        this.quantity = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.guid = null;
        this.referenceId = 0;
        this.quantity = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.guid != null) ? StringUtils.toUTF8(this.guid).length : 0);
        size += 4;
        size += 2;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guid=").append(this.guid).append('\n');
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("quantity=").append(this.quantity).append('\n');
    }
}
