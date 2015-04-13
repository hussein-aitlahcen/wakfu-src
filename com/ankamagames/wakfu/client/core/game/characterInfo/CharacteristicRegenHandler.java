package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.common.game.fighter.*;

public class CharacteristicRegenHandler extends AbstractRegenHandler
{
    private final FighterCharacteristic m_characteristic;
    
    public CharacteristicRegenHandler(final double regenTickDuration, final FighterCharacteristic characteristic) {
        super(regenTickDuration);
        this.m_characteristic = characteristic;
    }
    
    @Override
    protected int getValue() {
        return this.m_characteristic.value();
    }
    
    @Override
    protected void onRegen(final int regen) {
        super.onRegen(regen);
        this.m_characteristic.add(regen);
    }
    
    @Override
    protected void onValueSet(final int value) {
        this.m_characteristic.set(value);
    }
}
