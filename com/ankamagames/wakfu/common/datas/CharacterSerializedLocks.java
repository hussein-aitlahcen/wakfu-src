package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedLocks extends CharacterSerializedPart implements VersionableObject
{
    public final RawLocks locks;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedLocks() {
        super();
        this.locks = new RawLocks();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedLocks.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedLocks");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedLocks", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedLocks.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocks");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocks", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedLocks.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean locks_ok = this.locks.serialize(buffer);
        return locks_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean locks_ok = this.locks.unserialize(buffer);
        return locks_ok;
    }
    
    @Override
    public void clear() {
        this.locks.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10028001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedLocksConverter converter = new CharacterSerializedLocksConverter();
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
        size += this.locks.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("locks=...\n");
        this.locks.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedLocksConverter
    {
        private final RawLocks locks;
        
        private CharacterSerializedLocksConverter() {
            super();
            this.locks = new RawLocks();
        }
        
        public void pushResult() {
            CharacterSerializedLocks.this.locks.locks.clear();
            CharacterSerializedLocks.this.locks.locks.ensureCapacity(this.locks.locks.size());
            CharacterSerializedLocks.this.locks.locks.addAll(this.locks.locks);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v10021(final ByteBuffer buffer) {
            final boolean locks_ok = this.locks.unserializeVersion(buffer, 10021);
            return locks_ok;
        }
        
        public void convert_v0_to_v10021() {
        }
        
        public void convert_v10021_to_v10028001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 10021) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v10021();
                    this.convert_v10021_to_v10028001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10028001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10021(buffer);
                if (ok) {
                    this.convert_v10021_to_v10028001();
                    return true;
                }
                return false;
            }
        }
    }
}
