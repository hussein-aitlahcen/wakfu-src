package com.ankamagames.framework.graphics.engine.text;

import java.awt.geom.*;
import java.awt.font.*;
import java.awt.*;

public interface RenderDelegate
{
    boolean intensityOnly();
    
    Rectangle2D getBounds(String p0, Font p1, FontRenderContext p2);
    
    Rectangle2D getBounds(CharSequence p0, Font p1, FontRenderContext p2);
    
    Rectangle2D getBounds(GlyphVector p0, FontRenderContext p1);
    
    void draw(Graphics2D p0, String p1, int p2, int p3);
    
    void drawGlyphVector(Graphics2D p0, GlyphVector p1, int p2, int p3);
    
    int getBorder();
}
