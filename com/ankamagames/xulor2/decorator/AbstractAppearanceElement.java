package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public abstract class AbstractAppearanceElement extends NonGraphicalElement implements AppearanceElement
{
    protected Widget m_widget;
    protected DecoratorAppearance m_appearance;
    protected boolean m_enabled;
    protected boolean m_styleRemovable;
    public static final int ENABLED_HASH;
    public static final int REMOVABLE_HASH;
    
    public AbstractAppearanceElement() {
        super();
        this.m_enabled = false;
        this.m_styleRemovable = true;
    }
    
    @Override
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
        if (this.m_appearance != null) {
            this.m_appearance.setNeedsToResetMeshes();
        }
    }
    
    @Override
    public void setWidget(final Widget widget) {
        this.m_widget = widget;
    }
    
    @Override
    public Widget getWidget() {
        return this.m_widget;
    }
    
    @Override
    public void setDecoratorAppearance(final DecoratorAppearance app) {
        this.m_appearance = app;
    }
    
    @Override
    public DecoratorAppearance getDecoratorAppearance() {
        return this.m_appearance;
    }
    
    @Override
    public boolean isRemovable() {
        return this.m_styleRemovable;
    }
    
    @Override
    public void setRemovable(final boolean removable) {
        this.m_styleRemovable = removable;
    }
    
    @Override
    public void cleanAll() {
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_widget = null;
        this.m_appearance = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_enabled = false;
        this.m_styleRemovable = true;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final AbstractAppearanceElement e = (AbstractAppearanceElement)source;
        super.copyElement(e);
        e.m_enabled = this.m_enabled;
        e.m_styleRemovable = this.m_styleRemovable;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AbstractAppearanceElement.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AbstractAppearanceElement.REMOVABLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setRemovable(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractAppearanceElement.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AbstractAppearanceElement.REMOVABLE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setRemovable(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        ENABLED_HASH = "enabled".hashCode();
        REMOVABLE_HASH = "removable".hashCode();
    }
}
