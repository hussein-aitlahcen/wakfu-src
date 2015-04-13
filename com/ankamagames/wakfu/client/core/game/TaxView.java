package com.ankamagames.wakfu.client.core.game;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import org.jetbrains.annotations.*;

public class TaxView implements FieldProvider
{
    private final Tax m_tax;
    public static final String CONTEXT_NAME_FIELD = "contextName";
    public static final String TAX_VALUE_FIELD = "taxValue";
    public static final String TAX_PERCENTAGE_FIELD = "taxPercentage";
    public static final String TAX_PERCENTAGE_LONG_DESC_FIELD = "taxPercentageLongDesc";
    public static final String[] FIELDS;
    
    public TaxView(final Tax tax) {
        super();
        this.m_tax = tax;
    }
    
    @Override
    public String[] getFields() {
        return TaxView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("contextName")) {
            return this.getContextDesc();
        }
        if (fieldName.equals("taxValue")) {
            return this.m_tax.getValue() * 100.0f / 5.0f;
        }
        if (fieldName.equals("taxPercentage")) {
            return this.getTaxValueDesc();
        }
        if (fieldName.equals("taxPercentageLongDesc")) {
            return WakfuTranslator.getInstance().getString("protector.tax.longDesc", new TextWidgetFormater().openText().addSize(14).append(this.getTaxValueDesc()).closeText().finishAndToString());
        }
        return null;
    }
    
    private String getTaxValueDesc() {
        return getTaxValueDesc(this.m_tax);
    }
    
    private String getContextDesc() {
        return getContextDesc(this.m_tax);
    }
    
    public String getDescription() {
        return getDescription(this.m_tax);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public Tax getTax() {
        return this.m_tax;
    }
    
    public static String getTaxValueDesc(@NotNull final Tax tax) {
        return String.format("%d%%", (int)(tax.getValue() * 100.0f));
    }
    
    public static String getContextDesc(@NotNull final Tax tax) {
        return WakfuTranslator.getInstance().getString(55, tax.getContext().id, new Object[0]);
    }
    
    public static String getDescription(@NotNull final Tax tax) {
        return getContextDesc(tax) + " : " + getTaxValueDesc(tax);
    }
    
    static {
        FIELDS = new String[] { "contextName", "taxValue", "taxPercentage", "taxPercentageLongDesc" };
    }
}
