package com.ankamagames.framework.graphics.engine.text;

public interface Font
{
    public static final int FONT_STYLE_PLAIN = 0;
    public static final int FONT_STYLE_BOLD = 1;
    public static final int FONT_STYLE_ITALIC = 2;
    public static final int FONT_STYLE_BORDERED = 4;
    
    Font createDerivateFont(int p0, float p1, boolean p2);
    
    float getSize();
    
    int getStyle();
    
    short getBorderSize();
    
    void setDeltaXY(int p0, int p1);
}
