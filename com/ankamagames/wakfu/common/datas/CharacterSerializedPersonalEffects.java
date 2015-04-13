package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedPersonalEffects extends CharacterSerializedPart implements VersionableObject
{
    public int[] guildEffects;
    public int[] havenWorldEffects;
    public int[] antiAddictionEffects;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedPersonalEffects() {
        super();
        this.guildEffects = null;
        this.havenWorldEffects = null;
        this.antiAddictionEffects = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedPersonalEffects.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedPersonalEffects");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedPersonalEffects", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedPersonalEffects.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPersonalEffects");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPersonalEffects", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedPersonalEffects.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.guildEffects != null) {
            if (this.guildEffects.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.guildEffects.length);
            for (int i = 0; i < this.guildEffects.length; ++i) {
                buffer.putInt(this.guildEffects[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.havenWorldEffects != null) {
            if (this.havenWorldEffects.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.havenWorldEffects.length);
            for (int i = 0; i < this.havenWorldEffects.length; ++i) {
                buffer.putInt(this.havenWorldEffects[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.antiAddictionEffects != null) {
            if (this.antiAddictionEffects.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.antiAddictionEffects.length);
            for (int i = 0; i < this.antiAddictionEffects.length; ++i) {
                buffer.putInt(this.antiAddictionEffects[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int guildEffects_size = buffer.getShort() & 0xFFFF;
        if (guildEffects_size > 0) {
            this.guildEffects = new int[guildEffects_size];
            for (int i = 0; i < guildEffects_size; ++i) {
                this.guildEffects[i] = buffer.getInt();
            }
        }
        else {
            this.guildEffects = null;
        }
        final int havenWorldEffects_size = buffer.getShort() & 0xFFFF;
        if (havenWorldEffects_size > 0) {
            this.havenWorldEffects = new int[havenWorldEffects_size];
            for (int j = 0; j < havenWorldEffects_size; ++j) {
                this.havenWorldEffects[j] = buffer.getInt();
            }
        }
        else {
            this.havenWorldEffects = null;
        }
        final int antiAddictionEffects_size = buffer.getShort() & 0xFFFF;
        if (antiAddictionEffects_size > 0) {
            this.antiAddictionEffects = new int[antiAddictionEffects_size];
            for (int k = 0; k < antiAddictionEffects_size; ++k) {
                this.antiAddictionEffects[k] = buffer.getInt();
            }
        }
        else {
            this.antiAddictionEffects = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.guildEffects = null;
        this.havenWorldEffects = null;
        this.antiAddictionEffects = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10026000) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedPersonalEffectsConverter converter = new CharacterSerializedPersonalEffectsConverter();
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
        size += ((this.guildEffects != null) ? (this.guildEffects.length * 4) : 0);
        size += 2;
        size += ((this.havenWorldEffects != null) ? (this.havenWorldEffects.length * 4) : 0);
        size += 2;
        size += ((this.antiAddictionEffects != null) ? (this.antiAddictionEffects.length * 4) : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guildEffects=(").append(this.guildEffects.length).append(" bytes)\n");
        repr.append(prefix).append("havenWorldEffects=(").append(this.havenWorldEffects.length).append(" bytes)\n");
        repr.append(prefix).append("antiAddictionEffects=(").append(this.antiAddictionEffects.length).append(" bytes)\n");
    }
    
    private final class CharacterSerializedPersonalEffectsConverter
    {
        private int[] guildEffects;
        private int[] havenWorldEffects;
        private int[] antiAddictionEffects;
        
        private CharacterSerializedPersonalEffectsConverter() {
            super();
            this.guildEffects = null;
            this.havenWorldEffects = null;
            this.antiAddictionEffects = null;
        }
        
        public void pushResult() {
            CharacterSerializedPersonalEffects.this.guildEffects = this.guildEffects;
            CharacterSerializedPersonalEffects.this.havenWorldEffects = this.havenWorldEffects;
            CharacterSerializedPersonalEffects.this.antiAddictionEffects = this.antiAddictionEffects;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int guildEffects_size = buffer.getShort() & 0xFFFF;
            if (guildEffects_size > 0) {
                this.guildEffects = new int[guildEffects_size];
                for (int i = 0; i < guildEffects_size; ++i) {
                    this.guildEffects[i] = buffer.getInt();
                }
            }
            else {
                this.guildEffects = null;
            }
            final int havenWorldEffects_size = buffer.getShort() & 0xFFFF;
            if (havenWorldEffects_size > 0) {
                this.havenWorldEffects = new int[havenWorldEffects_size];
                for (int j = 0; j < havenWorldEffects_size; ++j) {
                    this.havenWorldEffects[j] = buffer.getInt();
                }
            }
            else {
                this.havenWorldEffects = null;
            }
            return true;
        }
        
        public void convert_v1_to_v10026000() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10026000) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10026000();
                return true;
            }
            return false;
        }
    }
}
