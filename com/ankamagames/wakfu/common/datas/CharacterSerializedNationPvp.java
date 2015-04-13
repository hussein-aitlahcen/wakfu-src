package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedNationPvp extends CharacterSerializedPart implements VersionableObject
{
    public byte pvpState;
    public long pvpDate;
    public byte pvpRank;
    public long pvpMoneyAmount;
    public long dailyPvpMoneyAmount;
    public long dailyPvpMoneyStartDate;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 34;
    
    public CharacterSerializedNationPvp() {
        super();
        this.pvpState = 0;
        this.pvpDate = 0L;
        this.pvpRank = 0;
        this.pvpMoneyAmount = 0L;
        this.dailyPvpMoneyAmount = 0L;
        this.dailyPvpMoneyStartDate = 0L;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedNationPvp.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedNationPvp");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedNationPvp", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedNationPvp.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationPvp");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationPvp", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedNationPvp.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.pvpState);
        buffer.putLong(this.pvpDate);
        buffer.put(this.pvpRank);
        buffer.putLong(this.pvpMoneyAmount);
        buffer.putLong(this.dailyPvpMoneyAmount);
        buffer.putLong(this.dailyPvpMoneyStartDate);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.pvpState = buffer.get();
        this.pvpDate = buffer.getLong();
        this.pvpRank = buffer.get();
        this.pvpMoneyAmount = buffer.getLong();
        this.dailyPvpMoneyAmount = buffer.getLong();
        this.dailyPvpMoneyStartDate = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.pvpState = 0;
        this.pvpDate = 0L;
        this.pvpRank = 0;
        this.pvpMoneyAmount = 0L;
        this.dailyPvpMoneyAmount = 0L;
        this.dailyPvpMoneyStartDate = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036014) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedNationPvpConverter converter = new CharacterSerializedNationPvpConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        return 34;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("pvpState=").append(this.pvpState).append('\n');
        repr.append(prefix).append("pvpDate=").append(this.pvpDate).append('\n');
        repr.append(prefix).append("pvpRank=").append(this.pvpRank).append('\n');
        repr.append(prefix).append("pvpMoneyAmount=").append(this.pvpMoneyAmount).append('\n');
        repr.append(prefix).append("dailyPvpMoneyAmount=").append(this.dailyPvpMoneyAmount).append('\n');
        repr.append(prefix).append("dailyPvpMoneyStartDate=").append(this.dailyPvpMoneyStartDate).append('\n');
    }
    
    private final class CharacterSerializedNationPvpConverter
    {
        private byte pvpState;
        private long pvpDate;
        private byte pvpRank;
        private long pvpMoneyAmount;
        private long dailyPvpMoneyAmount;
        private long dailyPvpMoneyStartDate;
        
        private CharacterSerializedNationPvpConverter() {
            super();
            this.pvpState = 0;
            this.pvpDate = 0L;
            this.pvpRank = 0;
            this.pvpMoneyAmount = 0L;
            this.dailyPvpMoneyAmount = 0L;
            this.dailyPvpMoneyStartDate = 0L;
        }
        
        public void pushResult() {
            CharacterSerializedNationPvp.this.pvpState = this.pvpState;
            CharacterSerializedNationPvp.this.pvpDate = this.pvpDate;
            CharacterSerializedNationPvp.this.pvpRank = this.pvpRank;
            CharacterSerializedNationPvp.this.pvpMoneyAmount = this.pvpMoneyAmount;
            CharacterSerializedNationPvp.this.dailyPvpMoneyAmount = this.dailyPvpMoneyAmount;
            CharacterSerializedNationPvp.this.dailyPvpMoneyStartDate = this.dailyPvpMoneyStartDate;
        }
        
        private boolean unserialize_v10036007(final ByteBuffer buffer) {
            this.pvpState = buffer.get();
            this.pvpDate = buffer.getLong();
            return true;
        }
        
        private boolean unserialize_v10036009(final ByteBuffer buffer) {
            this.pvpState = buffer.get();
            this.pvpDate = buffer.getLong();
            this.pvpRank = buffer.get();
            return true;
        }
        
        private boolean unserialize_v10036011(final ByteBuffer buffer) {
            this.pvpState = buffer.get();
            this.pvpDate = buffer.getLong();
            this.pvpRank = buffer.get();
            this.pvpMoneyAmount = buffer.getLong();
            return true;
        }
        
        public void convert_v10036007_to_v10036009() {
        }
        
        public void convert_v10036009_to_v10036011() {
        }
        
        public void convert_v10036011_to_v10036014() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 10036007) {
                return false;
            }
            if (version < 10036009) {
                final boolean ok = this.unserialize_v10036007(buffer);
                if (ok) {
                    this.convert_v10036007_to_v10036009();
                    this.convert_v10036009_to_v10036011();
                    this.convert_v10036011_to_v10036014();
                    return true;
                }
                return false;
            }
            else if (version < 10036011) {
                final boolean ok = this.unserialize_v10036009(buffer);
                if (ok) {
                    this.convert_v10036009_to_v10036011();
                    this.convert_v10036011_to_v10036014();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10036014) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036011(buffer);
                if (ok) {
                    this.convert_v10036011_to_v10036014();
                    return true;
                }
                return false;
            }
        }
    }
}
