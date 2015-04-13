package com.ankamagames.xulor2.component.colorpicker;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TintIntensityColorPicker extends AbstractColorPicker
{
    public static final String TAG = "tintIntensityColorPicker";
    public static final String TINT_COLOR_PICKER = "tintColorPicker";
    public static final String INTENSITY_COLOR_PICKER = "intensityColorPicker";
    private BasicColorPicker m_tintColorPicker;
    private BasicColorPicker m_intensityColorPicker;
    private int m_numVariation;
    private float m_minIntensity;
    private float m_maxIntensity;
    private EventListener m_colorChangedListener;
    public static final int COLORS_HASH;
    public static final int NUM_VARIATIONS_HASH;
    public static final int MIN_INTENSITY_HASH;
    public static final int MAX_INTENSITY_HASH;
    
    public TintIntensityColorPicker() {
        super();
        this.m_numVariation = 8;
        this.m_minIntensity = 0.3f;
        this.m_maxIntensity = 1.0f;
    }
    
    @Override
    public void addThemeElementName(final String themeElementName, final Widget widget) {
        super.addThemeElementName(themeElementName, widget);
        if (themeElementName.equals("tintColorPicker")) {
            this.m_tintColorPicker = (BasicColorPicker)widget;
            this.m_colorsChanged = true;
            this.setNeedsToPreProcess();
        }
        else if (themeElementName.equals("intensityColorPicker")) {
            this.m_intensityColorPicker = (BasicColorPicker)widget;
            this.m_colorsChanged = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public int getNumVariation() {
        return this.m_numVariation;
    }
    
    public void setNumVariation(final int numVariation) {
        this.m_numVariation = numVariation;
    }
    
    public float getMinIntensity() {
        return this.m_minIntensity;
    }
    
    public void setMinIntensity(final float minIntensity) {
        this.m_minIntensity = minIntensity;
    }
    
    public float getMaxIntensity() {
        return this.m_maxIntensity;
    }
    
    public void setMaxIntensity(final float maxIntensity) {
        this.m_maxIntensity = maxIntensity;
    }
    
    private void fillWithIntensityColorList(final Color c) {
        if (this.m_intensityColorPicker == null) {
            return;
        }
        final ArrayList<Color> colors = new ArrayList<Color>(this.m_numVariation);
        for (int i = 0; i < this.m_numVariation; ++i) {
            final Color color = new Color(c);
            final float intensity = this.m_minIntensity + i * (this.m_maxIntensity - this.m_minIntensity) / (this.m_numVariation - 1);
            color.setIntensity(intensity);
            colors.add(color);
        }
        this.m_intensityColorPicker.setColors(colors);
    }
    
    @Override
    protected void onColorsChanged() {
        this.m_tintColorPicker.setColors(this.m_colors);
        if (this.m_colors == null || this.m_colors.size() == 0) {
            return;
        }
        this.fillWithIntensityColorList(this.m_colors.get(0));
    }
    
    private void registerListeners() {
        this.m_colorChangedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == TintIntensityColorPicker.this.m_intensityColorPicker) {
                    return false;
                }
                final ItemEvent e = (ItemEvent)event;
                final Color c = (Color)e.getItemValue();
                TintIntensityColorPicker.this.fillWithIntensityColorList(c);
                return true;
            }
        };
        this.addEventListener(Events.ITEM_CLICK, this.m_colorChangedListener, true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_tintColorPicker = null;
        this.m_intensityColorPicker = null;
        this.m_colorChangedListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RowLayout rl = (RowLayout)this.getLayoutManager();
        rl.setHorizontal(false);
        this.m_numVariation = 8;
        this.m_minIntensity = 0.3f;
        this.m_maxIntensity = 1.0f;
        this.registerListeners();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TintIntensityColorPicker.NUM_VARIATIONS_HASH) {
            this.setNumVariation(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TintIntensityColorPicker.MAX_INTENSITY_HASH) {
            this.setMaxIntensity(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != TintIntensityColorPicker.MIN_INTENSITY_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setMinIntensity(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TintIntensityColorPicker.COLORS_HASH) {
            this.setColors((ArrayList<Color>)value);
        }
        else if (hash == TintIntensityColorPicker.NUM_VARIATIONS_HASH) {
            this.setNumVariation(PrimitiveConverter.getInteger(value));
        }
        else if (hash == TintIntensityColorPicker.MAX_INTENSITY_HASH) {
            this.setMaxIntensity(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != TintIntensityColorPicker.MIN_INTENSITY_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setMinIntensity(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        COLORS_HASH = "colors".hashCode();
        NUM_VARIATIONS_HASH = "numVariations".hashCode();
        MIN_INTENSITY_HASH = "minIntensity".hashCode();
        MAX_INTENSITY_HASH = "maxIntensity".hashCode();
    }
}
