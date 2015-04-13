package com.ankamagames.xulor2.component.colorpicker;

import com.ankamagames.xulor2.component.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class AbstractColorPicker extends Container
{
    private static final Logger m_logger;
    protected ArrayList<Color> m_colors;
    protected boolean m_colorsChanged;
    public static final int COLORS_HASH;
    
    public AbstractColorPicker() {
        super();
        this.m_colorsChanged = false;
    }
    
    public void setColors(final ArrayList<Color> colors) {
        if (this.m_colors == colors) {
            return;
        }
        this.m_colors = colors;
        this.m_colorsChanged = true;
        this.setNeedsToPreProcess();
    }
    
    protected abstract void onColorsChanged();
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_colorsChanged) {
            this.onColorsChanged();
            this.m_colorsChanged = false;
        }
        return ret;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setColors(null);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_colorsChanged = false;
        this.m_colors = new ArrayList<Color>();
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AbstractColorPicker.COLORS_HASH) {
            this.setColors((ArrayList<Color>)value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractColorPicker.class);
        COLORS_HASH = "colors".hashCode();
    }
}
