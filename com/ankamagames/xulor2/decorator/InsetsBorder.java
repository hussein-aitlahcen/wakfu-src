package com.ankamagames.xulor2.decorator;

import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class InsetsBorder extends AbstractDecorator
{
    protected Insets m_borderInsets;
    protected boolean m_insetsDirty;
    public static final int SPACING_HASH;
    public static final int INSETS_HASH;
    
    public Insets getInsets() {
        return this.m_borderInsets;
    }
    
    public void setInsets(final Insets insets) {
        this.m_borderInsets.set(insets.top, insets.left, insets.bottom, insets.right);
    }
    
    @Deprecated
    public void setSpacing(final Insets insets) {
        this.setInsets(insets);
    }
    
    @Override
    public abstract Entity getEntity();
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_borderInsets = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_borderInsets = new Insets(0, 0, 0, 0);
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        final InsetsBorder e = (InsetsBorder)b;
        super.copyElement(e);
        e.setInsets(this.m_borderInsets);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == InsetsBorder.SPACING_HASH || hash == InsetsBorder.INSETS_HASH) {
            this.setInsets(cl.convertToInsets(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == InsetsBorder.SPACING_HASH || hash == InsetsBorder.INSETS_HASH) {
            this.setInsets((Insets)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        SPACING_HASH = "spacing".hashCode();
        INSETS_HASH = "insets".hashCode();
    }
}
