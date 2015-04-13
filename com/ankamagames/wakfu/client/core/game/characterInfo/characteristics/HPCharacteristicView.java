package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class HPCharacteristicView extends CharacteristicView
{
    private CharacterInfo m_info;
    
    public HPCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info) {
        this(charac, provider, info, (byte)4);
    }
    
    public HPCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info, final byte iconId) {
        super(charac, provider, iconId);
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
