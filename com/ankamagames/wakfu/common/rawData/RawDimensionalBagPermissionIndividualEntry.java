package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawDimensionalBagPermissionIndividualEntry implements VersionableObject
{
    public long userId;
    public String userName;
    public byte rights;
    
    public RawDimensionalBagPermissionIndividualEntry() {
        super();
        this.userId = 0L;
        this.userName = null;
        this.rights = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.userId);
        if (this.userName != null) {
            final byte[] serialized_userName = StringUtils.toUTF8(this.userName);
            if (serialized_userName.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_userName.length);
            buffer.put(serialized_userName);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.put(this.rights);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.userId = buffer.getLong();
        final int userName_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_userName = new byte[userName_size];
        buffer.get(serialized_userName);
        this.userName = StringUtils.fromUTF8(serialized_userName);
        this.rights = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.userId = 0L;
        this.userName = null;
        this.rights = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 8;
        size += 2;
        size += ((this.userName != null) ? StringUtils.toUTF8(this.userName).length : 0);
        return ++size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("userId=").append(this.userId).append('\n');
        repr.append(prefix).append("userName=").append(this.userName).append('\n');
        repr.append(prefix).append("rights=").append(this.rights).append('\n');
    }
}
