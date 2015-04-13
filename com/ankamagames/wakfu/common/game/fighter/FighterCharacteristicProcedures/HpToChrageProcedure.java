package com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public final class HpToChrageProcedure implements FighterCharacteristicProcedure
{
    private final FighterCharacteristicManager m_manager;
    private final float m_originalCharacMaxPercentLimit;
    
    public HpToChrageProcedure(final FighterCharacteristicManager manager, final float originalCharacMaxPercentLimit) {
        super();
        this.m_manager = manager;
        this.m_originalCharacMaxPercentLimit = originalCharacMaxPercentLimit;
    }
    
    @Override
    public void execute(final FighterCharacteristicEvent event, final int value) {
        final FighterCharacteristic hp = this.m_manager.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        final FighterCharacteristic virtualHp = this.m_manager.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP);
        final FighterCharacteristic chrage = this.m_manager.getCharacteristic((CharacteristicType)FighterCharacteristicType.CHRAGE);
        if (hp == null || chrage == null || virtualHp == null) {
            return;
        }
        final int maxValue = hp.max() + virtualHp.max();
        final int maxValueToUse = (int)(maxValue * this.m_originalCharacMaxPercentLimit);
        final int missingValue = maxValue - (hp.value() + virtualHp.value());
        final float percentValue = Math.min(missingValue / maxValueToUse * 100.0f, 100.0f);
        chrage.set((int)Math.ceil(percentValue));
    }
}
