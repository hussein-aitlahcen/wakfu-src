package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawImageCharacteristic implements VersionableObject
{
    public int gfxId;
    public byte sex;
    public final RawCharacteristics imageCharac;
    
    public RawImageCharacteristic() {
        super();
        this.gfxId = 0;
        this.sex = 0;
        this.imageCharac = new RawCharacteristics();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.gfxId);
        buffer.put(this.sex);
        final boolean imageCharac_ok = this.imageCharac.serialize(buffer);
        return imageCharac_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.gfxId = buffer.getInt();
        this.sex = buffer.get();
        final boolean imageCharac_ok = this.imageCharac.unserialize(buffer);
        return imageCharac_ok;
    }
    
    @Override
    public void clear() {
        this.gfxId = 0;
        this.sex = 0;
        this.imageCharac.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final RawImageCharacteristicConverter converter = new RawImageCharacteristicConverter();
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
        size += 4;
        size = ++size + this.imageCharac.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("gfxId=").append(this.gfxId).append('\n');
        repr.append(prefix).append("sex=").append(this.sex).append('\n');
        repr.append(prefix).append("imageCharac=...\n");
        this.imageCharac.internalToString(repr, prefix + "  ");
    }
    
    private final class RawImageCharacteristicConverter
    {
        private int gfxId;
        private byte sex;
        private final RawCharacteristics imageCharac;
        
        private RawImageCharacteristicConverter() {
            super();
            this.gfxId = 0;
            this.sex = 0;
            this.imageCharac = new RawCharacteristics();
        }
        
        public void pushResult() {
            RawImageCharacteristic.this.gfxId = this.gfxId;
            RawImageCharacteristic.this.sex = this.sex;
            RawImageCharacteristic.this.imageCharac.characteristics.clear();
            RawImageCharacteristic.this.imageCharac.characteristics.ensureCapacity(this.imageCharac.characteristics.size());
            RawImageCharacteristic.this.imageCharac.characteristics.addAll(this.imageCharac.characteristics);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.gfxId = buffer.getInt();
            this.sex = buffer.get();
            final boolean imageCharac_ok = this.imageCharac.unserializeVersion(buffer, 1);
            return imageCharac_ok;
        }
        
        public void convert_v1_to_v10036003() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10036003) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10036003();
                return true;
            }
            return false;
        }
    }
}
