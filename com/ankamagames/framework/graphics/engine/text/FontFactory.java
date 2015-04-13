package com.ankamagames.framework.graphics.engine.text;

import org.apache.log4j.*;
import java.util.*;

public class FontFactory
{
    private static final Logger m_logger;
    public static final byte AWT_FONT_ON_DEMAND = 0;
    public static final byte FORCE_DISABLE_AWT_FONT = 1;
    public static final byte FORCE_ENABLE_AWT_FONT = 2;
    private static final HashMap<FontData, Font> m_fonts;
    private static byte m_enableAWTFontMode;
    private static String m_fontPath;
    
    public static void setEnableAWTFontMode(final byte enableAWTFontMode) {
        FontFactory.m_enableAWTFontMode = enableAWTFontMode;
    }
    
    public static void setFontPath(final String path) {
        FontFactory.m_fontPath = path;
    }
    
    public static Font createFont(final FontData name, final boolean enableAWTFont) {
        final Font font = getFont(name);
        if (font == null) {
            return createNewFont(name, enableAWTFont);
        }
        return font;
    }
    
    public static Font createFont(final String name) {
        return createFont(FontData.extract(name), false);
    }
    
    public static Font createFont(final String name, final boolean enableAWTFont) {
        return createFont(FontData.extract(name), enableAWTFont);
    }
    
    public static Font createFont(final String name, final int style, final int size) {
        return createFont(name, style, size, false);
    }
    
    public static Font createFont(final String name, final int style, final int size, final boolean enableAWTFont) {
        return createFont(name, style, size, 0, 0, enableAWTFont);
    }
    
    public static Font createFont(final String name, final int style, final int size, final int deltaX, final int deltaY, final boolean enableAWTFont) {
        final FontData fontData = new FontData(name, style, size, deltaX, deltaY);
        return createFont(fontData, enableAWTFont);
    }
    
    public static int getStyle(final String name) {
        int style = 0;
        if (name == null) {
            return style;
        }
        final String fontName = name.toLowerCase();
        if (fontName.contains("bold")) {
            style |= 0x1;
        }
        if (fontName.contains("italic")) {
            style |= 0x2;
        }
        if (fontName.contains("bordered")) {
            style |= 0x4;
        }
        return style;
    }
    
    public static int getSize(final String name) {
        final int sizeStart = name.lastIndexOf(45) + 1;
        if (sizeStart == 0) {
            return 0;
        }
        final String sizeString = name.substring(sizeStart);
        try {
            return Integer.parseInt(sizeString);
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    public static String getType(final String name) {
        final int typeEnd = name.indexOf(45);
        if (typeEnd <= 0) {
            return name;
        }
        return name.substring(0, typeEnd);
    }
    
    private static Font getFont(final FontData name) {
        return FontFactory.m_fonts.get(name);
    }
    
    private static Font createNewFont(final FontData fontData, final boolean enableAWTFont) {
        final boolean enableAWT = enableAWTFont(enableAWTFont);
        Font font = TexturedFontRendererFactory.createFont(fontData, FontFactory.m_fontPath, enableAWT);
        if (font == null) {
            FontFactory.m_logger.error((Object)("Unable to load the font " + fontData + " from path " + FontFactory.m_fontPath));
            font = getNearestFont(fontData);
        }
        FontFactory.m_fonts.put(fontData, font);
        return font;
    }
    
    public static boolean enableAWTFont(final boolean enableAWTFont) {
        boolean enableAWT = false;
        switch (FontFactory.m_enableAWTFontMode) {
            case 2: {
                enableAWT = true;
                break;
            }
            case 1: {
                enableAWT = false;
                break;
            }
            default: {
                enableAWT = enableAWTFont;
                break;
            }
        }
        return enableAWT;
    }
    
    private static Font getNearestFont(final FontData data) {
        Font nearestFont = null;
        float nearestDistance = Float.MAX_VALUE;
        for (final Map.Entry<FontData, Font> entry : FontFactory.m_fonts.entrySet()) {
            final FontData fontData = entry.getKey();
            final float distance = getDistance(data, fontData);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestFont = entry.getValue();
            }
        }
        return nearestFont;
    }
    
    private static float getDistance(final FontData src, final FontData dest) {
        final String srcType = src.getName();
        final int srcSize = src.getSize();
        final int srcStyle = src.getStyle();
        final int srcDeltaX = src.getDeltaX();
        final int srcDeltaY = src.getDeltaY();
        final String destType = dest.getName();
        final int destSize = dest.getSize();
        final int destStyle = dest.getStyle();
        final int destDeltaX = dest.getDeltaX();
        final int destDeltaY = dest.getDeltaY();
        float distance = 0.0f;
        if (!destType.equals(srcType)) {
            distance += 2.0f;
        }
        distance += Math.abs(srcSize - destSize);
        if (destStyle != srcStyle) {
            distance += 4.0f;
        }
        distance += Math.abs(srcDeltaX - destDeltaX) / 4.0f;
        distance += Math.abs(srcDeltaY - destDeltaY) / 4.0f;
        return distance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FontFactory.class);
        m_fonts = new HashMap<FontData, Font>();
        FontFactory.m_enableAWTFontMode = 0;
        FontFactory.m_fontPath = "";
    }
}
