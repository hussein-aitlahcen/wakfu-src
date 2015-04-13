package com.ankamagames.xulor2.appearance.spacing;

import java.awt.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class InsetsElement extends NonGraphicalElement
{
    private final Insets m_insets;
    public static final int SPACING_HASH;
    public static final int INSETS_HASH;
    
    public InsetsElement() {
        super();
        this.m_insets = new Insets(0, 0, 0, 0);
    }
    
    @Deprecated
    public void setSpacing(final Insets insets) {
        this.setInsets(insets);
    }
    
    @Deprecated
    public Insets getSpacing() {
        return this.getInsets();
    }
    
    public void setInsets(final Insets insets) {
        if (insets == null) {
            return;
        }
        this.m_insets.top = insets.top;
        this.m_insets.bottom = insets.bottom;
        this.m_insets.left = insets.left;
        this.m_insets.right = insets.right;
        final SpacingAppearance app = this.getParentOfType(SpacingAppearance.class);
        if (app != null) {
            app.setSpacing(this);
        }
    }
    
    public Insets getInsets() {
        return this.m_insets;
    }
    
    @Override
    public void copyElement(final BasicElement i) {
        final InsetsElement e = (InsetsElement)i;
        super.copyElement(e);
        e.setInsets(this.m_insets);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_insets.set(0, 0, 0, 0);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == InsetsElement.SPACING_HASH || hash == InsetsElement.INSETS_HASH) {
            this.setInsets(cl.convertToInsets(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == InsetsElement.SPACING_HASH || hash == InsetsElement.INSETS_HASH) {
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
