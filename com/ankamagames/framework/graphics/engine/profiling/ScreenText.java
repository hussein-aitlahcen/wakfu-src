package com.ankamagames.framework.graphics.engine.profiling;

import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.engine.*;

public class ScreenText
{
    private TextRenderer m_textRenderer;
    private int m_lineHeight;
    private int m_x;
    private int m_y;
    
    public ScreenText(final String fontName, final int style, final int size) {
        super();
        this.m_textRenderer = null;
        final Font font = FontFactory.createFont(fontName, style, size);
        if (font != null) {
            if (font instanceof BMFTexturedFont) {
                ((BMFTexturedFont)font).setMonospaced();
            }
            this.m_textRenderer = TexturedFontRendererFactory.createTextRenderer(font);
        }
        this.m_textRenderer.setColor(0.8f, 0.8f, 0.8f, 1.0f);
    }
    
    public void prepare(final Renderer renderer, final int x, final int y) {
        this.m_textRenderer.begin3DRendering();
        this.m_lineHeight = this.m_textRenderer.getMaxCharacterHeight();
        this.m_x = x - renderer.getViewportWidth() / 2;
        this.m_y = (int)(0.5f * renderer.getViewportHeight()) - y - this.m_lineHeight;
    }
    
    public void drawLine(final String text) {
        this.drawLine(text, null);
    }
    
    public void drawLine(final String text, final float[] color) {
        if (color != null) {
            this.m_textRenderer.setColor(color[0], color[1], color[2], color[3]);
        }
        else {
            this.m_textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        this.m_textRenderer.draw(text.toCharArray(), this.m_x, this.m_y);
        this.m_y -= this.m_lineHeight;
    }
    
    public void drawText(final String text, final int x, final int y) {
        this.m_textRenderer.draw(text.toCharArray(), this.m_x + x, this.m_y - y);
    }
    
    public void end() {
        this.m_textRenderer.endRendering();
    }
}
