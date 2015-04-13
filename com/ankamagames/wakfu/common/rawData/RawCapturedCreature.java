package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawCapturedCreature implements VersionableObject
{
    public byte index;
    public short typeId;
    public short quantity;
    public String name;
    
    public RawCapturedCreature() {
        super();
        this.index = 0;
        this.typeId = 0;
        this.quantity = 0;
        this.name = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.index);
        buffer.putShort(this.typeId);
        buffer.putShort(this.quantity);
        if (this.name != null) {
            final byte[] serialized_name = StringUtils.toUTF8(this.name);
            if (serialized_name.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_name.length);
            buffer.put(serialized_name);
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.index = buffer.get();
        this.typeId = buffer.getShort();
        this.quantity = buffer.getShort();
        final int name_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_name = new byte[name_size];
        buffer.get(serialized_name);
        this.name = StringUtils.fromUTF8(serialized_name);
        return true;
    }
    
    @Override
    public void clear() {
        this.index = 0;
        this.typeId = 0;
        this.quantity = 0;
        this.name = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10013) {
            return this.unserialize(buffer);
        }
        final RawCapturedCreatureConverter converter = new RawCapturedCreatureConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        ++size;
        size += 2;
        size += 2;
        size += 2;
        size += ((this.name != null) ? StringUtils.toUTF8(this.name).length : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("index=").append(this.index).append('\n');
        repr.append(prefix).append("typeId=").append(this.typeId).append('\n');
        repr.append(prefix).append("quantity=").append(this.quantity).append('\n');
        repr.append(prefix).append("name=").append(this.name).append('\n');
    }
    
    private final class RawCapturedCreatureConverter
    {
        private byte index;
        private short typeId;
        private short quantity;
        private String name;
        
        private RawCapturedCreatureConverter() {
            super();
            this.index = 0;
            this.typeId = 0;
            this.quantity = 0;
            this.name = null;
        }
        
        public void pushResult() {
            RawCapturedCreature.this.index = this.index;
            RawCapturedCreature.this.typeId = this.typeId;
            RawCapturedCreature.this.quantity = this.quantity;
            RawCapturedCreature.this.name = this.name;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.index = buffer.get();
            this.typeId = buffer.getShort();
            this.quantity = buffer.getShort();
            return true;
        }
        
        public void convert_v1_to_v10013() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10013) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10013();
                return true;
            }
            return false;
        }
    }
}
