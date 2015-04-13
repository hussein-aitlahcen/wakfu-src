package com.ankamagames.baseImpl.graphics.alea.display.lights;

import com.ankamagames.baseImpl.graphics.alea.display.lights.lightMap.*;
import com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class IsoLightSourceModifier extends LitSceneModifierMap
{
    private static final IsoLightSourceModifier m_instance;
    private final TIntObjectHashMap<IsoLightSource> m_lights;
    private final Cache m_lightCache;
    private final UpdatelightSourceProcedure m_updateProcedure;
    
    public static IsoLightSourceModifier getInstance() {
        return IsoLightSourceModifier.m_instance;
    }
    
    private IsoLightSourceModifier() {
        super();
        this.m_lights = new TIntObjectHashMap<IsoLightSource>();
        this.m_lightCache = new Cache(1024);
        this.m_updateProcedure = new UpdatelightSourceProcedure();
    }
    
    @Override
    public void precompute() {
        this.m_lightCache.reset();
        if (!this.m_lights.isEmpty()) {
            final float lightFactor = 1.0f;
            this.m_lights.forEachValue(new TObjectProcedure<IsoLightSource>() {
                @Override
                public boolean execute(final IsoLightSource light) {
                    if (light.isEnabled() && light.getIntensity() != 0.0f) {
                        light.apply(IsoLightSourceModifier.this.m_lightCache, 1.0f);
                    }
                    return true;
                }
            });
        }
    }
    
    @Override
    public void update(final int deltaTime) {
    }
    
    @Override
    public int getPriority() {
        return 600;
    }
    
    @Override
    public boolean useless() {
        return this.m_lights.isEmpty();
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        this.m_lightCache.applyOnColor(x, y, z, colors);
    }
    
    void updateLightSource(final float lightIntensity, final int deltaTime) {
        this.m_updateProcedure.reset(lightIntensity, deltaTime);
        if (!this.m_lights.isEmpty()) {
            this.m_lights.forEachValue(this.m_updateProcedure);
        }
        final int toRemoveCount = this.m_updateProcedure.m_lightsIdToRemove.size();
        if (toRemoveCount != 0) {
            for (int i = 0; i < toRemoveCount; ++i) {
                this.removeLight(this.m_updateProcedure.m_lightsIdToRemove.getQuick(i));
            }
        }
    }
    
    public final void add(final IsoLightSource light) {
        this.m_lights.put(light.getId(), light);
        ParticlesAndLightDebug.INSTANCE.addLightSource(light);
    }
    
    public final void removeLight(final int id) {
        final IsoLightSource light = this.m_lights.remove(id);
        ParticlesAndLightDebug.INSTANCE.removeLightSource(light);
    }
    
    public final void clear() {
        this.m_lights.clear();
    }
    
    public final IsoLightSource getLight(final int id) {
        return this.m_lights.get(id);
    }
    
    @Override
    public void setBounds(final int minX, final int minY, final int width, final int height) {
        this.m_lightCache.setBounds(minX, minY, width, height);
    }
    
    static {
        m_instance = new IsoLightSourceModifier();
    }
    
    private static class Cache implements LightMap
    {
        private final Rect m_bounds;
        private final TLongObjectHashMap<float[]> m_colors;
        
        Cache(final int capacity) {
            super();
            this.m_bounds = new Rect(0, 0, 0, 0);
            this.m_colors = new TLongObjectHashMap<float[]>(capacity, 0.9f);
        }
        
        public final void reset() {
            this.m_colors.clear();
        }
        
        public final void applyOnColor(final int x, final int y, final int z, final float[] colors) {
            final float[] v = this.m_colors.get(hash(x, y, z));
            if (v != null) {
                final int n = 0;
                colors[n] += v[0] + v[3];
                final int n2 = 1;
                colors[n2] += v[1] + v[4];
                final int n3 = 2;
                colors[n3] += v[2] + v[5];
            }
        }
        
        static long hash(final int x, final int y, final int z) {
            return MathHelper.getIntFromTwoInt(x, y);
        }
        
        @Override
        public Rect getBounds() {
            return this.m_bounds;
        }
        
        @Override
        public void setBounds(final int minX, final int minY, final int width, final int height) {
            this.m_bounds.set(minX, minX + width, minY, minY + height);
        }
        
        @Override
        public void addColors(final int x, final int y, final int z, final float br, final float bg, final float bb, final float sr, final float sg, final float sb) {
            final long hash = hash(x, y, z);
            final float[] value = this.m_colors.get(hash);
            if (value != null) {
                final float[] array = value;
                final int n = 0;
                array[n] += br;
                final float[] array2 = value;
                final int n2 = 1;
                array2[n2] += bg;
                final float[] array3 = value;
                final int n3 = 2;
                array3[n3] += bb;
                final float[] array4 = value;
                final int n4 = 3;
                array4[n4] += sr;
                final float[] array5 = value;
                final int n5 = 4;
                array5[n5] += sg;
                final float[] array6 = value;
                final int n6 = 5;
                array6[n6] += sb;
            }
            else {
                this.m_colors.put(hash, new float[] { br, bg, bb, sr, sg, sb });
            }
        }
    }
    
    private static class UpdatelightSourceProcedure implements TObjectProcedure<IsoLightSource>
    {
        private float m_lightIntensity;
        private int m_deltaTime;
        final TIntArrayList m_lightsIdToRemove;
        
        private UpdatelightSourceProcedure() {
            super();
            this.m_lightsIdToRemove = new TIntArrayList();
        }
        
        @Override
        public boolean execute(final IsoLightSource light) {
            if (light.isNightOnly()) {
                light.setIntensity(this.m_lightIntensity);
            }
            light.update(this.m_deltaTime);
            if (light.isShutdown()) {
                this.m_lightsIdToRemove.add(light.getId());
            }
            return true;
        }
        
        void reset(final float lightIntensity, final int deltaTime) {
            this.m_lightIntensity = lightIntensity;
            this.m_deltaTime = deltaTime;
            this.m_lightsIdToRemove.resetQuick();
        }
    }
}
