package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.transformer.*;

public class Sea
{
    static final TIntHashSet WATER_ELEMENTS;
    public float m_amplitude;
    public float m_speed;
    public float m_width;
    private float m_position;
    
    public Sea() {
        super();
        this.m_amplitude = 0.001f;
        this.m_speed = 2.0f;
        this.m_width = 10.0f;
        this.m_position = 0.0f;
    }
    
    public void update(final DisplayedScreenWorld world, final int deltaTime) {
        this.m_position += deltaTime;
        final ArrayList<DisplayedScreenMap> maps = world.getMaps();
        for (int i = maps.size() - 1; i >= 0; --i) {
            final DisplayedScreenMap map = maps.get(i);
            if (map != null) {
                final DisplayedScreenElement[] elts = map.getElements();
                if (elts != null) {
                    for (final DisplayedScreenElement elt : elts) {
                        if (elt.isVisible()) {
                            final int id = elt.getElement().getCommonProperties().getId();
                            if (Sea.WATER_ELEMENTS.contains(id)) {
                                final float x = elt.getWorldCellX();
                                final float y = elt.getWorldCellY();
                                final float d = (float)Math.sqrt(x * x + y * y) + this.m_speed * this.m_position / 1000.0f;
                                float amplitude = this.m_amplitude * MathHelper.sinf(6.2831855f * d / this.m_width);
                                if (amplitude < 0.0f) {
                                    amplitude *= 0.2f;
                                }
                                final BatchTransformer batchTransformer = elt.getEntitySprite().getTransformer();
                                final float tx = batchTransformer.getMatrix().getBuffer()[12];
                                final float ty = batchTransformer.getMatrix().getBuffer()[13];
                                batchTransformer.getMatrix().set(new float[] { 1.0f + amplitude, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f + amplitude, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, tx, ty, 0.0f, 1.0f });
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        (WATER_ELEMENTS = new TIntHashSet()).add(10208);
        Sea.WATER_ELEMENTS.add(11138);
        Sea.WATER_ELEMENTS.add(10036);
        Sea.WATER_ELEMENTS.add(10211);
        Sea.WATER_ELEMENTS.add(17702);
        Sea.WATER_ELEMENTS.add(17704);
        Sea.WATER_ELEMENTS.add(17712);
        Sea.WATER_ELEMENTS.add(17713);
        Sea.WATER_ELEMENTS.add(17755);
        Sea.WATER_ELEMENTS.add(17756);
    }
}
