package com.ankamagames.framework.graphics.engine.texture;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class Texture extends Image
{
    private static final FourCC RAW_FOUR_CC;
    protected long m_name;
    protected String m_fileName;
    protected boolean m_isUsed;
    protected boolean m_isReady;
    protected TextureBlendModes m_blendMode;
    protected boolean m_keepData;
    protected boolean m_createMask;
    protected boolean m_isRenderTarget;
    protected boolean m_generateMipMaps;
    protected int m_maxRefCount;
    private static short DEFAULT_LIFE;
    private short m_life;
    protected ManagerState m_managerState;
    
    protected Texture(final long name, final String fileName, final boolean keepData) {
        super();
        this.m_managerState = ManagerState.NONE;
        this.initialize(name, fileName, keepData);
        this.m_isRenderTarget = false;
    }
    
    protected Texture(final long name, final Image image, final boolean keepData) {
        super(image);
        this.m_managerState = ManagerState.NONE;
        this.initialize(name, null, keepData);
        this.m_isRenderTarget = false;
    }
    
    protected Texture(final long name, final int width, final int height, final boolean generateMipMaps) {
        super(Texture.RAW_FOUR_CC, new Layer(width, height, (short)32, null, PrimitiveArrays.EMPTY_BYTE_ARRAY));
        this.m_managerState = ManagerState.NONE;
        this.getLayer(0).removeReference();
        this.initialize(name, null, false);
        this.m_isRenderTarget = true;
        this.m_generateMipMaps = generateMipMaps;
    }
    
    public void setCreateMask(final boolean create) {
        this.m_createMask = create;
    }
    
    public final long getName() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_name;
    }
    
    public final String getFileName() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_fileName;
    }
    
    public final boolean load(final String path) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.readFromFileAsync(path.concat(this.m_fileName));
    }
    
    public final boolean load(final boolean async) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (async) {
            return this.readFromFileAsync(this.m_fileName);
        }
        return this.readFromFile(this.m_fileName);
    }
    
    public final boolean isUsed() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_isUsed;
    }
    
    public final void setUsed(final boolean used) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_isUsed = used;
    }
    
    @Override
    public void setLayer(final int layerIndex, final Layer layer) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        super.setLayer(layerIndex, layer);
        this.m_isReady = false;
    }
    
    public final boolean isReady() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_isReady && !this.m_needUpdate;
    }
    
    public final void optimize() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        for (int i = 0; i < this.getNumLayers(); ++i) {
            final Layer layer = this.getLayer(i);
            if (layer != Texture.m_defaultLayer) {
                layer.releaseData();
            }
        }
    }
    
    public final TextureBlendModes getBlend() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_blendMode;
    }
    
    public final void setBlend(final TextureBlendModes blendMode) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        this.m_blendMode = blendMode;
    }
    
    public final boolean isRenderTarget() {
        return this.m_isRenderTarget;
    }
    
    public abstract boolean prepare(final Renderer p0);
    
    public abstract void activate(final Renderer p0);
    
    public abstract void deactivate(final Renderer p0);
    
    public abstract void enable(final Renderer p0);
    
    public abstract void disable(final Renderer p0);
    
    public abstract boolean isCompressed();
    
    public abstract boolean updateTextureData(final Renderer p0);
    
    public float getMemorySize() {
        float textureSize = MathHelper.nearestGreatestPowOfTwo(this.getSize(0).getX()) * MathHelper.nearestGreatestPowOfTwo(this.getSize(0).getY()) * 4 / 1024.0f;
        if (this.m_generateMipMaps) {
            textureSize *= 2.0f;
        }
        if (this.isCompressed()) {
            textureSize /= 4.0f;
        }
        return textureSize;
    }
    
    public int getScore() {
        int score = 0;
        if (this.m_maxRefCount == 0) {
            score = 32;
        }
        else if (this.m_maxRefCount < 256) {
            score += (int)(32.0 - 4.0 * Math.log(this.m_maxRefCount));
        }
        if (this.getLife() < -1000) {
            score += 34;
        }
        else {
            score += -this.getLife() * 34 / 1000;
        }
        final float size = this.getMemorySize();
        if (size >= 1024.0f) {
            score += 34;
        }
        else {
            score += (int)(size * 34.0f / 1024.0f);
        }
        return score;
    }
    
    @Override
    public final void addReference() {
        super.addReference();
        this.m_life = Texture.DEFAULT_LIFE;
    }
    
    public final short getLife() {
        return this.m_life;
    }
    
    public final void reduceLife() {
        if (this.getNumReferences() == 0 && this.m_life > -32768) {
            --this.m_life;
        }
    }
    
    @Override
    protected void onNegativeNumReferences() {
        super.onNegativeNumReferences();
    }
    
    private void initialize(final long name, final String fileName, final boolean keepData) {
        this.m_name = name;
        this.m_fileName = fileName;
        this.m_isUsed = false;
        this.m_blendMode = TextureBlendModes.Modulate;
        this.m_keepData = keepData;
        this.m_life = Texture.DEFAULT_LIFE;
    }
    
    static {
        RAW_FOUR_CC = FourCC.RAW;
        Texture.DEFAULT_LIFE = 500;
    }
    
    public enum ManagerState
    {
        NONE, 
        CREATED, 
        PUT, 
        PREPARED, 
        REMOVING, 
        DELETED;
    }
}
