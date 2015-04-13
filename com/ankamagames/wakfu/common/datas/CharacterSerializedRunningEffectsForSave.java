package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedRunningEffectsForSave extends CharacterSerializedPart implements VersionableObject
{
    public final RawStateRunningEffects stateRunningEffects;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedRunningEffectsForSave() {
        super();
        this.stateRunningEffects = new RawStateRunningEffects();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedRunningEffectsForSave.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedRunningEffectsForSave");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedRunningEffectsForSave", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedRunningEffectsForSave.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRunningEffectsForSave");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRunningEffectsForSave", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedRunningEffectsForSave.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean stateRunningEffects_ok = this.stateRunningEffects.serialize(buffer);
        return stateRunningEffects_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean stateRunningEffects_ok = this.stateRunningEffects.unserialize(buffer);
        return stateRunningEffects_ok;
    }
    
    @Override
    public void clear() {
        this.stateRunningEffects.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10034001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedRunningEffectsForSaveConverter converter = new CharacterSerializedRunningEffectsForSaveConverter();
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
        size += this.stateRunningEffects.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("stateRunningEffects=...\n");
        this.stateRunningEffects.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedRunningEffectsForSaveConverter
    {
        private final RawStateRunningEffects stateRunningEffects;
        
        private CharacterSerializedRunningEffectsForSaveConverter() {
            super();
            this.stateRunningEffects = new RawStateRunningEffects();
        }
        
        public void pushResult() {
            CharacterSerializedRunningEffectsForSave.this.stateRunningEffects.effects.clear();
            CharacterSerializedRunningEffectsForSave.this.stateRunningEffects.effects.ensureCapacity(this.stateRunningEffects.effects.size());
            CharacterSerializedRunningEffectsForSave.this.stateRunningEffects.effects.addAll(this.stateRunningEffects.effects);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean stateRunningEffects_ok = this.stateRunningEffects.unserializeVersion(buffer, 1);
            return stateRunningEffects_ok;
        }
        
        public void convert_v1_to_v10034001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10034001) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10034001();
                return true;
            }
            return false;
        }
    }
}
