package com.ankamagames.wakfu.common.game.tax;

import gnu.trove.*;

public enum TaxContext
{
    FLEA_ADD_ITEM_CONTEXT(1, 0.05f, 0.05f, 0.7f), 
    MARKET_ADD_ITEM_CONTEXT(2, 0.05f, 0.05f, 0.7f);
    
    private static TByteObjectHashMap<TaxContext> m_typesById;
    public final byte id;
    public final float initialValue;
    public final float minValue;
    public final float maxValue;
    
    private TaxContext(final int id, final float initialValue, final float minValue, final float maxValue) {
        this.id = (byte)id;
        this.initialValue = initialValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public static TaxContext getById(final byte id) {
        return TaxContext.m_typesById.get(id);
    }
    
    static {
        TaxContext.m_typesById = new TByteObjectHashMap<TaxContext>();
        for (final TaxContext context : values()) {
            TaxContext.m_typesById.put(context.id, context);
        }
    }
}
