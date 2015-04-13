package com.ankamagames.wakfu.common.datas.Breed;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public abstract class SecondaryCharacsCalculator
{
    private static final Logger m_logger;
    public static final SecondaryCharacsCalculator NULL_CALCULATOR;
    
    protected abstract void calculateFor(final CharacteristicManager<FighterCharacteristic> p0, final short p1, final short p2);
    
    public abstract int previewModificationForLevelUp(final FighterCharacteristicType p0, final short p1, final short p2);
    
    public void applyForInitialize(final CharacteristicManager<FighterCharacteristic> characteristics, final short currentLevel) {
        this.calculateFor(characteristics, (short)0, currentLevel);
    }
    
    public void applyForLevelUp(final CharacteristicManager<FighterCharacteristic> characs, final short oldLevel, final short newLevel) {
        this.calculateFor(characs, oldLevel, newLevel);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SecondaryCharacsCalculator.class);
        NULL_CALCULATOR = new SecondaryCharacsCalculator() {
            @Override
            protected void calculateFor(final CharacteristicManager<FighterCharacteristic> characteristics, final short oldLevel, final short newLevel) {
            }
            
            @Override
            public int previewModificationForLevelUp(final FighterCharacteristicType charac, final short oldLevel, final short newLevel) {
                return 0;
            }
        };
    }
}
