package com.ankamagames.baseImpl.graphics.alea.display.lights;

import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import com.ankamagames.framework.graphics.engine.light.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.*;

public final class IsoSceneLightManager implements LightSourceManager
{
    public static final IsoSceneLightManager INSTANCE;
    private final ArrayList<LitScene> m_scenes;
    private final ArrayList<LitSceneObject> m_sceneObjects;
    private static final Comparator<LitSceneModifier> LIGHTING_MODIFIER_COMPARATOR;
    private final LightningMapManager m_lightMapStatic;
    private final ArrayList<LitSceneModifier> m_lightingModifiers;
    private final ArrayList<LitSceneModifierMap> m_needPrecompute;
    private final ArrayList<LitSceneModifier> m_lighting;
    private final ArrayList<GlobalLightListener> m_globalLightListeners;
    private float m_shadowIntensityFactor;
    private float m_nightLightIntensityFactor;
    private boolean m_shadowIntensityChanged;
    private boolean m_nightIntensityChanged;
    private boolean m_lightningEnabled;
    private final float[] _colors;
    
    private IsoSceneLightManager() {
        super();
        this.m_scenes = new ArrayList<LitScene>();
        this.m_sceneObjects = new ArrayList<LitSceneObject>(3000);
        this.m_lightMapStatic = LightningMapManager.getInstance();
        this.m_lightingModifiers = new ArrayList<LitSceneModifier>(8);
        this.m_needPrecompute = new ArrayList<LitSceneModifierMap>(3);
        this.m_lighting = new ArrayList<LitSceneModifier>(8);
        this.m_globalLightListeners = new ArrayList<GlobalLightListener>();
        this.m_shadowIntensityChanged = true;
        this.m_nightIntensityChanged = true;
        this.m_lightningEnabled = true;
        this._colors = new float[6];
        this.m_lightingModifiers.add(this.m_lightMapStatic);
    }
    
    public void initialize() {
        LightSourceManagerDelegate.INSTANCE.setUp(new IsoLightSourceFactory(), this);
        this.reset();
    }
    
    public boolean isLightningEnabled() {
        return this.m_lightningEnabled;
    }
    
    public void enableLightning(final boolean lightningEnabled) {
        this.m_lightningEnabled = lightningEnabled;
    }
    
    public IsoLightSource getLight(final int id) {
        return IsoLightSourceModifier.getInstance().getLight(id);
    }
    
    public void enableLight(final int id) {
        final IsoLightSource light = this.getLight(id);
        if (light != null) {
            light.setEnabled(true);
        }
    }
    
    public void disableLight(final int id) {
        final IsoLightSource light = this.getLight(id);
        if (light != null) {
            light.setEnabled(false);
        }
    }
    
    public void removeLight(final int id) {
        IsoLightSourceModifier.getInstance().removeLight(id);
    }
    
    public void shutdownLight(final int id, final int duration) {
        if (duration == 0) {
            this.removeLight(id);
            return;
        }
        final IsoLightSource light = this.getLight(id);
        if (light != null) {
            light.shutdown(duration);
        }
    }
    
    @Override
    public void addLight(final LightSource light) {
        IsoLightSourceModifier.getInstance().add((IsoLightSource)light);
    }
    
    @Override
    public void removeLight(final LightSource light) {
        IsoLightSourceModifier.getInstance().removeLight(light.getId());
    }
    
    public void addScene(final LitScene scene) {
        if (!this.m_scenes.contains(scene)) {
            this.m_scenes.add(scene);
        }
    }
    
    public void removeScene(final LitScene scene) {
        this.m_scenes.remove(scene);
    }
    
    public void addLightingModifier(final LitSceneModifier modifier) {
        if (!this.m_lightingModifiers.contains(modifier)) {
            this.m_lightingModifiers.add(modifier);
            Collections.sort(this.m_lightingModifiers, IsoSceneLightManager.LIGHTING_MODIFIER_COMPARATOR);
            if (modifier instanceof LitSceneModifierMap) {
                this.m_needPrecompute.add((LitSceneModifierMap)modifier);
            }
        }
    }
    
    public void removeLightingModifier(final LitSceneModifier modifier) {
        this.m_lightingModifiers.remove(modifier);
        this.m_needPrecompute.remove(modifier);
    }
    
    public void addGlobalLightingListener(final GlobalLightListener listener) {
        if (!this.m_globalLightListeners.contains(listener)) {
            this.m_globalLightListeners.add(listener);
        }
    }
    
    public void removeGlobalLightingListener(final GlobalLightListener listener) {
        this.m_globalLightListeners.remove(listener);
    }
    
    public void update(final IsoWorldScene scene, final int deltaTime) {
        this.updateLights(deltaTime);
        if (!this.m_lightningEnabled) {
            return;
        }
        this.getVisibleObjects(scene);
        if (this.m_lightMapStatic.isNewLightMapLoaded()) {
            this.updateShadow();
            this.updateNightLight();
            this.m_lightMapStatic.setNewLightMapLoaded(false);
        }
        else {
            if (this.m_shadowIntensityChanged) {
                this.updateShadow();
            }
            if (this.m_nightIntensityChanged) {
                this.updateNightLight();
            }
        }
        this.preComputeLighting();
        this.selectUsefulLightModifiers();
        this.applyLighting();
    }
    
    private void updateLights(final int deltaTime) {
        this.updateModifier(deltaTime);
        IsoLightSourceModifier.getInstance().updateLightSource(this.m_nightLightIntensityFactor, deltaTime);
    }
    
    private void selectUsefulLightModifiers() {
        this.m_lighting.clear();
        for (int i = 0, count = this.m_lightingModifiers.size(); i < count; ++i) {
            final LitSceneModifier modifier = this.m_lightingModifiers.get(i);
            if (!modifier.useless()) {
                this.m_lighting.add(modifier);
            }
        }
    }
    
    private void updateNightLight() {
        this.m_lightMapStatic.updateNightLight(this.m_nightLightIntensityFactor);
        this.m_nightIntensityChanged = false;
    }
    
    private void updateShadow() {
        this.m_lightMapStatic.updateShadow(this.m_shadowIntensityFactor);
        this.m_shadowIntensityChanged = false;
    }
    
    private void updateModifier(final int deltaTime) {
        for (int i = 0, count = this.m_lightingModifiers.size(); i < count; ++i) {
            this.m_lightingModifiers.get(i).update(deltaTime);
        }
    }
    
    private void getVisibleObjects(final IsoWorldScene scene) {
        final AbstractCamera camera = scene.getIsoCamera();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        this.m_sceneObjects.clear();
        for (int numScenes = this.m_scenes.size(), sceneIndex = 0; sceneIndex < numScenes; ++sceneIndex) {
            this.m_scenes.get(sceneIndex).queryObjects(camera, this.m_sceneObjects);
        }
        for (int objectCount = this.m_sceneObjects.size(), i = 0; i < objectCount; ++i) {
            final LitSceneObject o = this.m_sceneObjects.get(i);
            final int x = o.getWorldCellX();
            final int y = o.getWorldCellY();
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        for (int i = 0, count = this.m_needPrecompute.size(); i < count; ++i) {
            this.m_needPrecompute.get(i).setBounds(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
    }
    
    private void preComputeLighting() {
        for (int i = 0, count = this.m_needPrecompute.size(); i < count; ++i) {
            this.m_needPrecompute.get(i).precompute();
        }
    }
    
    private void applyLighting() {
        for (int i = 0, objCount = this.m_sceneObjects.size(); i < objCount; ++i) {
            final LitSceneObject object = this.m_sceneObjects.get(i);
            final int x = object.getWorldCellX();
            final int y = object.getWorldCellY();
            final int z = object.getWorldCellAltitude();
            final int layerId = object.getLayerId();
            this.computeLighting(x, y, z, layerId, this._colors);
            object.applyLighting(this._colors);
        }
    }
    
    private void computeLighting(final int x, final int y, final int z, final int layerId, final float[] colors) {
        colors[0] = 1.0f;
        colors[2] = (colors[1] = 1.0f);
        for (int i = 0, count = this.m_lighting.size(); i < count; ++i) {
            this.m_lighting.get(i).apply(x, y, z, layerId, colors);
        }
    }
    
    public void reset() {
        this.m_lightingModifiers.clear();
        this.m_needPrecompute.clear();
        this.setLightIntensityFactor(1.0f);
        this.m_shadowIntensityChanged = true;
        this.m_nightIntensityChanged = true;
        IsoLightSourceModifier.getInstance().clear();
        this.addLightingModifier(this.m_lightMapStatic);
        this.addLightingModifier(ClampShadowLitModifier.getInstance());
        this.addLightingModifier(ClampLightLitModifier.getInstance());
        this.addLightingModifier(IsoLightSourceModifier.getInstance());
    }
    
    public void forceUpdate() {
        this.m_lightMapStatic.setNewLightMapLoaded(true);
    }
    
    public void setLightIntensityFactor(final float shadowIntensity, final float nightLightIntensity) {
        final int count = this.m_globalLightListeners.size();
        if (!Engine.isEqualColor(this.m_shadowIntensityFactor, shadowIntensity)) {
            this.m_shadowIntensityFactor = shadowIntensity;
            this.m_shadowIntensityChanged = true;
            for (int i = 0; i < count; ++i) {
                this.m_globalLightListeners.get(i).shadowIntensityChanged(this.m_shadowIntensityFactor);
            }
        }
        if (!Engine.isEqualColor(this.m_nightLightIntensityFactor, nightLightIntensity)) {
            this.m_nightLightIntensityFactor = nightLightIntensity;
            this.m_nightIntensityChanged = true;
            for (int i = 0; i < count; ++i) {
                this.m_globalLightListeners.get(i).nightLightIntensityChanged(this.m_nightLightIntensityFactor);
            }
        }
    }
    
    public void setLightIntensityFactor(final float lightIntensityFactor) {
        final float shadowIntensity = (lightIntensityFactor > 0.001f) ? lightIntensityFactor : 0.0f;
        final float nightLightIntensity = (lightIntensityFactor < -0.001f) ? (-lightIntensityFactor) : 0.0f;
        this.setLightIntensityFactor(shadowIntensity, nightLightIntensity);
    }
    
    public float getNightLightIntensityFactor() {
        return this.m_nightLightIntensityFactor;
    }
    
    public float getShadowIntensityFactor() {
        return this.m_shadowIntensityFactor;
    }
    
    static {
        INSTANCE = new IsoSceneLightManager();
        LIGHTING_MODIFIER_COMPARATOR = new Comparator<LitSceneModifier>() {
            @Override
            public int compare(final LitSceneModifier o1, final LitSceneModifier o2) {
                return o1.getPriority() - o2.getPriority();
            }
        };
    }
}
