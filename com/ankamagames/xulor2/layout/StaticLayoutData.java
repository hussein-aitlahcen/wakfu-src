package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class StaticLayoutData extends AbstractLayoutData
{
    public static final String TAG = "StaticLayoutData";
    public static final String SHORT_TAG = "sld";
    private Dimension m_dimension;
    private Alignment17 m_alignment;
    private int m_x;
    private int m_y;
    private Percentage m_xPerc;
    private Percentage m_yPerc;
    private int m_xOffset;
    private int m_yOffset;
    private boolean m_xInit;
    private boolean m_yInit;
    private boolean m_xOffsetInit;
    private boolean m_yOffsetInit;
    private boolean m_initValue;
    private boolean m_usable;
    private Widget m_referentWidget;
    private String m_controlGroup;
    private boolean m_cascadeMethodEnabled;
    public static final int ALIGN_HASH;
    public static final int ALIGNMENT_HASH;
    public static final int CASCADE_METHOD_ENABLED_HASH;
    public static final int RESIZE_ONCE_HASH;
    public static final int INIT_VALUE_HASH;
    public static final int SIZE_HASH;
    public static final int USABLE_HASH;
    public static final int X_HASH;
    public static final int X_OFFSET_HASH;
    public static final int X_PERC_HASH;
    public static final int Y_HASH;
    public static final int Y_OFFSET_HASH;
    public static final int Y_PERC_HASH;
    
    public StaticLayoutData() {
        super();
        this.m_x = 0;
        this.m_y = 0;
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_xInit = false;
        this.m_yInit = false;
        this.m_xOffsetInit = false;
        this.m_yOffsetInit = false;
        this.m_initValue = false;
        this.m_usable = true;
        this.m_referentWidget = null;
        this.m_controlGroup = null;
        this.m_cascadeMethodEnabled = false;
    }
    
    @Override
    public String getTag() {
        return "StaticLayoutData";
    }
    
    public boolean isInitValue() {
        return this.m_initValue;
    }
    
    public void setInitValue(final boolean initValue) {
        this.m_initValue = initValue;
        this.m_usable = true;
    }
    
    @Deprecated
    public void setResizeOnce(final boolean resizeOnce) {
        this.setInitValue(resizeOnce);
    }
    
    public boolean isAutoPositionable() {
        return this.m_referentWidget != null;
    }
    
    public boolean isCascadePositionable() {
        return this.m_cascadeMethodEnabled;
    }
    
    public Widget getReferentWidget() {
        return this.m_referentWidget;
    }
    
    public void setReferentWidget(final Widget referentWidget) {
        this.m_referentWidget = referentWidget;
    }
    
    public void setCascadeMethodEnabled(final boolean cascadeMethodEnabled) {
        this.m_cascadeMethodEnabled = cascadeMethodEnabled;
    }
    
    public String getControlGroup() {
        return this.m_controlGroup;
    }
    
    public void setControlGroup(final String controlGroup) {
        this.m_controlGroup = controlGroup;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public Percentage getXPerc() {
        return this.m_xPerc;
    }
    
    public void setXPerc(final Percentage xPerc) {
        this.m_xPerc = xPerc;
    }
    
    public Percentage getYPerc() {
        return this.m_yPerc;
    }
    
    public void setYPerc(final Percentage yPerc) {
        this.m_yPerc = yPerc;
    }
    
    public void setX(final int x) {
        this.m_xInit = true;
        this.m_x = x;
    }
    
    public boolean isXInit() {
        return this.m_xInit;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_yInit = true;
        this.m_y = y;
    }
    
    public boolean isYInit() {
        return this.m_yInit;
    }
    
    public int getXOffset() {
        return this.m_xOffset;
    }
    
    public void setXOffset(final int offset) {
        this.m_xOffsetInit = true;
        this.m_xOffset = offset;
    }
    
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    public void setYOffset(final int offset) {
        this.m_yOffsetInit = true;
        this.m_yOffset = offset;
    }
    
    public boolean isXOffsetInit() {
        return this.m_xOffsetInit;
    }
    
    public boolean isYOffsetInit() {
        return this.m_yOffsetInit;
    }
    
    public Dimension getSize() {
        return this.m_dimension;
    }
    
    public void setSize(final Dimension dimension) {
        this.m_dimension = dimension;
    }
    
    public Alignment17 getAlignment() {
        return this.m_alignment;
    }
    
    public void setAlignment(final Alignment17 position) {
        this.m_alignment = position;
    }
    
    public void setAlign(final Alignment17 position) {
        this.m_alignment = position;
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    public boolean isUsable() {
        return !this.m_initValue || this.m_usable;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_x = 0;
        this.m_y = 0;
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_xInit = false;
        this.m_yInit = false;
        this.m_xOffsetInit = false;
        this.m_yOffsetInit = false;
        this.m_initValue = false;
        this.m_cascadeMethodEnabled = false;
        this.m_usable = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_referentWidget = null;
        this.m_dimension = null;
        this.m_alignment = null;
        this.m_controlGroup = null;
        this.m_xPerc = null;
        this.m_yPerc = null;
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final StaticLayoutData e = (StaticLayoutData)s;
        super.copyElement(e);
        e.m_alignment = this.m_alignment;
        if (this.m_dimension != null) {
            e.m_dimension = (Dimension)this.m_dimension.clone();
        }
        if (this.m_xInit) {
            e.setX(this.m_x);
        }
        if (this.m_yInit) {
            e.setY(this.m_y);
        }
        if (this.m_xOffsetInit) {
            e.setXOffset(this.m_xOffset);
        }
        if (this.m_yOffsetInit) {
            e.setYOffset(this.m_yOffset);
        }
        if (this.m_xPerc != null) {
            e.setXPerc((Percentage)this.m_xPerc.clone());
        }
        if (this.m_yPerc != null) {
            e.setYPerc((Percentage)this.m_yPerc.clone());
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == StaticLayoutData.ALIGN_HASH || hash == StaticLayoutData.ALIGNMENT_HASH) {
            this.setAlign(Alignment17.value(value));
        }
        else if (hash == StaticLayoutData.CASCADE_METHOD_ENABLED_HASH) {
            this.setCascadeMethodEnabled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StaticLayoutData.RESIZE_ONCE_HASH || hash == StaticLayoutData.INIT_VALUE_HASH) {
            this.setInitValue(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StaticLayoutData.SIZE_HASH) {
            this.setSize(cl.convertToDimension(value));
        }
        else if (hash == StaticLayoutData.USABLE_HASH) {
            this.setUsable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StaticLayoutData.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == StaticLayoutData.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else if (hash == StaticLayoutData.X_PERC_HASH) {
            this.setXPerc(cl.convertToPercentage(value));
        }
        else if (hash == StaticLayoutData.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == StaticLayoutData.Y_OFFSET_HASH) {
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != StaticLayoutData.Y_PERC_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setYPerc(cl.convertToPercentage(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == StaticLayoutData.X_PERC_HASH) {
            this.setXPerc((Percentage)value);
        }
        else if (hash == StaticLayoutData.Y_PERC_HASH) {
            this.setYPerc((Percentage)value);
        }
        else {
            if (hash != StaticLayoutData.SIZE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setSize((Dimension)value);
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        ALIGNMENT_HASH = "alignment".hashCode();
        CASCADE_METHOD_ENABLED_HASH = "cascadeMethodEnabled".hashCode();
        RESIZE_ONCE_HASH = "resizeOnce".hashCode();
        INIT_VALUE_HASH = "initValue".hashCode();
        SIZE_HASH = "size".hashCode();
        USABLE_HASH = "usable".hashCode();
        X_HASH = "x".hashCode();
        X_OFFSET_HASH = "xOffset".hashCode();
        X_PERC_HASH = "xPerc".hashCode();
        Y_HASH = "y".hashCode();
        Y_OFFSET_HASH = "yOffset".hashCode();
        Y_PERC_HASH = "yPerc".hashCode();
    }
}
