package com.ankamagames.framework.graphics.image;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AlphaMask
{
    protected final ByteArrayBitSet m_mask;
    protected int m_layerWidth;
    protected byte m_resize;
    
    public AlphaMask(final AlphaMask alphaMask) {
        super();
        this.m_resize = 1;
        this.m_mask = new ByteArrayBitSet(alphaMask.m_mask);
        this.m_layerWidth = alphaMask.m_layerWidth;
        this.m_resize = alphaMask.m_resize;
    }
    
    public AlphaMask(final Layer layer, final int alphaLevel) {
        super();
        this.m_resize = 1;
        final int size = layer.getWidth() * layer.getHeight();
        this.m_mask = new ByteArrayBitSet(size);
        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();
        for (int i = 0; i < layerWidth; ++i) {
            for (int j = 0; j < layerHeight; ++j) {
                this.m_mask.set(j * layerWidth + i, layer.getAlpha(i, j) >= alphaLevel);
            }
        }
        this.m_layerWidth = layerWidth;
    }
    
    public AlphaMask(final byte[] mask, final int offset, final int imageWidth, final int size, final byte resize) {
        super();
        this.m_resize = 1;
        this.m_layerWidth = MathHelper.fastCeil(imageWidth / resize);
        this.m_mask = ByteArrayBitSet.fromByteArray(mask, offset, size);
        this.m_resize = resize;
    }
    
    public AlphaMask(final byte[] mask, final int imageWidth, final byte resize) {
        super();
        this.m_resize = 1;
        this.m_layerWidth = MathHelper.fastCeil(imageWidth / resize);
        this.m_mask = ByteArrayBitSet.wrap(mask);
        this.m_resize = resize;
    }
    
    public void setValue(final int i, final boolean b) {
        this.m_mask.set(i, b);
    }
    
    public void setValue(final int x, final int y, final boolean b) {
        this.m_mask.set(y / this.m_resize * this.m_layerWidth + x / this.m_resize, b);
    }
    
    public boolean getValue(final int x, final int y) {
        return this.m_mask.get(y / this.m_resize * this.m_layerWidth + x / this.m_resize);
    }
}
