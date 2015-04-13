package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedCurrentMovementPath extends CharacterSerializedPart implements VersionableObject
{
    public CurrentPath currentPath;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedCurrentMovementPath() {
        super();
        this.currentPath = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedCurrentMovementPath.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedCurrentMovementPath");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedCurrentMovementPath", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedCurrentMovementPath.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCurrentMovementPath");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCurrentMovementPath", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedCurrentMovementPath.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.currentPath != null) {
            buffer.put((byte)1);
            final boolean currentPath_ok = this.currentPath.serialize(buffer);
            if (!currentPath_ok) {
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
        final boolean currentPath_present = buffer.get() == 1;
        if (currentPath_present) {
            this.currentPath = new CurrentPath();
            final boolean currentPath_ok = this.currentPath.unserialize(buffer);
            if (!currentPath_ok) {
                return false;
            }
        }
        else {
            this.currentPath = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.currentPath = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedCurrentMovementPathConverter converter = new CharacterSerializedCurrentMovementPathConverter();
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
        if (this.currentPath != null) {
            size += this.currentPath.serializedSize();
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
        repr.append(prefix).append("currentPath=");
        if (this.currentPath == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.currentPath.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class CurrentPath implements VersionableObject
    {
        public byte[] encodedPath;
        
        public CurrentPath() {
            super();
            this.encodedPath = null;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.encodedPath != null) {
                if (this.encodedPath.length > 255) {
                    return false;
                }
                buffer.put((byte)this.encodedPath.length);
                buffer.put(this.encodedPath);
            }
            else {
                buffer.put((byte)0);
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int encodedPath_size = buffer.get() & 0xFF;
            if (encodedPath_size > 0) {
                buffer.get(this.encodedPath = new byte[encodedPath_size]);
            }
            else {
                this.encodedPath = null;
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.encodedPath = null;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size = ++size + ((this.encodedPath != null) ? this.encodedPath.length : 0);
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("encodedPath=(").append((this.encodedPath != null) ? this.encodedPath.length : 0).append(" bytes)\n");
        }
    }
    
    private final class CharacterSerializedCurrentMovementPathConverter
    {
        private CurrentPath currentPath;
        
        private CharacterSerializedCurrentMovementPathConverter() {
            super();
            this.currentPath = null;
        }
        
        public void pushResult() {
            CharacterSerializedCurrentMovementPath.this.currentPath = this.currentPath;
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
