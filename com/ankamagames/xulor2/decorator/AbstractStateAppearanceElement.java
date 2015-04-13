package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class AbstractStateAppearanceElement extends AbstractAppearanceElement implements StateAppearanceElement
{
    protected String m_label;
    public static final int LABEL_HASH;
    public static final int STATE_HASH;
    
    public AbstractStateAppearanceElement() {
        super();
        this.m_label = null;
    }
    
    @Override
    public void setLabel(final String label) {
        this.m_label = label;
    }
    
    @Override
    public void setState(final String label) {
        this.setLabel(label);
    }
    
    @Override
    public String getLabel() {
        return this.m_label;
    }
    
    @Override
    public String getState() {
        return this.getLabel();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_label = null;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final AbstractStateAppearanceElement e = (AbstractStateAppearanceElement)source;
        super.copyElement(e);
        e.m_label = this.m_label;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AbstractStateAppearanceElement.LABEL_HASH || hash == AbstractStateAppearanceElement.STATE_HASH) {
            this.setLabel(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractStateAppearanceElement.LABEL_HASH || hash == AbstractStateAppearanceElement.STATE_HASH) {
            this.setLabel(String.valueOf(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        LABEL_HASH = "label".hashCode();
        STATE_HASH = "state".hashCode();
    }
}
