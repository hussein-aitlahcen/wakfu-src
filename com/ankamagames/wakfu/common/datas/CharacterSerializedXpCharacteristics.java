package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedXpCharacteristics extends CharacterSerializedPart implements VersionableObject
{
    public final RawBonusPointCharacteristics bonusPointCharacteristics;
    public int wakfuGauge;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedXpCharacteristics() {
        super();
        this.bonusPointCharacteristics = new RawBonusPointCharacteristics();
        this.wakfuGauge = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedXpCharacteristics.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedXpCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedXpCharacteristics", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedXpCharacteristics.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedXpCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedXpCharacteristics", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedXpCharacteristics.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean bonusPointCharacteristics_ok = this.bonusPointCharacteristics.serialize(buffer);
        if (!bonusPointCharacteristics_ok) {
            return false;
        }
        buffer.putInt(this.wakfuGauge);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean bonusPointCharacteristics_ok = this.bonusPointCharacteristics.unserialize(buffer);
        if (!bonusPointCharacteristics_ok) {
            return false;
        }
        this.wakfuGauge = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.bonusPointCharacteristics.clear();
        this.wakfuGauge = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10030001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedXpCharacteristicsConverter converter = new CharacterSerializedXpCharacteristicsConverter();
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
        size += this.bonusPointCharacteristics.serializedSize();
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
        repr.append(prefix).append("bonusPointCharacteristics=...\n");
        this.bonusPointCharacteristics.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("wakfuGauge=").append(this.wakfuGauge).append('\n');
    }
    
    private final class CharacterSerializedXpCharacteristicsConverter
    {
        private long xp;
        private final RawBonusPointCharacteristics bonusPointCharacteristics;
        private int wakfuGauge;
        
        private CharacterSerializedXpCharacteristicsConverter() {
            super();
            this.xp = 0L;
            this.bonusPointCharacteristics = new RawBonusPointCharacteristics();
            this.wakfuGauge = 0;
        }
        
        public void pushResult() {
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.freePoints = this.bonusPointCharacteristics.freePoints;
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.xpBonusPoints.clear();
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.xpBonusPoints.ensureCapacity(this.bonusPointCharacteristics.xpBonusPoints.size());
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.xpBonusPoints.addAll(this.bonusPointCharacteristics.xpBonusPoints);
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.characteristicBonusPoints.clear();
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.characteristicBonusPoints.ensureCapacity(this.bonusPointCharacteristics.characteristicBonusPoints.size());
            CharacterSerializedXpCharacteristics.this.bonusPointCharacteristics.characteristicBonusPoints.addAll(this.bonusPointCharacteristics.characteristicBonusPoints);
            CharacterSerializedXpCharacteristics.this.wakfuGauge = this.wakfuGauge;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.xp = buffer.getLong();
            final boolean bonusPointCharacteristics_ok = this.bonusPointCharacteristics.unserializeVersion(buffer, 1);
            if (!bonusPointCharacteristics_ok) {
                return false;
            }
            this.wakfuGauge = buffer.getInt();
            return true;
        }
        
        public void convert_v1_to_v10030001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10030001) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10030001();
                return true;
            }
            return false;
        }
    }
}
