package com.ankamagames.framework.graphics.engine.text;

@Deprecated
public class AWTFont implements Font
{
    private final java.awt.Font m_AWTFont;
    private final boolean m_isAntiAliased;
    private final boolean m_isBlured;
    private int m_deltaX;
    private int m_deltaY;
    
    public AWTFont(final java.awt.Font font, final boolean isAntiAliased, final boolean isBlured) {
        super();
        this.m_AWTFont = font;
        this.m_isAntiAliased = isAntiAliased;
        this.m_isBlured = isBlured;
    }
    
    @Override
    public Font createDerivateFont(final int fontStyle, final float size, final boolean enableAWTFont) {
        if (!FontFactory.enableAWTFont(enableAWTFont)) {
            return null;
        }
        final java.awt.Font font = this.m_AWTFont.deriveFont(fontStyle, size);
        final AWTFont awtFont = new AWTFont(font, this.isAntiAliased(), this.isBlured());
        awtFont.setDeltaXY(this.m_deltaX, this.m_deltaY);
        return awtFont;
    }
    
    @Override
    public float getSize() {
        return this.m_AWTFont.getSize2D();
    }
    
    @Override
    public int getStyle() {
        int style = 0;
        if (this.m_AWTFont.isBold()) {
            style |= 0x1;
        }
        if (this.m_AWTFont.isItalic()) {
            style |= 0x2;
        }
        return style;
    }
    
    @Override
    public short getBorderSize() {
        return 0;
    }
    
    public final java.awt.Font getAWTFont() {
        return this.m_AWTFont;
    }
    
    public final boolean isAntiAliased() {
        return this.m_isAntiAliased;
    }
    
    public boolean isBlured() {
        return this.m_isBlured;
    }
    
    @Override
    public void setDeltaXY(final int x, final int y) {
        this.m_deltaX = x;
        this.m_deltaY = y;
    }
    
    public int getDeltaX() {
        return this.m_deltaX;
    }
    
    public int getDeltaY() {
        return this.m_deltaY;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AWTFont awtFont = (AWTFont)o;
        if (this.m_deltaX != awtFont.m_deltaX) {
            return false;
        }
        if (this.m_deltaY != awtFont.m_deltaY) {
            return false;
        }
        if (this.m_isAntiAliased != awtFont.m_isAntiAliased) {
            return false;
        }
        if (this.m_isBlured != awtFont.m_isBlured) {
            return false;
        }
        if (this.m_AWTFont != null) {
            if (this.m_AWTFont.equals(awtFont.m_AWTFont)) {
                return true;
            }
        }
        else if (awtFont.m_AWTFont == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_AWTFont != null) ? this.m_AWTFont.hashCode() : 0;
        result = 31 * result + (this.m_isAntiAliased ? 1 : 0);
        result = 31 * result + (this.m_isBlured ? 1 : 0);
        result = 31 * result + this.m_deltaX;
        result = 31 * result + this.m_deltaY;
        return result;
    }
}
