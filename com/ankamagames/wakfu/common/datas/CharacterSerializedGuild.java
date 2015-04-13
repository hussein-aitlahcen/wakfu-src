package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedGuild extends CharacterSerializedPart implements VersionableObject
{
    public long guildId;
    public long fightXp;
    public long jobXP;
    public byte rank;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 25;
    
    public CharacterSerializedGuild() {
        super();
        this.guildId = 0L;
        this.fightXp = 0L;
        this.jobXP = 0L;
        this.rank = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedGuild.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedGuild");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedGuild", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedGuild.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGuild");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedGuild", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedGuild.this.serializedSize();
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
        buffer.putLong(this.fightXp);
        buffer.putLong(this.jobXP);
        buffer.put(this.rank);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.guildId = buffer.getLong();
        this.fightXp = buffer.getLong();
        this.jobXP = buffer.getLong();
        this.rank = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.guildId = 0L;
        this.fightXp = 0L;
        this.jobXP = 0L;
        this.rank = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        return 25;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
        repr.append(prefix).append("fightXp=").append(this.fightXp).append('\n');
        repr.append(prefix).append("jobXP=").append(this.jobXP).append('\n');
        repr.append(prefix).append("rank=").append(this.rank).append('\n');
    }
}
