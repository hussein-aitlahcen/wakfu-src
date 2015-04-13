package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedNationPvpMoney extends CharacterSerializedPart implements VersionableObject
{
    public long pvpMoneyAmount;
    public long dailyPvpMoneyAmount;
    private final BinarSerialPart m_binarPart;
    public static final int SERIALIZED_SIZE = 16;
    
    public CharacterSerializedNationPvpMoney() {
        super();
        this.pvpMoneyAmount = 0L;
        this.dailyPvpMoneyAmount = 0L;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedNationPvpMoney.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedNationPvpMoney");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedNationPvpMoney", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedNationPvpMoney.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationPvpMoney");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedNationPvpMoney", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedNationPvpMoney.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.pvpMoneyAmount);
        buffer.putLong(this.dailyPvpMoneyAmount);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.pvpMoneyAmount = buffer.getLong();
        this.dailyPvpMoneyAmount = buffer.getLong();
        return true;
    }
    
    @Override
    public void clear() {
        this.pvpMoneyAmount = 0L;
        this.dailyPvpMoneyAmount = 0L;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036014) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedNationPvpMoneyConverter converter = new CharacterSerializedNationPvpMoneyConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        return 16;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("pvpMoneyAmount=").append(this.pvpMoneyAmount).append('\n');
        repr.append(prefix).append("dailyPvpMoneyAmount=").append(this.dailyPvpMoneyAmount).append('\n');
    }
    
    private final class CharacterSerializedNationPvpMoneyConverter
    {
        private long pvpMoneyAmount;
        private long dailyPvpMoneyAmount;
        
        private CharacterSerializedNationPvpMoneyConverter() {
            super();
            this.pvpMoneyAmount = 0L;
            this.dailyPvpMoneyAmount = 0L;
        }
        
        public void pushResult() {
            CharacterSerializedNationPvpMoney.this.pvpMoneyAmount = this.pvpMoneyAmount;
            CharacterSerializedNationPvpMoney.this.dailyPvpMoneyAmount = this.dailyPvpMoneyAmount;
        }
        
        private boolean unserialize_v10036012(final ByteBuffer buffer) {
            this.pvpMoneyAmount = buffer.getLong();
            return true;
        }
        
        public void convert_v10036012_to_v10036014() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 10036012) {
                return false;
            }
            if (version >= 10036014) {
                return false;
            }
            final boolean ok = this.unserialize_v10036012(buffer);
            if (ok) {
                this.convert_v10036012_to_v10036014();
                return true;
            }
            return false;
        }
    }
}
