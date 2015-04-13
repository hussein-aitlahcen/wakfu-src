package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class TotalHPCharacteristicView extends MultipleCharacteristicView
{
    private CharacterInfo m_info;
    
    public TotalHPCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info, final FighterCharacteristic... characs) {
        super(charac, provider, (byte)39, characs);
        this.m_info = info;
    }
    
    @Override
    protected Object getPercentage() {
        if (this.m_info.isOffPlay()) {
            return this.m_info.getCharacteristicValue(FighterCharacteristicType.KO_TIME_BEFORE_DEATH);
        }
        return super.getPercentage();
    }
}
