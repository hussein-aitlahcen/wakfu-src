package com.ankamagames.wakfu.common.game.fighter;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class MonsterBreedCharacteristicManager extends BreedCharacteristicManager
{
    private final float[] m_increments;
    private final int m_levelMin;
    
    public MonsterBreedCharacteristicManager(final int levelMin) {
        super();
        this.m_increments = new float[FighterCharacteristicType.getCharacteristicMaxIndex() + 1];
        this.m_levelMin = levelMin;
    }
    
    public float getCharacteristicIncValue(final FighterCharacteristicType charac) {
        return this.m_increments[charac.getId()];
    }
    
    public int getCharacteristicValue(final FighterCharacteristicType charac, final int level) {
        final int levelDiff = level - this.m_levelMin;
        return (int)(this.getBaseCharacteristic(charac) + levelDiff * this.m_increments[charac.getId()]);
    }
    
    public void setCharacteristic(final FighterCharacteristicType charac, final int baseValue, final float increment) {
        this.m_increments[charac.getId()] = increment;
        this.setBaseCharacteristic(charac, baseValue);
    }
}
