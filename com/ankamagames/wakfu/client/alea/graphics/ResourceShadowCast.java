package com.ankamagames.wakfu.client.alea.graphics;

import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class ResourceShadowCast extends LitSceneModifierMap implements GlobalLightListener
{
    public static final ResourceShadowCast INSTANCE;
    private final TLongObjectHashMap<Resource> m_resources;
    private final TByteObjectHashMap<float[]> m_shadows;
    private float m_contrast;
    private float m_lastContrast;
    private float m_scale;
    private boolean m_parametersChanged;
    private final TObjectByteHashMap<Resource> m_resourceSize;
    private boolean m_needUpdate;
    private boolean m_needClear;
    private final Cache m_lightCache;
    private final TLongObjectProcedure<Resource> _checkShadowSizeProcedure;
    private final TObjectByteProcedure<Resource> _updateLightMapProcedure;
    private static final float MIN_BRIGHTNESS = 0.09803922f;
    
    protected ResourceShadowCast() {
        super();
        this.m_resources = new TLongObjectHashMap<Resource>();
        this.m_shadows = new TByteObjectHashMap<float[]>();
        this.m_contrast = 0.65f;
        this.m_lastContrast = 0.0f;
        this.m_scale = 0.65f;
        this.m_parametersChanged = true;
        this.m_resourceSize = new TObjectByteHashMap<Resource>();
        this.m_needUpdate = true;
        this.m_needClear = true;
        this.m_lightCache = new Cache(512);
        this._checkShadowSizeProcedure = new TLongObjectProcedure<Resource>() {
            @Override
            public boolean execute(final long key, final Resource resource) {
                final byte lastSize = ResourceShadowCast.this.m_resourceSize.get(resource);
                final byte size = ResourceShadowCast.this.getShadowSize(resource);
                if (lastSize != size) {
                    ResourceShadowCast.this.m_needUpdate = true;
                }
                ResourceShadowCast.this.m_resourceSize.put(resource, size);
                return true;
            }
        };
        this._updateLightMapProcedure = new TObjectByteProcedure<Resource>() {
            @Override
            public boolean execute(final Resource resource, final byte shadowSize) {
                if (shadowSize > 0) {
                    final int x = resource.getWorldCellX();
                    final int y = resource.getWorldCellY();
                    final float[] mask = ResourceShadowCast.this.m_shadows.get(shadowSize);
                    final int lsize = 3;
                    ResourceShadowCast.this.m_lightCache.updateValue(mask, shadowSize + 1, 3, x - 1, y - 1);
                    resource.setCurrentShadowValue(1.0f / mask[shadowSize + 2]);
                }
                return true;
            }
        };
    }
    
    @Override
    public void shadowIntensityChanged(final float value) {
        this.setShadowContrast(value);
    }
    
    @Override
    public void nightLightIntensityChanged(final float value) {
    }
    
    public void setShadowContrast(final float contrast) {
        this.m_contrast = ((contrast < 0.0f) ? 0.0f : contrast);
        this.m_parametersChanged = !Engine.isEqualColor(this.m_lastContrast, this.m_contrast);
        if (this.m_parametersChanged) {
            this.m_lastContrast = this.m_contrast;
        }
    }
    
    public void setShadowScale(final int scale) {
        this.m_scale = scale;
        this.m_parametersChanged = true;
    }
    
    @Override
    public void precompute() {
        if (this.m_needClear) {
            this.m_resourceSize.clear();
            this.m_resourceSize.ensureCapacity(this.m_resources.size());
            this.m_needClear = false;
        }
        this.m_resources.forEachEntry(this._checkShadowSizeProcedure);
        if (!this.m_needUpdate) {
            return;
        }
        this.m_needUpdate = false;
        this.m_lightCache.reset();
        this.m_resourceSize.forEachEntry(this._updateLightMapProcedure);
        this.m_lightCache.normalize();
    }
    
    @Override
    public int getPriority() {
        return 200;
    }
    
    @Override
    public boolean useless() {
        return Engine.isEqualColor(this.m_contrast, 0.0f);
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        this.m_lightCache.applyOnColor(x, y, colors);
    }
    
    @Override
    public void update(final int deltaTime) {
        if (!this.m_parametersChanged && !this.m_shadows.isEmpty()) {
            return;
        }
        this.m_scale = 0.8f;
        for (final ResourceSizeCategory category : ResourceSizeCategory.values()) {
            final byte size = this.getShadowSize(category);
            if (size > 0) {
                final int shadowSize = size + 1;
                final int lsize = 3;
                final float[] shadowMask = new float[shadowSize * 3];
                this.m_shadows.put(size, shadowMask);
                for (int i = 0; i < shadowMask.length; ++i) {
                    shadowMask[i] = 1.0f;
                }
                final float v = 0.09803922f * this.m_contrast;
                for (int j = 0; j < shadowSize; ++j) {
                    final float value = (j == 0 || j == shadowSize - 1) ? (v * 0.5f) : v;
                    shadowMask[j + shadowSize] = 1.0f - value;
                    shadowMask[j] = (shadowMask[j + 2 * shadowSize] = 1.0f - 0.4f * value);
                }
            }
        }
        this.m_parametersChanged = false;
        this.m_needUpdate = true;
    }
    
    public final void addResource(final Resource resource) {
        final int shadowLength = resource.getCurrentSizeCategory().getShadowLength();
        if (shadowLength <= 0) {
            return;
        }
        this.m_resources.put(getKey(resource), resource);
        this.m_needClear = true;
    }
    
    public final void removeResource(final Resource resource) {
        this.m_resources.remove(getKey(resource));
        this.m_needClear = true;
    }
    
    private static long getKey(final Resource resource) {
        return MathHelper.getLongFromTwoInt(resource.getWorldCellX(), resource.getWorldCellY());
    }
    
    byte getShadowSize(final Resource resource) {
        if (!resource.isVisible()) {
            return 0;
        }
        if (resource.getEvolutionStep() == 0 || resource.getEvolutionStep() == 16) {
            return 0;
        }
        return this.getShadowSize(resource.getCurrentSizeCategory());
    }
    
    private byte getShadowSize(final ResourceSizeCategory category) {
        return (byte)(category.getShadowLength() * this.m_scale);
    }
    
    static {
        INSTANCE = new ResourceShadowCast();
    }
    
    private static class Cache implements TIntHashingStrategy
    {
        private final TIntFloatHashMap m_colors;
        
        Cache(final int capacity) {
            super();
            this.m_colors = new TIntFloatHashMap(capacity, 0.9f, this);
        }
        
        @Override
        public int computeHashCode(final int val) {
            return val;
        }
        
        public void updateValue(final float[] mask, final int width, final int height, final int destX, final int destY) {
            for (int y = 0; y < height; ++y) {
                final int srcOffset = y * width;
                for (int x = 0; x < width; ++x) {
                    this.updateValue(destX + x, destY + y, mask[x + srcOffset]);
                }
            }
        }
        
        private void updateValue(final int x, final int y, final float v) {
            final int hash = hash(x, y);
            final float value = this.m_colors.get(hash);
            if (value != 0.0f) {
                this.m_colors.put(hash, v * value);
            }
            else {
                this.m_colors.put(hash, v);
            }
        }
        
        public final void reset() {
            this.m_colors.clear();
        }
        
        public void applyOnColor(final int x, final int y, final float[] colors) {
            final float v = this.m_colors.get(hash(x, y));
            if (v != 0.0f) {
                final int n = 0;
                colors[n] *= v;
                final int n2 = 1;
                colors[n2] *= v;
                final int n3 = 2;
                colors[n3] *= v;
            }
        }
        
        public void normalize() {
            this.m_colors.forEachEntry(new TIntFloatProcedure() {
                @Override
                public boolean execute(final int key, final float value) {
                    if (value < 0.9019608f) {
                        Cache.this.m_colors.put(key, 0.9019608f);
                    }
                    return true;
                }
            });
        }
        
        static int hash(final int x, final int y) {
            return (x & 0xFFFF) | y << 16;
        }
    }
}
