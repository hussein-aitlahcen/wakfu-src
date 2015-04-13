package com.ankamagames.wakfu.common.game.fighter;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class LazyFighterCharacteristicManager implements CharacteristicManager<FighterCharacteristic>
{
    private final TObjectProcedure<FighterCharacteristic> m_copyCharacProcedure;
    FightCharacteristicMap m_characteristics;
    private final MonsterBreedCharacteristicManager m_parentManager;
    private final int m_level;
    
    public LazyFighterCharacteristicManager(final MonsterBreedCharacteristicManager parent, final int level) {
        super();
        this.m_copyCharacProcedure = new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                LazyFighterCharacteristicManager.this.getCharacteristic((CharacteristicType)charac.getType()).copyMinMaxAndValue(charac);
                return true;
            }
        };
        this.m_parentManager = parent;
        this.m_level = level;
    }
    
    @Override
    public void makeDefault() {
        this.m_characteristics = null;
    }
    
    @Override
    public void copy(final FighterCharacteristicManager srcCharacteristics) {
        srcCharacteristics.foreachCharacteristic(this.m_copyCharacProcedure);
    }
    
    @Override
    public boolean contains(final CharacteristicType charac) {
        return charac != null && ((this.m_characteristics != null && this.m_characteristics.contains(charac.getId())) || this.m_parentManager.contains(charac));
    }
    
    @Nullable
    @Override
    public FighterCharacteristic getCharacteristic(final CharacteristicType charac) {
        this.ensureCharacteristicsExists();
        final FighterCharacteristic characteristic = this.m_characteristics.get(charac.getId());
        if (characteristic != null) {
            return characteristic;
        }
        if (this.m_parentManager.contains(charac)) {
            return this.createCharacteristicFromParent((FighterCharacteristicType)charac);
        }
        return null;
    }
    
    private FighterCharacteristic createCharacteristicFromParent(final FighterCharacteristicType type) {
        final FighterCharacteristic charactCopy = this.createCharacteristic(type);
        return this.initializeCharacteristicFromParent(charactCopy);
    }
    
    private FighterCharacteristic initializeCharacteristicFromParent(final FighterCharacteristic characteristic) {
        final FighterCharacteristicType type = characteristic.getType();
        characteristic.setMin(type.getDefaultMin());
        if (type.isExpandable()) {
            characteristic.setMax(this.m_parentManager.getCharacteristicValue(type, this.m_level));
            characteristic.toMax();
        }
        else {
            characteristic.setMax(type.getDefaultMax());
            characteristic.set(this.m_parentManager.getCharacteristicValue(type, this.m_level));
        }
        return characteristic;
    }
    
    private FighterCharacteristic createCharacteristic(final FighterCharacteristicType type) {
        final FighterCharacteristic charactCopy = new FighterCharacteristic(type, type.getLowerBound(), type.getUpperBound());
        this.m_characteristics.put(type.getId(), charactCopy);
        return charactCopy;
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) {
        if (this.m_characteristics != null) {
            final FighterCharacteristic characteristic = this.m_characteristics.get(charac.getId());
            if (characteristic != null) {
                return characteristic.value();
            }
        }
        if (this.m_parentManager.contains(charac)) {
            return this.m_parentManager.getCharacteristicValue((FighterCharacteristicType)charac, this.m_level);
        }
        return -1;
    }
    
    @Override
    public int getCharacteristicMaxValue(final CharacteristicType charac) {
        if (this.m_characteristics != null) {
            final FighterCharacteristic characteristic = this.m_characteristics.get(charac.getId());
            if (characteristic != null) {
                return characteristic.max();
            }
        }
        if (!this.m_parentManager.contains(charac)) {
            return -1;
        }
        if (charac.isExpandable()) {
            return this.m_parentManager.getCharacteristicValue((FighterCharacteristicType)charac, this.m_level);
        }
        return charac.getDefaultMax();
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics rawCharacteristics) {
        rawCharacteristics.clear();
        if (this.m_characteristics != null) {
            this.m_characteristics.forEachEntry(new TIntObjectProcedure<FighterCharacteristic>() {
                @Override
                public boolean execute(final int key, final FighterCharacteristic charac) {
                    final RawCharacteristics.Characteristic rawChar = new RawCharacteristics.Characteristic();
                    rawChar.index = (byte)key;
                    rawChar.current = charac.m_currentValue;
                    rawChar.min = charac.m_minValue;
                    rawChar.max = charac.m_maxValue;
                    rawChar.maxPercentModifier = charac.m_maxPercentModifier;
                    rawCharacteristics.characteristics.add(rawChar);
                    return true;
                }
            });
        }
        return true;
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics rawCharacteristics, final CharacteristicType... characteristicTypes) {
        rawCharacteristics.clear();
        if (this.m_characteristics != null) {
            for (int i = 0; i < characteristicTypes.length; ++i) {
                final byte index = characteristicTypes[i].getId();
                final FighterCharacteristic charac = this.m_characteristics.get(index);
                if (charac != null) {
                    final RawCharacteristics.Characteristic rawChar = new RawCharacteristics.Characteristic();
                    rawChar.index = index;
                    rawChar.current = charac.m_currentValue;
                    rawChar.min = charac.m_minValue;
                    rawChar.max = charac.m_maxValue;
                    rawChar.maxPercentModifier = charac.m_maxPercentModifier;
                    rawCharacteristics.characteristics.add(rawChar);
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawCharacteristics rawCharacteristics) {
        if (!rawCharacteristics.characteristics.isEmpty()) {
            this.ensureCharacteristicsExists();
        }
        for (final RawCharacteristics.Characteristic rawChar : rawCharacteristics.characteristics) {
            final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(rawChar.index);
            FighterCharacteristic characteristic = this.m_characteristics.get(charac.getId());
            if (characteristic == null) {
                characteristic = new FighterCharacteristic(charac, charac.getLowerBound(), charac.getUpperBound());
                this.m_characteristics.put(charac.getId(), characteristic);
            }
            characteristic.m_currentValue = rawChar.current;
            characteristic.m_minValue = rawChar.min;
            characteristic.m_maxValue = rawChar.max;
            characteristic.m_maxPercentModifier = rawChar.maxPercentModifier;
        }
        return true;
    }
    
    private void ensureCharacteristicsExists() {
        if (this.m_characteristics == null) {
            this.m_characteristics = new FightCharacteristicMap();
        }
    }
    
    public MonsterBreedCharacteristicManager getParent() {
        return this.m_parentManager;
    }
    
    private void createAllCharacteristics() {
        this.ensureCharacteristicsExists();
        for (final FighterCharacteristicType c : FighterCharacteristicType.values()) {
            if (!this.m_characteristics.contains(c.getId())) {
                this.createCharacteristicFromParent(c);
            }
        }
    }
    
    public void addListener(final CharacteristicUpdateListener listener) {
        this.createAllCharacteristics();
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                charac.addListener(listener);
                return true;
            }
        });
    }
    
    public void removeListener(final CharacteristicUpdateListener listener) {
        if (this.m_characteristics == null) {
            return;
        }
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                charac.removeListener(listener);
                return true;
            }
        });
    }
}
