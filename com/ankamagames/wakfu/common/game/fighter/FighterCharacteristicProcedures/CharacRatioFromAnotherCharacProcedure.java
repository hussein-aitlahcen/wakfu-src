package com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class CharacRatioFromAnotherCharacProcedure implements FighterCharacteristicProcedure
{
    private final CharacteristicManager<FighterCharacteristic> m_manager;
    private final FighterCharacteristicType m_fromCharacType;
    private final FighterCharacteristicType m_toCharacType;
    private final float m_ratio;
    private int m_previousAddedValue;
    
    public CharacRatioFromAnotherCharacProcedure(final CharacteristicManager<FighterCharacteristic> manager, final FighterCharacteristicType fromCharacType, final FighterCharacteristicType toCharacType, final float ratio) {
        super();
        this.m_previousAddedValue = 0;
        this.m_manager = manager;
        this.m_fromCharacType = fromCharacType;
        this.m_toCharacType = toCharacType;
        this.m_ratio = ratio;
    }
    
    @Override
    public void execute(final FighterCharacteristicEvent event, final int value) {
        if (value == 0) {
            return;
        }
        final FighterCharacteristic from = this.m_manager.getCharacteristic(this.m_fromCharacType);
        final FighterCharacteristic to = this.m_manager.getCharacteristic(this.m_toCharacType);
        to.substract(this.m_previousAddedValue);
        final int fromValue = from.value();
        final int valueToAdd = (int)Math.floor(this.m_ratio * fromValue);
        this.m_previousAddedValue = to.add(valueToAdd);
    }
}
