package com.ankamagames.xulor2.appearance;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TextWidgetAppearance extends DecoratorAppearance
{
    public static final String TAG = "TextWidgetAppearance";
    public static final String COMPATIBLE_TAG1 = "TextViewAppearance";
    public static final String COMPATIBLE_TAG2 = "TextEditorAppearance";
    public static final String COMPATIBLE_TAG3 = "LabelAppearance";
    public static final String TEXT_COLOR_TAG = "text";
    private static final ObjectPool m_pool;
    private Alignment9 m_align;
    private TextRenderer m_font;
    private Color m_textColor;
    private boolean m_useHighContrast;
    private boolean m_useHighContrastInit;
    private boolean m_justify;
    private boolean m_justifyInit;
    public static final int ALIGN_HASH;
    public static final int ALIGNMENT_HASH;
    public static final int JUSTIFY_HASH;
    public static final int FONT_HASH;
    public static final int TEXT_COLOR_HASH;
    public static final int USE_HIGH_CONTRAST_HASH;
    
    public static TextWidgetAppearance checkOut() {
        TextWidgetAppearance appearance;
        try {
            appearance = (TextWidgetAppearance)TextWidgetAppearance.m_pool.borrowObject();
            appearance.m_currentPool = TextWidgetAppearance.m_pool;
        }
        catch (Exception e) {
            TextWidgetAppearance.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            appearance = new TextWidgetAppearance();
            appearance.onCheckOut();
        }
        return appearance;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
    }
    
    @Override
    public String getTag() {
        return "TextWidgetAppearance";
    }
    
    @Override
    public void setWidget(final Widget w) {
        if (this.m_align != null && w instanceof Alignment9Client) {
            ((Alignment9Client)w).setAlign(this.m_align);
        }
        if (this.m_font != null && w instanceof FontClient) {
            ((FontClient)w).setFont(this.m_font);
        }
        if (this.m_textColor != null && w instanceof ColorClient) {
            ((ColorClient)w).setColor(this.m_textColor, null);
        }
        if (this.m_useHighContrastInit && this.m_widget instanceof TextWidget) {
            ((TextWidget)this.m_widget).setUseHighContrast(this.m_useHighContrast);
        }
        if (this.m_justifyInit && this.m_widget instanceof TextWidget) {
            ((TextWidget)this.m_widget).setJustify(this.m_justify);
        }
        super.setWidget(w);
    }
    
    public void setUseHighContrast(final boolean use) {
        this.m_useHighContrast = use;
        this.m_useHighContrastInit = true;
        if (this.m_widget != null && this.m_widget instanceof TextWidget) {
            ((TextWidget)this.m_widget).setUseHighContrast(this.m_useHighContrast);
        }
    }
    
    public boolean getUseHighContrast() {
        return this.m_useHighContrast;
    }
    
    public boolean getJustify() {
        return this.m_justify;
    }
    
    public void setJustify(final boolean justify) {
        this.m_justify = justify;
        this.m_justifyInit = true;
        if (this.m_widget != null && this.m_widget instanceof TextWidget) {
            ((TextWidget)this.m_widget).setJustify(justify);
        }
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
        if (this.m_align != null && this.m_widget != null && this.m_widget instanceof Alignment9Client) {
            ((Alignment9Client)this.m_widget).setAlign(align);
        }
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    @Deprecated
    public void setAlignment(final Alignment9 align) {
        this.setAlign(align);
    }
    
    @Deprecated
    public Alignment9 getAlignment() {
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof Alignment9Switch && sw.getState().equalsIgnoreCase(this.m_currentState)) {
                return ((Alignment9Switch)sw).getAlignment();
            }
        }
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof Alignment9Switch && sw.getState().equalsIgnoreCase("DEFAULT")) {
                return ((Alignment9Switch)sw).getAlignment();
            }
        }
        return this.m_align;
    }
    
    public void setFont(final TextRenderer renderer) {
        this.m_font = renderer;
        if (this.m_widget instanceof FontClient) {
            ((FontClient)this.m_widget).setFont(renderer);
        }
    }
    
    public TextRenderer getFont() {
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof FontElement && sw.getState().equalsIgnoreCase(this.m_currentState)) {
                return ((FontElement)sw).getRenderer();
            }
        }
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof FontElement && sw.getState().equalsIgnoreCase("DEFAULT")) {
                return ((FontElement)sw).getRenderer();
            }
        }
        return this.m_font;
    }
    
    @Override
    public void setColor(final Color color, final String name) {
        if (name == null || "text".equals(name)) {
            this.setTextColor(color);
        }
        else {
            super.setColor(color, name);
        }
    }
    
    public void setTextColor(final Color color) {
        if (this.m_textColor == color) {
            return;
        }
        this.m_textColor = color;
        if (this.m_widget instanceof ColorClient) {
            ((ColorClient)this.m_widget).setColor(color, null);
        }
    }
    
    public Color getTextColor() {
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof ColorElement && sw.getState().equalsIgnoreCase(this.m_currentState)) {
                return ((ColorElement)sw).getColor();
            }
        }
        for (int i = 0, size = this.m_switches.size(); i < size; ++i) {
            final Switch sw = this.m_switches.get(i);
            if (sw instanceof ColorElement && sw.getState().equalsIgnoreCase("DEFAULT")) {
                return ((ColorElement)sw).getColor();
            }
        }
        return this.m_textColor;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
        this.m_font = null;
        this.m_textColor = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_useHighContrast = false;
        this.m_useHighContrastInit = false;
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final TextWidgetAppearance e = (TextWidgetAppearance)t;
        super.copyElement(e);
        if (this.m_align != null) {
            e.setAlign(this.m_align);
        }
        if (this.m_font != null) {
            e.setFont(this.m_font);
        }
        if (this.m_textColor != null) {
            e.setTextColor(this.m_textColor);
        }
        if (this.m_useHighContrastInit) {
            e.setUseHighContrast(this.m_useHighContrast);
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TextWidgetAppearance.ALIGN_HASH || hash == TextWidgetAppearance.ALIGNMENT_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == TextWidgetAppearance.JUSTIFY_HASH) {
            this.setJustify(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidgetAppearance.FONT_HASH) {
            this.setFont(cl.convertToFont(value));
        }
        else if (hash == TextWidgetAppearance.TEXT_COLOR_HASH) {
            this.setTextColor(cl.convertToColor(value));
        }
        else {
            if (hash != TextWidgetAppearance.USE_HIGH_CONTRAST_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setUseHighContrast(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == TextWidgetAppearance.TEXT_COLOR_HASH) {
            this.setTextColor((Color)value);
        }
        else if (hash == TextWidgetAppearance.ALIGN_HASH || hash == TextWidgetAppearance.ALIGNMENT_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == TextWidgetAppearance.JUSTIFY_HASH) {
            this.setJustify(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == TextWidgetAppearance.FONT_HASH) {
            this.setFont((TextRenderer)value);
        }
        else {
            if (hash != TextWidgetAppearance.USE_HIGH_CONTRAST_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setUseHighContrast(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<TextWidgetAppearance>() {
            @Override
            public TextWidgetAppearance makeObject() {
                return new TextWidgetAppearance();
            }
        });
        ALIGN_HASH = "align".hashCode();
        ALIGNMENT_HASH = "alignment".hashCode();
        JUSTIFY_HASH = "justify".hashCode();
        FONT_HASH = "font".hashCode();
        TEXT_COLOR_HASH = "textColor".hashCode();
        USE_HIGH_CONTRAST_HASH = "useHighContrast".hashCode();
    }
}
