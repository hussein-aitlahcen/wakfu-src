package com.ankamagames.wakfu.common.game.tax;

import com.ankamagames.wakfu.common.game.item.*;

public interface TaxHandler
{
    float getTaxValue(TaxContext p0);
    
    void setTaxes(TaxContext p0, float p1);
    
    int getTaxAmount(MerchantClient p0, TaxContext p1, int p2);
    
    void checkInTax(TaxContext p0, int p1);
    
    boolean mustTax(MerchantClient p0);
}
