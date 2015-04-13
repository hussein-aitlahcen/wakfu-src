package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public abstract class MultipleCharacteristicView extends BaseCharacteristicView
{
    protected final ArrayList<FighterCharacteristic> m_characteristics;
    
    public MultipleCharacteristicView(final FighterCharacteristic baseCharac, final CharacteristicViewProvider provider, final FighterCharacteristic... characs) {
        this(baseCharac, provider, (byte)(-1), characs);
    }
    
    public MultipleCharacteristicView(final FighterCharacteristic baseCharac, final CharacteristicViewProvider provider, final byte iconId, final FighterCharacteristic... characs) {
        super(baseCharac, iconId, provider);
        (this.m_characteristics = new ArrayList<FighterCharacteristic>()).addAll(Arrays.asList(characs));
        for (int i = 0, size = this.m_characteristics.size(); i < size; ++i) {
            this.m_characteristics.get(i).addListener(this);
        }
    }
    
    @Override
    protected int getCharacValue() {
        int value = 0;
        for (int i = 0, size = this.m_characteristics.size(); i < size; ++i) {
            value += this.m_characteristics.get(i).value();
        }
        return value;
    }
    
    @Override
    protected int getCharacMax() {
        int max = 0;
        for (int i = 0, size = this.m_characteristics.size(); i < size; ++i) {
            max += this.m_characteristics.get(i).max();
        }
        return max;
    }
}
