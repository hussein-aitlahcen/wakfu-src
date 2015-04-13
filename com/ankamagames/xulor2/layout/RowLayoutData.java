package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class RowLayoutData extends AbstractLayoutData
{
    public static final String TAG = "rld";
    private Alignment9 m_align;
    public static final int ALIGN_HASH;
    public static final int VGAP_HASH;
    public static final int HORIZONTAL_HASH;
    
    public RowLayoutData() {
        super();
        this.m_align = Alignment9.CENTER;
    }
    
    @Override
    public String getTag() {
        return "rld";
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        ((RowLayoutData)source).setAlign(this.m_align);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RowLayoutData.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        VGAP_HASH = "vgap".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
    }
}
