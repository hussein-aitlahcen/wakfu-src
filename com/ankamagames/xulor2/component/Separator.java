package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class Separator extends Image
{
    public static final String TAG = "Separator";
    private boolean m_horizontal;
    private boolean m_horizontalChanged;
    public static final int HORIZONTAL_HASH;
    
    public Separator() {
        super();
        this.m_horizontal = true;
        this.m_horizontalChanged = false;
    }
    
    @Override
    public String getTag() {
        return "Separator";
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
        this.m_horizontalChanged = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    public boolean computeMinSize() {
        boolean minSizeChanged = super.computeMinSize();
        if (this.m_imageMesh.getPixmap() != null) {
            if (this.m_horizontal) {
                this.setMinSize(new Dimension(0, this.m_imageMesh.getPixmap().getHeight()));
            }
            else {
                this.setMinSize(new Dimension(this.m_imageMesh.getPixmap().getWidth(), 0));
            }
            minSizeChanged = true;
        }
        return minSizeChanged;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_horizontalChanged) {
            if (this.computeMinSize()) {
                this.m_containerParent.invalidateMinSize();
            }
            this.m_horizontalChanged = false;
        }
        return ret;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Separator.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        HORIZONTAL_HASH = "horizontal".hashCode();
    }
}
