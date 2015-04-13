package com.ankamagames.wakfu.common.game.fighter;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class FighterCharacteristicManager implements CharacteristicManager<FighterCharacteristic>
{
    private static final Logger m_logger;
    private final FightCharacteristicMap m_characteristics;
    
    public FighterCharacteristicManager() {
        super();
        this.m_characteristics = new FightCharacteristicMap();
        for (final FighterCharacteristicType c : FighterCharacteristicType.values()) {
            final FighterCharacteristic characteristic = new FighterCharacteristic(c, c.getLowerBound(), c.getUpperBound());
            this.m_characteristics.put(c.getId(), characteristic);
        }
        this.makeDefault();
    }
    
    @Override
    public void makeDefault() {
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                charac.makeDefault();
                return true;
            }
        });
    }
    
    @Override
    public void copy(final FighterCharacteristicManager srcCharacteristic) {
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic currentCharac) {
                final FighterCharacteristic srcCharac = srcCharacteristic.getCharacteristic((CharacteristicType)currentCharac.getType());
                currentCharac.copyMinMaxAndValue(srcCharac);
                return true;
            }
        });
    }
    
    @Override
    public boolean contains(final CharacteristicType charac) {
        return charac != null && this.m_characteristics.contains(charac.getId());
    }
    
    @Override
    public FighterCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.get(charac.getId());
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) {
        return this.m_characteristics.get(charac.getId()).value();
    }
    
    @Override
    public int getCharacteristicMaxValue(final CharacteristicType charac) {
        return this.m_characteristics.get(charac.getId()).max();
    }
    
    public void foreachCharacteristic(final TObjectProcedure<FighterCharacteristic> procedure) {
        this.m_characteristics.forEachValue(procedure);
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics rawCharacteristics) {
        rawCharacteristics.clear();
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
        return true;
    }
    
    @Override
    public boolean toRaw(final RawCharacteristics rawCharacteristics, final CharacteristicType... characteristicTypes) {
        rawCharacteristics.clear();
        for (int i = 0; i < characteristicTypes.length; ++i) {
            final byte index = characteristicTypes[i].getId();
            final FighterCharacteristic charac = this.m_characteristics.get(index);
            final RawCharacteristics.Characteristic rawChar = new RawCharacteristics.Characteristic();
            rawChar.index = index;
            rawChar.current = charac.m_currentValue;
            rawChar.min = charac.m_minValue;
            rawChar.max = charac.m_maxValue;
            rawChar.maxPercentModifier = charac.m_maxPercentModifier;
            rawCharacteristics.characteristics.add(rawChar);
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawCharacteristics rawCharacteristics) {
        boolean ok = true;
        for (final RawCharacteristics.Characteristic rawChar : rawCharacteristics.characteristics) {
            final FighterCharacteristic charac = this.m_characteristics.get(rawChar.index);
            if (charac != null) {
                charac.m_minValue = rawChar.min;
                charac.m_maxValue = rawChar.max;
                charac.m_maxPercentModifier = rawChar.maxPercentModifier;
                charac.set(rawChar.current);
            }
            else {
                FighterCharacteristicManager.m_logger.error((Object)("Impossible de trouver la caract\u00e9ristique \u00e0 l'index " + rawChar.index));
                ok = false;
            }
        }
        return ok;
    }
    
    public void addListener(final CharacteristicUpdateListener listener) {
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                charac.addListener(listener);
                return true;
            }
        });
    }
    
    public void removeListener(final CharacteristicUpdateListener listener) {
        this.m_characteristics.forEachValue(new TObjectProcedure<FighterCharacteristic>() {
            @Override
            public boolean execute(final FighterCharacteristic charac) {
                charac.removeListener(listener);
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)FighterCharacteristicManager.class);
    }
}
