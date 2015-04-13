package com.ankamagames.wakfu.client.ui.swt;

public class SWTStaticLayoutData
{
    public static final byte ALIGN_CENTER = 1;
    public static final byte ALIGN_NORTH_EAST = 2;
    public static final byte ALIGN_NORTH = 3;
    public static final float SIZE_TO_PREF_SIZE = -2.0f;
    private final float m_widthPercentage;
    private final float m_heightPercentage;
    private final byte m_align;
    
    public SWTStaticLayoutData(final float widthPercentage, final float heightPercentage, final byte align) {
        super();
        this.m_widthPercentage = widthPercentage;
        this.m_heightPercentage = heightPercentage;
        this.m_align = align;
    }
    
    public int getWidth(final int parentWidth, final int prefWidth) {
        if (this.m_widthPercentage == -2.0f) {
            return prefWidth;
        }
        return (int)(parentWidth * this.m_widthPercentage);
    }
    
    public int getHeight(final int parentHeight, final int prefHeight) {
        if (this.m_heightPercentage == -2.0f) {
            return prefHeight;
        }
        return (int)(parentHeight * this.m_heightPercentage);
    }
    
    public int getPositionX(final int width, final int parentWidth) {
        switch (this.m_align) {
            case 1:
            case 3: {
                return (int)Math.round((parentWidth - width) * 0.5);
            }
            case 2: {
                return Math.max(0, parentWidth - width);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getPositionY(final int height, final int parentHeight) {
        switch (this.m_align) {
            case 1: {
                return (int)Math.round((parentHeight - height) * 0.5);
            }
            case 2:
            case 3: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
}
