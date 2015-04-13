package com.ankamagames.wakfu.common.game.tax;

public class Tax
{
    private final TaxContext m_context;
    private float m_value;
    
    public Tax(final TaxContext context) {
        super();
        this.m_context = context;
        this.m_value = context.initialValue;
    }
    
    public Tax(final TaxContext context, final float value) {
        this(context);
        this.m_value = value;
    }
    
    public int getTaxAmount(final int kamasAmount) {
        return Math.round(kamasAmount * this.m_value);
    }
    
    public float getValue() {
        return this.m_value;
    }
    
    public void setValue(final float value) {
        this.m_value = Math.max(Math.min(value, this.m_context.maxValue), this.m_context.minValue);
    }
    
    public TaxContext getContext() {
        return this.m_context;
    }
}
