package com.ankamagames.framework.graphics.engine.text;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class BlurRenderDelegate implements RenderDelegate
{
    public static BlurRenderDelegate BLUR_RENDER_DELEGATE;
    private static Color BLUR_COLOR;
    
    @Override
    public void draw(final Graphics2D graphics, final String text, int x, int y) {
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        ++x;
        ++y;
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, x + 1, y);
        graphics.drawString(text, x + 1, y + 1);
        graphics.drawString(text, x, y + 1);
        graphics.drawString(text, x - 1, y);
        graphics.drawString(text, x - 1, y - 1);
        graphics.drawString(text, x, y - 1);
        graphics.drawString(text, x + 1, y - 1);
        graphics.drawString(text, x - 1, y + 1);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, x, y);
    }
    
    @Override
    public void drawGlyphVector(final Graphics2D graphics, final GlyphVector str, int x, int y) {
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        ++x;
        ++y;
        graphics.setColor(Color.BLACK);
        graphics.drawGlyphVector(str, x + 1, y);
        graphics.drawGlyphVector(str, x + 1, y + 1);
        graphics.drawGlyphVector(str, x, y + 1);
        graphics.drawGlyphVector(str, x - 1, y);
        graphics.drawGlyphVector(str, x - 1, y - 1);
        graphics.drawGlyphVector(str, x, y - 1);
        graphics.drawGlyphVector(str, x + 1, y - 1);
        graphics.drawGlyphVector(str, x - 1, y + 1);
        graphics.setColor(Color.WHITE);
        graphics.drawGlyphVector(str, x, y);
    }
    
    @Override
    public boolean intensityOnly() {
        return false;
    }
    
    @Override
    public Rectangle2D getBounds(final CharSequence str, final Font font, final FontRenderContext frc) {
        final GlyphVector glyphVector = GlyphVectorCache.INSTANCE.getGlyphVector(str.toString(), font, frc);
        return this.getBounds(glyphVector, frc);
    }
    
    @Override
    public Rectangle2D getBounds(final String str, final Font font, final FontRenderContext frc) {
        final GlyphVector glyphVector = GlyphVectorCache.INSTANCE.getGlyphVector(str, font, frc);
        return this.getBounds(glyphVector, frc);
    }
    
    @Override
    public Rectangle2D getBounds(final GlyphVector gv, final FontRenderContext frc) {
        final Rectangle2D ret = gv.getLogicalBounds();
        ret.setRect(ret.getX(), ret.getY(), ret.getWidth() + 2.0, ret.getHeight() + 2.0);
        return ret;
    }
    
    @Override
    public int getBorder() {
        return 1;
    }
    
    static {
        BlurRenderDelegate.BLUR_RENDER_DELEGATE = new BlurRenderDelegate();
        BlurRenderDelegate.BLUR_COLOR = new Color(0.2f, 0.2f, 0.2f, 1.0f);
    }
}
