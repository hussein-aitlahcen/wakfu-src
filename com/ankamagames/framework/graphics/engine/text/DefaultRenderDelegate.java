package com.ankamagames.framework.graphics.engine.text;

import java.awt.geom.*;
import java.awt.font.*;
import java.awt.*;

public class DefaultRenderDelegate implements RenderDelegate
{
    @Override
    public boolean intensityOnly() {
        return true;
    }
    
    @Override
    public Rectangle2D getBounds(final CharSequence str, final Font font, final FontRenderContext frc) {
        final GlyphVector glyphVector = GlyphVectorCache.INSTANCE.getGlyphVector(str.toString(), font, frc);
        return this.getBounds(glyphVector, frc);
    }
    
    @Override
    public Rectangle2D getBounds(final String str, final Font font, final FontRenderContext frc) {
        final GlyphVector glyphVector = font.createGlyphVector(frc, str);
        return this.getBounds(glyphVector, frc);
    }
    
    @Override
    public Rectangle2D getBounds(final GlyphVector gv, final FontRenderContext frc) {
        return gv.getLogicalBounds();
    }
    
    @Override
    public void drawGlyphVector(final Graphics2D graphics, final GlyphVector str, final int x, final int y) {
        graphics.drawGlyphVector(str, x, y);
    }
    
    @Override
    public void draw(final Graphics2D graphics, final String str, final int x, final int y) {
        graphics.drawString(str, x, y);
    }
    
    @Override
    public int getBorder() {
        return 0;
    }
}
