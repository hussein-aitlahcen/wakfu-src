package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;

public class ApplicationResolution
{
    public static final Logger m_logger;
    private final int m_width;
    private final int m_height;
    private final int m_bpp;
    private final int m_frequency;
    private final Mode m_mode;
    private static final String SERIALIZATION_SEPARTOR = "x";
    
    public ApplicationResolution(final int width, final int height, final int bpp, final int frequency, final Mode mode) {
        super();
        this.m_width = width;
        this.m_height = height;
        this.m_bpp = bpp;
        this.m_frequency = frequency;
        this.m_mode = mode;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public int getBpp() {
        return this.m_bpp;
    }
    
    public int getFrequency() {
        return this.m_frequency;
    }
    
    public Mode getMode() {
        return this.m_mode;
    }
    
    @Override
    public String toString() {
        return "{Resolution : " + this.m_width + 'x' + this.m_height + 'x' + this.m_bpp + ' ' + StringUtils.capitalize(this.m_mode.name()) + ' ' + this.m_frequency + "Hz}";
    }
    
    public boolean isUndefined() {
        return this.m_mode != Mode.WINDOWED_FULLSCREEN && (this.m_width <= 0 || this.m_height <= 0);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof ApplicationResolution) {
            final ApplicationResolution oAr = (ApplicationResolution)o;
            return oAr.m_width == this.m_width && oAr.m_height == this.m_height && oAr.m_mode == this.m_mode && oAr.m_frequency == this.m_frequency && (oAr.m_bpp == this.m_bpp || oAr.m_bpp == -1 || this.m_bpp == -1);
        }
        return super.equals(o);
    }
    
    public static ApplicationResolution unserialize(String serializedRes) {
        if (serializedRes == null) {
            serializedRes = "";
        }
        final String[] parts = serializedRes.split("x");
        final int width = getSafeIntFromArray(parts, 0, 0);
        final int height = getSafeIntFromArray(parts, 1, 0);
        final int bpp = getSafeIntFromArray(parts, 2, -1);
        final int frequency = getSafeIntFromArray(parts, 3, 0);
        final int modeOrdinal = getSafeIntFromArray(parts, 4, Mode.WINDOWED.ordinal());
        Mode mode = Mode.WINDOWED;
        for (final Mode m : Mode.values()) {
            if (m.ordinal() == modeOrdinal) {
                mode = m;
                break;
            }
        }
        return new ApplicationResolution(width, height, bpp, frequency, mode);
    }
    
    private static int getSafeIntFromArray(final String[] strArray, final int index, final int defaultValue) {
        if (strArray == null || index >= strArray.length) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(strArray[index]);
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public final String serialize() {
        return this.m_width + "x" + this.m_height + "x" + this.m_bpp + "x" + this.m_frequency + "x" + this.m_mode.ordinal();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ApplicationResolution.class);
    }
    
    public enum Mode
    {
        WINDOWED, 
        WINDOWED_FULLSCREEN, 
        FULLSCREEN;
    }
}
