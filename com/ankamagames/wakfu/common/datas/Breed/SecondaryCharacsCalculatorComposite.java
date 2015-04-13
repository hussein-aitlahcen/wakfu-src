package com.ankamagames.wakfu.common.datas.Breed;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;

class SecondaryCharacsCalculatorComposite extends SecondaryCharacsCalculator
{
    private final List<SecondaryCharacsCalculator> m_individualGains;
    
    SecondaryCharacsCalculatorComposite(final TByteFloatHashMap gains) {
        super();
        this.m_individualGains = new ArrayList<SecondaryCharacsCalculator>();
        gains.forEachEntry(new TByteFloatProcedure() {
            @Override
            public boolean execute(final byte characId, final float value) {
                SecondaryCharacsCalculatorComposite.this.m_individualGains.add(new SecondaryCharacLinearIncrease(characId, value));
                return true;
            }
        });
    }
    
    @Override
    protected void calculateFor(final CharacteristicManager<FighterCharacteristic> characteristics, final short oldLevel, final short newLevel) {
        for (final SecondaryCharacsCalculator individualGain : this.m_individualGains) {
            individualGain.calculateFor(characteristics, oldLevel, newLevel);
        }
    }
    
    @Override
    public int previewModificationForLevelUp(final FighterCharacteristicType charac, final short oldLevel, final short newLevel) {
        int totalGain = 0;
        for (final SecondaryCharacsCalculator individualGain : this.m_individualGains) {
            totalGain += individualGain.previewModificationForLevelUp(charac, oldLevel, newLevel);
        }
        return totalGain;
    }
}
