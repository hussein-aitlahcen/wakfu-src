package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawAptitude implements VersionableObject
{
    public short referenceId;
    public short level;
    public short wonLevel;
    public static final int SERIALIZED_SIZE = 6;
    
    public RawAptitude() {
        super();
        this.referenceId = 0;
        this.level = 0;
        this.wonLevel = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.referenceId);
        buffer.putShort(this.level);
        buffer.putShort(this.wonLevel);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.referenceId = buffer.getShort();
        this.level = buffer.getShort();
        this.wonLevel = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.referenceId = 0;
        this.level = 0;
        this.wonLevel = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10004) {
            return this.unserialize(buffer);
        }
        final RawAptitudeConverter converter = new RawAptitudeConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
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
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("level=").append(this.level).append('\n');
        repr.append(prefix).append("wonLevel=").append(this.wonLevel).append('\n');
    }
    
    private final class RawAptitudeConverter
    {
        private short referenceId;
        private short level;
        private short wonLevel;
        
        private RawAptitudeConverter() {
            super();
            this.referenceId = 0;
            this.level = 0;
            this.wonLevel = 0;
        }
        
        public void pushResult() {
            RawAptitude.this.referenceId = this.referenceId;
            RawAptitude.this.level = this.level;
            RawAptitude.this.wonLevel = this.wonLevel;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.referenceId = buffer.getShort();
            this.level = buffer.getShort();
            return true;
        }
        
        public void convert_v1_to_v10004() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10004) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10004();
                return true;
            }
            return false;
        }
    }
}
