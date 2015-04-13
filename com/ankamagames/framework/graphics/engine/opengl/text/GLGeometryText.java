package com.ankamagames.framework.graphics.engine.opengl.text;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class GLGeometryText extends GeometryText
{
    public static final ObjectFactory Factory;
    private TextRenderer m_renderer;
    
    @Override
    public int getLineWidth(final String line) {
        return this.m_renderer.getLineWidth(line);
    }
    
    @Override
    public int getLineHeight(final String line) {
        return this.m_renderer.getLineHeight(line);
    }
    
    @Override
    public Point2i getLineDimensions(final String line) {
        return new Point2i(this.getLineWidth(line), this.getLineHeight(line));
    }
    
    @Override
    public void setFont(final Font font) {
        if (font == this.m_font) {
            return;
        }
        this.m_renderer = TexturedFontRendererFactory.createTextRenderer(font);
        super.setFont(font);
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    @Override
    public void render(final Renderer renderer) {
        if (this.m_renderer == null) {
            return;
        }
        final int numLines = this.m_lines.size();
        if (numLines == 0) {
            return;
        }
        EngineStats.getInstance().addRenderedGeometry(this);
        this.m_renderer.setColor(this.m_color.getRed(), this.m_color.getGreen(), this.m_color.getBlue(), this.m_color.getAlpha());
        final float x = this.m_alignOffsetX * this.m_scale + this.m_offsetX;
        float y = (this.m_alignOffsetY + numLines * this.m_lineHeight) * this.m_scale + this.m_offsetY;
        this.m_renderer.beginRendering(0, 0);
        for (int i = 0; i < numLines; ++i) {
            final char[] line = this.m_lines.get(i);
            y -= this.m_lineHeight * this.m_scale;
            this.m_renderer.draw(line, x, y, this.m_scale);
        }
        this.m_renderer.endRendering();
    }
    
    @Override
    protected void checkin() {
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<GLGeometryText>
    {
        public ObjectFactory() {
            super(GLGeometryText.class);
        }
        
        @Override
        public GLGeometryText create() {
            return new GLGeometryText(null);
        }
    }
}
