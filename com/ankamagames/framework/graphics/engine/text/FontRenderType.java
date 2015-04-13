package com.ankamagames.framework.graphics.engine.text;

import java.util.regex.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import java.net.*;
import java.io.*;
import java.util.*;

public enum FontRenderType
{
    BMF_TEXTURED, 
    TEXTURED, 
    AWT_FILE_FONT, 
    AWT_SYSTEM_FONT;
    
    private static final Pattern FONT_SEPARATOR;
    private static FontRenderType[] m_orderedFonts;
    
    private Font _createFont(final FontData fontData, final String path, final boolean enableAWTFont) {
        switch (this) {
            case BMF_TEXTURED: {
                try {
                    final BMFTexturedFont texturedFont = new BMFTexturedFont();
                    texturedFont.load(fontData.toAWTFontName(), path);
                    texturedFont.setDeltaXY(fontData.getDeltaX(), fontData.getDeltaY());
                    return texturedFont;
                }
                catch (IOException e2) {
                    break;
                }
            }
            case TEXTURED: {
                try {
                    final TexturedFont texturedFont2 = new TexturedFont();
                    texturedFont2.load(fontData.toAWTFontName(), path);
                    texturedFont2.setDeltaXY(fontData.getDeltaX(), fontData.getDeltaY());
                    return texturedFont2;
                }
                catch (IOException e2) {
                    break;
                }
            }
            case AWT_FILE_FONT: {
                if (enableAWTFont) {
                    try {
                        final URL url = ContentFileHelper.getURL(path + fontData.getName() + ".ttf");
                        if (URLUtils.urlExists(url)) {
                            final InputStream stream = new BufferedInputStream(url.openStream());
                            java.awt.Font font = java.awt.Font.createFont(0, stream);
                            stream.close();
                            final java.awt.Font tmpFont = java.awt.Font.decode(fontData.toAWTFontName());
                            font = font.deriveFont(tmpFont.getStyle(), tmpFont.getSize());
                            final AWTFont awtFont = new AWTFont(font, true, fontData.isBordered());
                            awtFont.setDeltaXY(fontData.getDeltaX(), fontData.getDeltaY());
                            return awtFont;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }
            case AWT_SYSTEM_FONT: {
                if (enableAWTFont) {
                    final AWTFont awtFont2 = new AWTFont(java.awt.Font.decode(fontData.toAWTFontName()), true, fontData.isBordered());
                    awtFont2.setDeltaXY(fontData.getDeltaX(), fontData.getDeltaY());
                    return awtFont2;
                }
                break;
            }
        }
        return null;
    }
    
    public static Font createFont(final FontData fontData, final String path, final boolean enableAWTFont) {
        for (int i = 0, size = FontRenderType.m_orderedFonts.length; i < size; ++i) {
            final Font font = FontRenderType.m_orderedFonts[i]._createFont(fontData, path, enableAWTFont);
            if (font != null) {
                return font;
            }
        }
        return null;
    }
    
    public static void sortRenderTypes(final Comparator<FontRenderType> comparator) {
        Arrays.sort(FontRenderType.m_orderedFonts, comparator);
    }
    
    static {
        FONT_SEPARATOR = Pattern.compile("-");
        FontRenderType.m_orderedFonts = new FontRenderType[values().length];
        final FontRenderType[] values = values();
        for (int i = 0, size = values.length; i < size; ++i) {
            FontRenderType.m_orderedFonts[i] = values[i];
        }
    }
}
