package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.java.util.*;
import java.util.regex.*;

public class FontData
{
    public static final Pattern FONT_NAME_PATTERN;
    private final String m_name;
    private final int m_style;
    private final int m_size;
    private final int m_deltaX;
    private final int m_deltaY;
    
    public FontData(final String name, final int style, final int size, final int deltaX, final int deltaY) {
        super();
        this.m_name = name;
        this.m_style = style;
        this.m_size = size;
        this.m_deltaX = deltaX;
        this.m_deltaY = deltaY;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public int getStyle() {
        return this.m_style;
    }
    
    public int getSize() {
        return this.m_size;
    }
    
    public int getDeltaX() {
        return this.m_deltaX;
    }
    
    public int getDeltaY() {
        return this.m_deltaY;
    }
    
    public boolean isBordered() {
        return (this.m_style & 0x4) != 0x0;
    }
    
    public String toAWTFontName() {
        return build(this.m_name, this.m_style, this.m_size, 0, 0);
    }
    
    @Override
    public String toString() {
        return build(this.m_name, this.m_style, this.m_size, this.m_deltaX, this.m_deltaY);
    }
    
    public static String build(final String name, final int style, final int size, final int deltaX, final int deltaY) {
        final StringBuilder sb = new StringBuilder(name.toLowerCase());
        sb.append('-');
        if ((style & 0x1) != 0x0) {
            sb.append("bold");
        }
        if ((style & 0x2) != 0x0) {
            sb.append("italic");
        }
        if ((style & 0x4) != 0x0) {
            sb.append("bordered");
        }
        if (style == 0) {
            sb.append("plain");
        }
        sb.append('-');
        sb.append(size);
        if (deltaX != 0 || deltaY != 0) {
            sb.append('#').append(deltaX);
            sb.append('#').append(deltaY);
        }
        return sb.toString();
    }
    
    public static FontData extract(final String fullName) {
        final Matcher matcher = FontData.FONT_NAME_PATTERN.matcher(fullName);
        if (matcher.matches()) {
            final String name = matcher.group(2);
            final int style = FontFactory.getStyle(matcher.group(3));
            final int fontSize = PrimitiveConverter.getInteger(matcher.group(4), 10);
            final int deltaX = PrimitiveConverter.getInteger(matcher.group(6), 0);
            final int deltaY = PrimitiveConverter.getInteger(matcher.group(7), 0);
            return new FontData(name, style, fontSize, deltaX, deltaY);
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FontData fontData = (FontData)o;
        if (this.m_deltaX != fontData.m_deltaX) {
            return false;
        }
        if (this.m_deltaY != fontData.m_deltaY) {
            return false;
        }
        if (this.m_size != fontData.m_size) {
            return false;
        }
        if (this.m_style != fontData.m_style) {
            return false;
        }
        if (this.m_name != null) {
            if (this.m_name.equals(fontData.m_name)) {
                return true;
            }
        }
        else if (fontData.m_name == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_name != null) ? this.m_name.hashCode() : 0;
        result = 31 * result + this.m_style;
        result = 31 * result + this.m_size;
        result = 31 * result + this.m_deltaX;
        result = 31 * result + this.m_deltaY;
        return result;
    }
    
    static {
        FONT_NAME_PATTERN = Pattern.compile("(([^\\-]*)-([^\\-]*)-([^#]*))(#([0-9\\+\\-]+)#([0-9\\+\\-]+))?");
    }
}
