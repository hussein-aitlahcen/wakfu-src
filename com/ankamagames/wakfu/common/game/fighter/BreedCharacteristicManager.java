package com.ankamagames.wakfu.common.game.fighter;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class BreedCharacteristicManager
{
    private final int[] m_characteristicsArray;
    private final boolean[] m_setted;
    
    public BreedCharacteristicManager() {
        super();
        final int max = FighterCharacteristicType.getCharacteristicMaxIndex() + 1;
        this.m_characteristicsArray = new int[max];
        this.m_setted = new boolean[max];
        for (final FighterCharacteristicType charac : FighterCharacteristicType.values()) {
            this.setBaseCharacteristic(charac, charac.getDefaultValue());
        }
    }
    
    public int getBaseCharacteristic(final CharacteristicType charac) {
        return this.m_characteristicsArray[charac.getId()];
    }
    
    public void setBaseCharacteristic(final CharacteristicType chara, final int value) {
        this.m_characteristicsArray[chara.getId()] = value;
        this.m_setted[chara.getId()] = true;
    }
    
    public boolean contains(final CharacteristicType charac) {
        return this.m_setted[charac.getId()];
    }
}
