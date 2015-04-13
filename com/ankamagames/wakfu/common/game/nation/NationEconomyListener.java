package com.ankamagames.wakfu.common.game.nation;

import com.ankamagames.wakfu.common.game.tax.*;

public interface NationEconomyListener
{
    void onSpentCashAdjusted(Nation p0, byte p1, int p2);
    
    void onTaxCashAdjusted(Nation p0, TaxContext p1, int p2);
}
