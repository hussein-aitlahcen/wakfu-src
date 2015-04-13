package com.ankamagames.framework.graphics.engine.text;

import gnu.trove.*;

public class TexturedFontRendererFactory
{
    private static final THashMap<Font, TextRenderer> m_renderers;
    
    public static Font createFont(final FontData fontData, final String path, final boolean enableAWTFont) {
        return FontRenderType.createFont(fontData, path, enableAWTFont);
    }
    
    public static TextRenderer createTextRenderer(final Font font) {
        final TextRenderer textRenderer = TexturedFontRendererFactory.m_renderers.get(font);
        if (textRenderer != null) {
            return textRenderer;
        }
        if (font.getClass() == TexturedFont.class) {
            final TexturedTextRenderer renderer = new TexturedTextRenderer();
            renderer.setFont((TexturedFont)font);
            TexturedFontRendererFactory.m_renderers.put(font, renderer);
            return renderer;
        }
        if (font.getClass() == BMFTexturedFont.class) {
            final BMFTexturedTextRenderer renderer2 = new BMFTexturedTextRenderer();
            renderer2.setFont((BMFTexturedFont)font);
            TexturedFontRendererFactory.m_renderers.put(font, renderer2);
            return renderer2;
        }
        if (font instanceof AWTFont) {
            final AWTFont awtFont = (AWTFont)font;
            BluredTextRenderer renderer3;
            if (awtFont.isBlured()) {
                renderer3 = new BluredTextRenderer(awtFont.getAWTFont(), awtFont.isAntiAliased(), false, BlurRenderDelegate.BLUR_RENDER_DELEGATE, awtFont.getDeltaX(), awtFont.getDeltaY());
            }
            else {
                renderer3 = new BluredTextRenderer(awtFont.getAWTFont(), awtFont.isAntiAliased(), false, awtFont.getDeltaX(), awtFont.getDeltaY());
            }
            renderer3.setUseVertexArrays(false);
            TexturedFontRendererFactory.m_renderers.put(font, renderer3);
            return renderer3;
        }
        return null;
    }
    
    static {
        m_renderers = new THashMap<Font, TextRenderer>();
    }
}
