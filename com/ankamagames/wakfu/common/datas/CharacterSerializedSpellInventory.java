package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedSpellInventory extends CharacterSerializedPart implements VersionableObject
{
    public short spellInventoryVersion;
    public int lockedSpellId;
    public final RawSpellLevelInventory spellInventory;
    public boolean needSpellRestat;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedSpellInventory() {
        super();
        this.spellInventoryVersion = 0;
        this.lockedSpellId = 0;
        this.spellInventory = new RawSpellLevelInventory();
        this.needSpellRestat = false;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedSpellInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedSpellInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedSpellInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedSpellInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedSpellInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedSpellInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedSpellInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(this.spellInventoryVersion);
        buffer.putInt(this.lockedSpellId);
        final boolean spellInventory_ok = this.spellInventory.serialize(buffer);
        if (!spellInventory_ok) {
            return false;
        }
        buffer.put((byte)(this.needSpellRestat ? 1 : 0));
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.spellInventoryVersion = buffer.getShort();
        this.lockedSpellId = buffer.getInt();
        final boolean spellInventory_ok = this.spellInventory.unserialize(buffer);
        if (!spellInventory_ok) {
            return false;
        }
        this.needSpellRestat = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public void clear() {
        this.spellInventoryVersion = 0;
        this.lockedSpellId = 0;
        this.spellInventory.clear();
        this.needSpellRestat = false;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10031001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedSpellInventoryConverter converter = new CharacterSerializedSpellInventoryConverter();
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
        size += 2;
        size += 4;
        size += this.spellInventory.serializedSize();
        return ++size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("spellInventoryVersion=").append(this.spellInventoryVersion).append('\n');
        repr.append(prefix).append("lockedSpellId=").append(this.lockedSpellId).append('\n');
        repr.append(prefix).append("spellInventory=...\n");
        this.spellInventory.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("needSpellRestat=").append(this.needSpellRestat).append('\n');
    }
    
    private final class CharacterSerializedSpellInventoryConverter
    {
        private short spellInventoryVersion;
        private int lockedSpellId;
        private final RawSpellLevelInventory spellInventory;
        private boolean needSpellRestat;
        
        private CharacterSerializedSpellInventoryConverter() {
            super();
            this.spellInventoryVersion = 0;
            this.lockedSpellId = 0;
            this.spellInventory = new RawSpellLevelInventory();
            this.needSpellRestat = false;
        }
        
        public void pushResult() {
            CharacterSerializedSpellInventory.this.spellInventoryVersion = this.spellInventoryVersion;
            CharacterSerializedSpellInventory.this.lockedSpellId = this.lockedSpellId;
            CharacterSerializedSpellInventory.this.spellInventory.contents.clear();
            CharacterSerializedSpellInventory.this.spellInventory.contents.ensureCapacity(this.spellInventory.contents.size());
            CharacterSerializedSpellInventory.this.spellInventory.contents.addAll(this.spellInventory.contents);
            CharacterSerializedSpellInventory.this.needSpellRestat = this.needSpellRestat;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean spellInventory_ok = this.spellInventory.unserializeVersion(buffer, 1);
            return spellInventory_ok;
        }
        
        private boolean unserialize_v10006(final ByteBuffer buffer) {
            this.spellInventoryVersion = buffer.getShort();
            final boolean spellInventory_ok = this.spellInventory.unserializeVersion(buffer, 10006);
            return spellInventory_ok;
        }
        
        private boolean unserialize_v10008(final ByteBuffer buffer) {
            this.spellInventoryVersion = buffer.getShort();
            this.lockedSpellId = buffer.getInt();
            final boolean spellInventory_ok = this.spellInventory.unserializeVersion(buffer, 10008);
            return spellInventory_ok;
        }
        
        public void convert_v1_to_v10006() {
        }
        
        public void convert_v10006_to_v10008() {
        }
        
        public void convert_v10008_to_v10031001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version < 10006) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10006();
                    this.convert_v10006_to_v10008();
                    this.convert_v10008_to_v10031001();
                    return true;
                }
                return false;
            }
            else if (version < 10008) {
                final boolean ok = this.unserialize_v10006(buffer);
                if (ok) {
                    this.convert_v10006_to_v10008();
                    this.convert_v10008_to_v10031001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10031001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10008(buffer);
                if (ok) {
                    this.convert_v10008_to_v10031001();
                    return true;
                }
                return false;
            }
        }
    }
}
