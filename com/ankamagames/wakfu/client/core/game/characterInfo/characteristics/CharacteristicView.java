package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class CharacteristicView extends BaseCharacteristicView
{
    public CharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider) {
        this(charac, provider, (byte)(-1));
    }
    
    public CharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider, final byte iconId) {
        super(charac, iconId, provider);
        this.m_charac.addListener(this);
    }
    
    @Override
    protected int getCharacValue() {
        return this.m_charac.value();
    }
    
    @Override
    protected int getCharacMax() {
        return this.m_charac.max();
    }
}
