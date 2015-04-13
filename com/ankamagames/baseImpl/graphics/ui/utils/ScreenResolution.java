package com.ankamagames.baseImpl.graphics.ui.utils;

import java.awt.*;

public class ScreenResolution
{
    private int m_screenWidth;
    private int m_screenHeight;
    private int m_screenBpp;
    private String m_screenComment;
    
    public ScreenResolution(final int screenWidth, final int screenHeight, final int screenBpp, final String screenComment) {
        super();
        this.m_screenWidth = screenWidth;
        this.m_screenHeight = screenHeight;
        this.m_screenBpp = screenBpp;
        this.m_screenComment = screenComment;
    }
    
    public final int getScreenWidth() {
        return this.m_screenWidth;
    }
    
    public final void setScreenWidth(final int screenWidth) {
        this.m_screenWidth = screenWidth;
    }
    
    public final int getScreenHeight() {
        return this.m_screenHeight;
    }
    
    public final void setScreenHeight(final int screenHeight) {
        this.m_screenHeight = screenHeight;
    }
    
    public final int getScreenBpp() {
        return this.m_screenBpp;
    }
    
    public final void setScreenBpp(final int screenBpp) {
        this.m_screenBpp = screenBpp;
    }
    
    @Override
    public final String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.m_screenWidth).append("x").append(this.m_screenHeight);
        if (this.m_screenBpp > 0) {
            buffer.append(" (").append(this.m_screenBpp).append(")");
        }
        if (this.m_screenComment != null && !this.m_screenComment.isEmpty()) {
            buffer.append(" (").append(this.m_screenComment).append(")");
        }
        return buffer.toString();
    }
    
    public static ScreenResolution getCurrentResolution() {
        final Toolkit t = Toolkit.getDefaultToolkit();
        final Dimension screenSize = t.getScreenSize();
        final int w = screenSize.width;
        final int h = screenSize.height;
        final int bpp = t.getColorModel().getPixelSize();
        return new ScreenResolution(w, h, bpp, "courante");
    }
}
