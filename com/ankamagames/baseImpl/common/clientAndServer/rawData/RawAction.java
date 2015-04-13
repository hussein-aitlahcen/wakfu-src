package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.nio.*;

public class RawAction implements VersionableObject
{
    public SpawnedCharacter spawnedCharacter;
    
    public RawAction() {
        super();
        this.spawnedCharacter = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.spawnedCharacter != null) {
            buffer.put((byte)1);
            final boolean spawnedCharacter_ok = this.spawnedCharacter.serialize(buffer);
            if (!spawnedCharacter_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean spawnedCharacter_present = buffer.get() == 1;
        if (spawnedCharacter_present) {
            this.spawnedCharacter = new SpawnedCharacter();
            final boolean spawnedCharacter_ok = this.spawnedCharacter.unserialize(buffer);
            if (!spawnedCharacter_ok) {
                return false;
            }
        }
        else {
            this.spawnedCharacter = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.spawnedCharacter = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final RawActionConverter converter = new RawActionConverter();
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
        if (this.spawnedCharacter != null) {
            size += this.spawnedCharacter.serializedSize();
        }
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("spawnedCharacter=");
        if (this.spawnedCharacter == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.spawnedCharacter.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class SpawnedCharacter implements VersionableObject
    {
        public byte[] serializedCharacter;
        
        public SpawnedCharacter() {
            super();
            this.serializedCharacter = null;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.serializedCharacter != null) {
                if (this.serializedCharacter.length > 65535) {
                    return false;
                }
                buffer.putShort((short)this.serializedCharacter.length);
                buffer.put(this.serializedCharacter);
            }
            else {
                buffer.putShort((short)0);
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int serializedCharacter_size = buffer.getShort() & 0xFFFF;
            if (serializedCharacter_size > 0) {
                buffer.get(this.serializedCharacter = new byte[serializedCharacter_size]);
            }
            else {
                this.serializedCharacter = null;
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.serializedCharacter = null;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            size += ((this.serializedCharacter != null) ? this.serializedCharacter.length : 0);
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("serializedCharacter=(").append((this.serializedCharacter != null) ? this.serializedCharacter.length : 0).append(" bytes)\n");
        }
    }
    
    private final class RawActionConverter
    {
        private SpawnedCharacter spawnedCharacter;
        
        private RawActionConverter() {
            super();
            this.spawnedCharacter = null;
        }
        
        public void pushResult() {
            RawAction.this.spawnedCharacter = this.spawnedCharacter;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 1) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v1();
                return true;
            }
            return false;
        }
    }
}
