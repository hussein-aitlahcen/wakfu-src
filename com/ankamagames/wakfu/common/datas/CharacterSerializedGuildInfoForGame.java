package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedGuildInfoForGame extends CharacterSerializedPart implements VersionableObject
{
    public long guildId;
    public long authorisations;
    public int[] activeBuffs;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedGuildInfoForGame() {
        super();
        this.guildId = 0L;
        this.authorisations = 0L;
        this.activeBuffs = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedGuildInfoForGame.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedGuildInfoForGame");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedGuildInfoForGame", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedGuildInfoForGame.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGuildInfoForGame");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGuildInfoForGame", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedGuildInfoForGame.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.guildId);
        buffer.putLong(this.authorisations);
        if (this.activeBuffs != null) {
            if (this.activeBuffs.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.activeBuffs.length);
            for (int i = 0; i < this.activeBuffs.length; ++i) {
                buffer.putInt(this.activeBuffs[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.guildId = buffer.getLong();
        this.authorisations = buffer.getLong();
        final int activeBuffs_size = buffer.getShort() & 0xFFFF;
        if (activeBuffs_size > 0) {
            this.activeBuffs = new int[activeBuffs_size];
            for (int i = 0; i < activeBuffs_size; ++i) {
                this.activeBuffs[i] = buffer.getInt();
            }
        }
        else {
            this.activeBuffs = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.guildId = 0L;
        this.authorisations = 0L;
        this.activeBuffs = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 8;
        size += 8;
        size += 2;
        size += ((this.activeBuffs != null) ? (this.activeBuffs.length * 4) : 0);
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
        repr.append(prefix).append("authorisations=").append(this.authorisations).append('\n');
        repr.append(prefix).append("activeBuffs=(").append(this.activeBuffs.length).append(" bytes)\n");
    }
}
