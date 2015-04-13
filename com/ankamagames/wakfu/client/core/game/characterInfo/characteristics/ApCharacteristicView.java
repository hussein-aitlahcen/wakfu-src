package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

final class ApCharacteristicView extends CharacteristicView
{
    private CharacterInfo m_info;
    
    ApCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info) {
        super(charac, provider, (byte)1);
        this.m_info = info;
    }
    
    @Override
    protected int getCharacValue() {
        if (this.m_info.isActiveProperty(FightPropertyType.AP_AS_MP)) {
            return 0;
        }
        return super.getCharacValue();
    }
    
    @Override
    protected int getCharacMax() {
        if (this.m_info.isActiveProperty(FightPropertyType.AP_AS_MP)) {
            return 0;
        }
        return super.getCharacMax();
    }
}
