package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public abstract class Background extends AbstractDecorator
{
    private boolean m_scaled;
    public static final int SCALED_HASH;
    
    public Background() {
        super();
        this.m_scaled = false;
    }
    
    public boolean isScaled() {
        return this.m_scaled;
    }
    
    public void setScaled(final boolean scaled) {
        this.m_scaled = scaled;
    }
    
    @Override
    public abstract AbstractBackgroundMesh getMesh();
    
    @Override
    public void copyElement(final BasicElement b) {
        final Background e = (Background)b;
        super.copyElement(e);
        e.setScaled(this.m_scaled);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_scaled = false;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Background.SCALED_HASH) {
            this.setScaled(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Background.SCALED_HASH) {
            this.setScaled(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        SCALED_HASH = "scaled".hashCode();
    }
}
