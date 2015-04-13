package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class CharacterSerializedRemoteGuildInfo extends CharacterSerializedPart implements VersionableObject
{
    public long guildId;
    public long blazon;
    public short level;
    public String guildName;
    public int nationId;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedRemoteGuildInfo() {
        super();
        this.guildId = 0L;
        this.blazon = 0L;
        this.level = 0;
        this.guildName = null;
        this.nationId = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedRemoteGuildInfo.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedRemoteGuildInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedRemoteGuildInfo", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedRemoteGuildInfo.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRemoteGuildInfo");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedRemoteGuildInfo", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedRemoteGuildInfo.this.serializedSize();
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
        buffer.putLong(this.blazon);
        buffer.putShort(this.level);
        if (this.guildName != null) {
            final byte[] serialized_guildName = StringUtils.toUTF8(this.guildName);
            if (serialized_guildName.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_guildName.length);
            buffer.put(serialized_guildName);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.nationId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.guildId = buffer.getLong();
        this.blazon = buffer.getLong();
        this.level = buffer.getShort();
        final int guildName_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_guildName = new byte[guildName_size];
        buffer.get(serialized_guildName);
        this.guildName = StringUtils.fromUTF8(serialized_guildName);
        this.nationId = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.guildId = 0L;
        this.blazon = 0L;
        this.level = 0;
        this.guildName = null;
        this.nationId = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036013) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedRemoteGuildInfoConverter converter = new CharacterSerializedRemoteGuildInfoConverter();
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
        size += 8;
        size += 8;
        size += 2;
        size += 2;
        size += ((this.guildName != null) ? StringUtils.toUTF8(this.guildName).length : 0);
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
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
        repr.append(prefix).append("blazon=").append(this.blazon).append('\n');
        repr.append(prefix).append("level=").append(this.level).append('\n');
        repr.append(prefix).append("guildName=").append(this.guildName).append('\n');
        repr.append(prefix).append("nationId=").append(this.nationId).append('\n');
    }
    
    private final class CharacterSerializedRemoteGuildInfoConverter
    {
        private long guildId;
        private long blazon;
        private short level;
        private String guildName;
        private int nationId;
        
        private CharacterSerializedRemoteGuildInfoConverter() {
            super();
            this.guildId = 0L;
            this.blazon = 0L;
            this.level = 0;
            this.guildName = null;
            this.nationId = 0;
        }
        
        public void pushResult() {
            CharacterSerializedRemoteGuildInfo.this.guildId = this.guildId;
            CharacterSerializedRemoteGuildInfo.this.blazon = this.blazon;
            CharacterSerializedRemoteGuildInfo.this.level = this.level;
            CharacterSerializedRemoteGuildInfo.this.guildName = this.guildName;
            CharacterSerializedRemoteGuildInfo.this.nationId = this.nationId;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.guildId = buffer.getLong();
            this.blazon = buffer.getLong();
            this.level = buffer.getShort();
            final int guildName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_guildName = new byte[guildName_size];
            buffer.get(serialized_guildName);
            this.guildName = StringUtils.fromUTF8(serialized_guildName);
            return true;
        }
        
        public void convert_v1_to_v10036013() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10036013) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10036013();
                return true;
            }
            return false;
        }
    }
}
