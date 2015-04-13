package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

class SecondaryCharacLinearIncrease extends SecondaryCharacsCalculator
{
    private final byte m_characteristicId;
    private final float m_value;
    
    SecondaryCharacLinearIncrease(final byte characteristicId, final float value) {
        super();
        this.m_characteristicId = characteristicId;
        this.m_value = value;
    }
    
    public int getValueAtLevel(final short level) {
        return (int)Math.floor(level * this.m_value);
    }
    
    @Override
    protected void calculateFor(final CharacteristicManager<FighterCharacteristic> characteristics, final short oldLevel, final short newLevel) {
        final FighterCharacteristic characteristic = characteristics.getCharacteristic(FighterCharacteristicType.getCharacteristicTypeFromId(this.m_characteristicId));
        final int modification = this.getValueAtLevel(newLevel) - this.getValueAtLevel(oldLevel);
        characteristic.updateMaxValue(modification);
        characteristic.add(modification);
    }
    
    @Override
    public int previewModificationForLevelUp(final FighterCharacteristicType charac, final short oldLevel, final short newLevel) {
        if (this.m_characteristicId != charac.getId()) {
            return 0;
        }
        return this.getValueAtLevel(newLevel) - this.getValueAtLevel(oldLevel);
    }
}
