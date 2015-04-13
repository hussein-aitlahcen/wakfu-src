package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedAccountInformation extends CharacterSerializedPart implements VersionableObject
{
    public int[] adminRights;
    public int subscriptionLevel;
    public int forcedSubscriptionLevel;
    public int antiAddictionLevel;
    public long sessionStartTime;
    public int[] additionalRights;
    public byte additionalSlots;
    public byte vaultUpgrades;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedAccountInformation() {
        super();
        this.adminRights = null;
        this.subscriptionLevel = 0;
        this.forcedSubscriptionLevel = 0;
        this.antiAddictionLevel = 0;
        this.sessionStartTime = 0L;
        this.additionalRights = null;
        this.additionalSlots = 0;
        this.vaultUpgrades = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAccountInformation.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAccountInformation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAccountInformation", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAccountInformation.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAccountInformation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAccountInformation", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAccountInformation.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.adminRights != null) {
            if (this.adminRights.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.adminRights.length);
            for (int i = 0; i < this.adminRights.length; ++i) {
                buffer.putInt(this.adminRights[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putInt(this.subscriptionLevel);
        buffer.putInt(this.forcedSubscriptionLevel);
        buffer.putInt(this.antiAddictionLevel);
        buffer.putLong(this.sessionStartTime);
        if (this.additionalRights != null) {
            if (this.additionalRights.length > 65535) {
                return false;
            }
            buffer.putShort((short)this.additionalRights.length);
            for (int i = 0; i < this.additionalRights.length; ++i) {
                buffer.putInt(this.additionalRights[i]);
            }
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.put(this.additionalSlots);
        buffer.put(this.vaultUpgrades);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int adminRights_size = buffer.getShort() & 0xFFFF;
        if (adminRights_size > 0) {
            this.adminRights = new int[adminRights_size];
            for (int i = 0; i < adminRights_size; ++i) {
                this.adminRights[i] = buffer.getInt();
            }
        }
        else {
            this.adminRights = null;
        }
        this.subscriptionLevel = buffer.getInt();
        this.forcedSubscriptionLevel = buffer.getInt();
        this.antiAddictionLevel = buffer.getInt();
        this.sessionStartTime = buffer.getLong();
        final int additionalRights_size = buffer.getShort() & 0xFFFF;
        if (additionalRights_size > 0) {
            this.additionalRights = new int[additionalRights_size];
            for (int j = 0; j < additionalRights_size; ++j) {
                this.additionalRights[j] = buffer.getInt();
            }
        }
        else {
            this.additionalRights = null;
        }
        this.additionalSlots = buffer.get();
        this.vaultUpgrades = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.adminRights = null;
        this.subscriptionLevel = 0;
        this.forcedSubscriptionLevel = 0;
        this.antiAddictionLevel = 0;
        this.sessionStartTime = 0L;
        this.additionalRights = null;
        this.additionalSlots = 0;
        this.vaultUpgrades = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10035003) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedAccountInformationConverter converter = new CharacterSerializedAccountInformationConverter();
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
        size += ((this.adminRights != null) ? (this.adminRights.length * 4) : 0);
        size += 4;
        size += 4;
        size += 4;
        size += 8;
        size += 2;
        size += ((this.additionalRights != null) ? (this.additionalRights.length * 4) : 0);
        ++size;
        return ++size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("adminRights=(").append(this.adminRights.length).append(" bytes)\n");
        repr.append(prefix).append("subscriptionLevel=").append(this.subscriptionLevel).append('\n');
        repr.append(prefix).append("forcedSubscriptionLevel=").append(this.forcedSubscriptionLevel).append('\n');
        repr.append(prefix).append("antiAddictionLevel=").append(this.antiAddictionLevel).append('\n');
        repr.append(prefix).append("sessionStartTime=").append(this.sessionStartTime).append('\n');
        repr.append(prefix).append("additionalRights=(").append(this.additionalRights.length).append(" bytes)\n");
        repr.append(prefix).append("additionalSlots=").append(this.additionalSlots).append('\n');
        repr.append(prefix).append("vaultUpgrades=").append(this.vaultUpgrades).append('\n');
    }
    
    private final class CharacterSerializedAccountInformationConverter
    {
        private int[] adminRights;
        private int subscriptionLevel;
        private int forcedSubscriptionLevel;
        private int antiAddictionLevel;
        private long sessionStartTime;
        private int[] additionalRights;
        private byte additionalSlots;
        private byte vaultUpgrades;
        
        private CharacterSerializedAccountInformationConverter() {
            super();
            this.adminRights = null;
            this.subscriptionLevel = 0;
            this.forcedSubscriptionLevel = 0;
            this.antiAddictionLevel = 0;
            this.sessionStartTime = 0L;
            this.additionalRights = null;
            this.additionalSlots = 0;
            this.vaultUpgrades = 0;
        }
        
        public void pushResult() {
            CharacterSerializedAccountInformation.this.adminRights = this.adminRights;
            CharacterSerializedAccountInformation.this.subscriptionLevel = this.subscriptionLevel;
            CharacterSerializedAccountInformation.this.forcedSubscriptionLevel = this.forcedSubscriptionLevel;
            CharacterSerializedAccountInformation.this.antiAddictionLevel = this.antiAddictionLevel;
            CharacterSerializedAccountInformation.this.sessionStartTime = this.sessionStartTime;
            CharacterSerializedAccountInformation.this.additionalRights = this.additionalRights;
            CharacterSerializedAccountInformation.this.additionalSlots = this.additionalSlots;
            CharacterSerializedAccountInformation.this.vaultUpgrades = this.vaultUpgrades;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int adminRights_size = buffer.getShort() & 0xFFFF;
            if (adminRights_size > 0) {
                this.adminRights = new int[adminRights_size];
                for (int i = 0; i < adminRights_size; ++i) {
                    this.adminRights[i] = buffer.getInt();
                }
            }
            else {
                this.adminRights = null;
            }
            this.subscriptionLevel = buffer.getInt();
            this.forcedSubscriptionLevel = buffer.getInt();
            this.antiAddictionLevel = buffer.getInt();
            this.sessionStartTime = buffer.getLong();
            final int additionalRights_size = buffer.getShort() & 0xFFFF;
            if (additionalRights_size > 0) {
                this.additionalRights = new int[additionalRights_size];
                for (int j = 0; j < additionalRights_size; ++j) {
                    this.additionalRights[j] = buffer.getInt();
                }
            }
            else {
                this.additionalRights = null;
            }
            return true;
        }
        
        private boolean unserialize_v10034001(final ByteBuffer buffer) {
            final int adminRights_size = buffer.getShort() & 0xFFFF;
            if (adminRights_size > 0) {
                this.adminRights = new int[adminRights_size];
                for (int i = 0; i < adminRights_size; ++i) {
                    this.adminRights[i] = buffer.getInt();
                }
            }
            else {
                this.adminRights = null;
            }
            this.subscriptionLevel = buffer.getInt();
            this.forcedSubscriptionLevel = buffer.getInt();
            this.antiAddictionLevel = buffer.getInt();
            this.sessionStartTime = buffer.getLong();
            final int additionalRights_size = buffer.getShort() & 0xFFFF;
            if (additionalRights_size > 0) {
                this.additionalRights = new int[additionalRights_size];
                for (int j = 0; j < additionalRights_size; ++j) {
                    this.additionalRights[j] = buffer.getInt();
                }
            }
            else {
                this.additionalRights = null;
            }
            this.additionalSlots = buffer.get();
            return true;
        }
        
        public void convert_v1_to_v10034001() {
        }
        
        public void convert_v10034001_to_v10035003() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version < 10034001) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10034001();
                    this.convert_v10034001_to_v10035003();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10035003) {
                    return false;
                }
                final boolean ok = this.unserialize_v10034001(buffer);
                if (ok) {
                    this.convert_v10034001_to_v10035003();
                    return true;
                }
                return false;
            }
        }
    }
}
