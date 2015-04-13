package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class GlidingLayoutData extends AbstractLayoutData
{
    public static final String TAG = "gld";
    private Alignment9 m_align;
    private Alignment9 m_initAlign;
    private boolean m_initValue;
    private boolean m_usable;
    public static final int ALIGN_HASH;
    public static final int INIT_ALIGN_HASH;
    public static final int INIT_VALUE_HASH;
    
    public GlidingLayoutData() {
        super();
        this.m_align = Alignment9.CENTER;
        this.m_initAlign = Alignment9.CENTER;
        this.m_initValue = false;
        this.m_usable = true;
    }
    
    @Override
    public String getTag() {
        return "gld";
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    public Alignment9 getInitAlign() {
        return this.m_initAlign;
    }
    
    public void setInitAlign(final Alignment9 initAlign) {
        this.m_initAlign = initAlign;
    }
    
    public boolean isInitValue() {
        return this.m_initValue;
    }
    
    public void setInitValue(final boolean initValue) {
        this.m_initValue = initValue;
    }
    
    public boolean isUsable() {
        return !this.m_initValue || this.m_usable || MasterRootContainer.getInstance().isResized();
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        ((GlidingLayoutData)source).setAlign(this.m_align);
        ((GlidingLayoutData)source).setAlign(this.m_initAlign);
        ((GlidingLayoutData)source).setInitValue(this.m_initValue);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
        this.m_initAlign = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_initValue = false;
        this.m_usable = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == GlidingLayoutData.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == GlidingLayoutData.INIT_ALIGN_HASH) {
            this.setInitAlign(Alignment9.value(value));
        }
        else {
            if (hash != GlidingLayoutData.INIT_VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setInitValue(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        INIT_ALIGN_HASH = "initAlign".hashCode();
        INIT_VALUE_HASH = "initValue".hashCode();
    }
}
