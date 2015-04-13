package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.text.*;

public final class EngineStats
{
    private static final boolean DEBUG_MODE = false;
    private static final EngineStats m_instance;
    private ArrayList<MemoryObjectPool> m_pools;
    private float m_poolsX;
    private float m_poolsY;
    private HashMap<String, Integer> m_numGeometries;
    private int m_numBatches;
    private ScreenText m_textRenderer;
    
    public static EngineStats getInstance() {
        return EngineStats.m_instance;
    }
    
    public void monitorPool(final MemoryObjectPool pool) {
    }
    
    public final void resetCounters() {
        if (this.m_numGeometries != null) {
            this.m_numGeometries.clear();
        }
        this.m_numBatches = 0;
    }
    
    public void addRenderedGeometry(final Geometry geometry) {
    }
    
    public void render(final Renderer renderer) {
    }
    
    public void addBatchCounter() {
    }
    
    private EngineStats() {
        super();
        this.resetCounters();
    }
    
    private void drawPoolInfo(final Renderer renderer, final MemoryObjectPool pool) {
        final float usedRatio = 200.0f * pool.getNumUsed() / pool.getSize();
        renderer.drawRect(this.m_poolsX, this.m_poolsY, usedRatio, 14.0f, -12582912);
        renderer.drawRect(this.m_poolsX + usedRatio, this.m_poolsY, 200.0f - usedRatio, 14.0f, -16760832);
        this.m_textRenderer.drawLine(pool.getClassType().getSimpleName() + "(" + pool.getNumUsed() + "/" + pool.getSize() + ")");
        this.m_poolsY -= 16.0f;
    }
    
    static {
        m_instance = new EngineStats();
    }
    
    public static class ScreenText
    {
        private TextRenderer m_textRenderer;
        private int m_lineHeight;
        private int m_x;
        private int m_y;
        
        public ScreenText(final String fontName, final int style, final int size) {
            this(FontFactory.createFont(fontName, style, size));
        }
        
        public ScreenText() {
            this("COPRGTL", 4, 11);
        }
        
        public ScreenText(final Font font) {
            super();
            this.m_textRenderer = null;
            (this.m_textRenderer = TexturedFontRendererFactory.createTextRenderer(font)).setColor(0.8f, 0.8f, 0.8f, 1.0f);
        }
        
        public void prepare(final Renderer renderer, final int x, final int y) {
            this.m_textRenderer.begin3DRendering();
            this.m_lineHeight = this.m_textRenderer.getMaxCharacterHeight();
            this.m_textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            this.m_x = x - renderer.getViewportWidth() / 2;
            this.m_y = (int)(1.5f * renderer.getViewportHeight()) - y - this.m_lineHeight;
        }
        
        public void drawLine(final String text) {
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
}
