package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedLocalGuildInfo extends CharacterSerializedPart implements VersionableObject
{
    public byte[] guild;
    public int havenWorldId;
    public float moderationBonusLearningFactor;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedLocalGuildInfo() {
        super();
        this.guild = null;
        this.havenWorldId = 0;
        this.moderationBonusLearningFactor = 0.0f;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedLocalGuildInfo.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedLocalGuildInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedLocalGuildInfo", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedLocalGuildInfo.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocalGuildInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocalGuildInfo", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedLocalGuildInfo.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.guild != null) {
            if (this.guild.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.guild.length);
            buffer.put(this.guild);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.havenWorldId);
        buffer.putFloat(this.moderationBonusLearningFactor);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int guild_size = buffer.getShort() & 0xFFFF;
        if (guild_size > 0) {
            buffer.get(this.guild = new byte[guild_size]);
        }
        else {
            this.guild = null;
        }
        this.havenWorldId = buffer.getInt();
        this.moderationBonusLearningFactor = buffer.getFloat();
        return true;
    }
    
    @Override
    public void clear() {
        this.guild = null;
        this.havenWorldId = 0;
        this.moderationBonusLearningFactor = 0.0f;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.guild != null) ? this.guild.length : 0);
        size += 4;
        size += 4;
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guild=(").append((this.guild != null) ? this.guild.length : 0).append(" bytes)\n");
        repr.append(prefix).append("havenWorldId=").append(this.havenWorldId).append('\n');
        repr.append(prefix).append("moderationBonusLearningFactor=").append(this.moderationBonusLearningFactor).append('\n');
    }
}
