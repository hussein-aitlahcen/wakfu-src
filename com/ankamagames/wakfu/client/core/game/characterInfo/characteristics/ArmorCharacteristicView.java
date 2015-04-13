package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

final class ArmorCharacteristicView extends CharacteristicView
{
    private CharacterInfo m_info;
    private int m_currentValue;
    private int m_currentMaxValue;
    
    ArmorCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final CharacterInfo info) {
        super(charac, provider);
        this.m_currentValue = 0;
        this.m_currentMaxValue = 0;
        this.m_info = info;
        this.m_currentValue = charac.value();
        this.m_currentMaxValue = charac.value();
    }
    
    @Override
    protected int getCharacValue() {
        return super.getCharacValue();
    }
    
    @Override
    protected int getCharacMax() {
        return this.m_currentMaxValue;
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        if (charac.getType() != this.getType()) {
            return;
        }
        if (charac.value() > this.m_currentValue) {
            this.m_currentMaxValue += charac.value() - this.m_currentValue;
        }
        else if (charac.value() <= 0) {
            this.m_currentMaxValue = 0;
        }
        this.m_currentValue = charac.value();
        super.onCharacteristicUpdated(charac);
    }
}
