package com.ankamagames.xulor2.nongraphical;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.isometric.text.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class ToolTipElement extends NonGraphicalElement
{
    public static final String TAG = "tooltip";
    public static Color DEFAULT_TEXT_COLOR;
    public static Color DEFAULT_BACKGROUND_COLOR;
    public static Color DEFAULT_BORDER_COLOR;
    public static Font DEFAULT_FONT;
    public static float DEFAULT_BORDER_WIDTH;
    public String m_text;
    public int m_xOffset;
    public int m_yOffset;
    public float m_borderWidth;
    public int m_duration;
    public float m_maxWidth;
    public Color m_textColor;
    public Color m_backgroundColor;
    public Color m_borderColor;
    public Font m_font;
    public BackgroundedTextHotPointPosition m_hotPointPosition;
    public Alignment9 m_position;
    public static final int TEXT_HASH;
    public static final int BACKGROUND_COLOR_HASH;
    public static final int BORDER_COLOR_HASH;
    public static final int BORDER_WIDTH_HASH;
    public static final int DURATION_HASH;
    public static final int HOT_POINT_POSITION_HASH;
    public static final int MAX_WIDTH_HASH;
    public static final int POSITION_HASH;
    public static final int TEXT_COLOR_HASH;
    public static final int X_OFFSET_HASH;
    public static final int Y_OFFSET_HASH;
    
    public ToolTipElement() {
        super();
        this.m_text = "";
        this.m_xOffset = 0;
        this.m_yOffset = 0;
        this.m_borderWidth = ToolTipElement.DEFAULT_BORDER_WIDTH;
        this.m_duration = Tooltip.getDefaultDuration();
        this.m_maxWidth = -1.0f;
        this.m_textColor = ToolTipElement.DEFAULT_TEXT_COLOR;
        this.m_backgroundColor = ToolTipElement.DEFAULT_BACKGROUND_COLOR;
        this.m_borderColor = ToolTipElement.DEFAULT_BORDER_COLOR;
        this.m_font = ToolTipElement.DEFAULT_FONT;
        this.m_hotPointPosition = BackgroundedTextHotPointPosition.SOUTH_WEST;
        this.m_position = Alignment9.NORTH_WEST;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ToolTipElement t = (ToolTipElement)source;
        super.copyElement(source);
        t.m_text = this.m_text;
        t.m_xOffset = this.m_xOffset;
        t.m_yOffset = this.m_yOffset;
        t.m_duration = this.m_duration;
        t.m_maxWidth = this.m_maxWidth;
        t.setTextColor(this.m_textColor);
        t.setBackgroundColor(this.m_backgroundColor);
        t.setBorderColor(this.m_borderColor);
        t.m_font = this.m_font;
        t.m_hotPointPosition = this.m_hotPointPosition;
        t.m_position = this.m_position;
        t.m_borderWidth = this.m_borderWidth;
    }
    
    @Override
    public String getTag() {
        return "tooltip";
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration * 1000;
    }
    
    public Color getBackgroundColor() {
        return this.m_backgroundColor;
    }
    
    public Color getBorderColor() {
        return this.m_borderColor;
    }
    
    public float getBorderWidth() {
        return this.m_borderWidth;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public Font getFont() {
        return this.m_font;
    }
    
    public BackgroundedTextHotPointPosition getHotPointPosition() {
        return this.m_hotPointPosition;
    }
    
    public float getMaxWidth() {
        return this.m_maxWidth;
    }
    
    public Alignment9 getPosition() {
        return this.m_position;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public Color getTextColor() {
        return this.m_textColor;
    }
    
    public Integer getXOffset() {
        return this.m_xOffset;
    }
    
    public int getYOffset() {
        return this.m_yOffset;
    }
    
    public void setBackgroundColor(final Color backgroundColor) {
        if (this.m_backgroundColor == backgroundColor) {
            return;
        }
        this.m_backgroundColor = backgroundColor;
    }
    
    public void setBorderColor(final Color borderColor) {
        if (this.m_borderColor == borderColor) {
            return;
        }
        this.m_borderColor = borderColor;
    }
    
    public void setBorderWidth(final float borderWidth) {
        this.m_borderWidth = borderWidth;
    }
    
    public void setHotPointPosition(final BackgroundedTextHotPointPosition hotPointPosition) {
        this.m_hotPointPosition = hotPointPosition;
    }
    
    public void setMaxWidth(final float maxWidth) {
        this.m_maxWidth = maxWidth;
    }
    
    public void setPosition(final Alignment9 position) {
        this.m_position = position;
    }
    
    public void setText(final String text) {
        this.m_text = text;
    }
    
    public void setTextColor(final Color textColor) {
        if (this.m_textColor == textColor) {
            return;
        }
        this.m_textColor = textColor;
    }
    
    public void setXOffset(final int xOffset) {
        this.m_xOffset = xOffset;
    }
    
    public void setYOffset(final int yOffset) {
        this.m_yOffset = yOffset;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ToolTipElement.TEXT_HASH) {
            this.setText(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == ToolTipElement.BACKGROUND_COLOR_HASH) {
            this.setBackgroundColor(cl.convertToColor(value));
        }
        else if (hash == ToolTipElement.BORDER_COLOR_HASH) {
            this.setBorderColor(cl.convertToColor(value));
        }
        else if (hash == ToolTipElement.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ToolTipElement.HOT_POINT_POSITION_HASH) {
            this.setHotPointPosition(BackgroundedTextHotPointPosition.value(value));
        }
        else if (hash == ToolTipElement.MAX_WIDTH_HASH) {
            this.setMaxWidth(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ToolTipElement.POSITION_HASH) {
            this.setPosition(Alignment9.value(value));
        }
        else if (hash == ToolTipElement.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != ToolTipElement.Y_OFFSET_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ToolTipElement.TEXT_HASH) {
            this.setText(String.valueOf(value));
        }
        else if (hash == ToolTipElement.BACKGROUND_COLOR_HASH) {
            this.setBackgroundColor((Color)value);
        }
        else if (hash == ToolTipElement.BORDER_COLOR_HASH) {
            this.setBorderColor((Color)value);
        }
        else if (hash == ToolTipElement.DURATION_HASH) {
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ToolTipElement.HOT_POINT_POSITION_HASH) {
            this.setHotPointPosition((BackgroundedTextHotPointPosition)value);
        }
        else if (hash == ToolTipElement.MAX_WIDTH_HASH) {
            this.setMaxWidth(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ToolTipElement.POSITION_HASH) {
            this.setPosition((Alignment9)value);
        }
        else if (hash == ToolTipElement.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != ToolTipElement.Y_OFFSET_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_textColor = null;
        this.m_backgroundColor = null;
        this.m_borderColor = null;
    }
    
    static {
        ToolTipElement.DEFAULT_TEXT_COLOR = Color.BLACK;
        ToolTipElement.DEFAULT_BACKGROUND_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.7f);
        ToolTipElement.DEFAULT_BORDER_COLOR = new Color(0.06f, 0.04f, 0.03f, 0.4f);
        ToolTipElement.DEFAULT_BORDER_WIDTH = 1.5f;
        TEXT_HASH = "text".hashCode();
        BACKGROUND_COLOR_HASH = "backgroundColor".hashCode();
        BORDER_COLOR_HASH = "borderColor".hashCode();
        BORDER_WIDTH_HASH = "borderWidth".hashCode();
        DURATION_HASH = "duration".hashCode();
        HOT_POINT_POSITION_HASH = "hotPointPosition".hashCode();
        MAX_WIDTH_HASH = "maxWidth".hashCode();
        POSITION_HASH = "position".hashCode();
        TEXT_COLOR_HASH = "textColor".hashCode();
        X_OFFSET_HASH = "xOffset".hashCode();
        Y_OFFSET_HASH = "yOffset".hashCode();
    }
}
