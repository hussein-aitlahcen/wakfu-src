package com.ankamagames.wakfu.common.game.fighter;

import org.apache.log4j.*;
import gnu.trove.*;

class FightCharacteristicMap
{
    private static final Logger m_logger;
    private final FighterCharacteristic[] m_characteristicsArray;
    
    FightCharacteristicMap() {
        super();
        this.m_characteristicsArray = new FighterCharacteristic[FighterCharacteristicType.getCharacteristicMaxIndex() + 1];
    }
    
    public boolean contains(final byte id) {
        return this.m_characteristicsArray[id] != null;
    }
    
    public FighterCharacteristic get(final byte id) {
        return this.m_characteristicsArray[id];
    }
    
    public void put(final byte id, final FighterCharacteristic charac) {
        this.m_characteristicsArray[id] = charac;
    }
    
    public void forEachEntry(final TIntObjectProcedure<FighterCharacteristic> procedure) {
        for (int i = 0, size = this.m_characteristicsArray.length; i < size; ++i) {
            final FighterCharacteristic characteristic = this.m_characteristicsArray[i];
            if (characteristic != null) {
                if (!procedure.execute(i, characteristic)) {
                    return;
                }
            }
        }
    }
    
    public void forEachValue(final TObjectProcedure<FighterCharacteristic> procedure) {
        for (int i = 0, size = this.m_characteristicsArray.length; i < size; ++i) {
            final FighterCharacteristic characteristic = this.m_characteristicsArray[i];
            if (characteristic != null) {
                if (!procedure.execute(characteristic)) {
                    return;
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightCharacteristicMap.class);
    }
}
