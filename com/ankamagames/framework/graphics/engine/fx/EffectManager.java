package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.fx.postProcess.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.opengl.Cg.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.util.*;
import com.sun.opengl.cg.*;
import com.ankamagames.framework.graphics.image.*;
import gnu.trove.*;
import javax.media.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class EffectManager
{
    public static final boolean RELOAD_FILE_IF_MODIFIED = false;
    private Factory m_factory;
    private final AddWorldEffect m_addWorldEffectProcedure;
    private final UpdateEffect m_updateEffectProcedure;
    public int m_timeIncrement;
    public int m_elapsedTime;
    public EntitySprite m_renderTargetEntity;
    public EntitySprite m_renderTargetEntity2;
    private static final EffectManager m_instance;
    private final HashMap<String, Effect> m_effects;
    private THashMap<String, String> m_effectsToLoad;
    private final ArrayList<PostProcess> m_postProcess;
    private final ArrayList<PostProcess> m_currentPostProcess;
    private final TIntObjectHashMap<EffectBase> m_worldEffects;
    private final TIntObjectHashMap<EffectBase> m_worldEffectsToAdd;
    private final ArrayList<Texture> m_renderTargets;
    private Texture m_fullScreenRenderTarget0;
    private Texture m_fullScreenRenderTarget1;
    private boolean m_createRenderTargetFailed;
    public GLGeometrySprite m_renderTargetGeometry;
    private boolean m_enabledPostProcess;
    private String m_baseEffect;
    private String m_uiEffect;
    private int _reloadCounter;
    
    private EffectManager() {
        super();
        this.m_factory = new Factory() {
            @Override
            public Effect createEffect(final String className) {
                return new GLEffect();
            }
        };
        this.m_addWorldEffectProcedure = new AddWorldEffect();
        this.m_updateEffectProcedure = new UpdateEffect();
        this.m_postProcess = new ArrayList<PostProcess>();
        this.m_currentPostProcess = new ArrayList<PostProcess>();
        this.m_baseEffect = "transform";
        this.m_uiEffect = "gui";
        this.m_effects = new HashMap<String, Effect>(16);
        this.m_effectsToLoad = new THashMap<String, String>(16);
        this.m_worldEffects = new TIntObjectHashMap<EffectBase>();
        this.m_worldEffectsToAdd = new TIntObjectHashMap<EffectBase>();
        this.m_renderTargets = new ArrayList<Texture>();
        this.m_renderTargetEntity = EntitySprite.Factory.newInstance();
        this.m_renderTargetEntity.m_owner = this;
        this.m_renderTargetGeometry = GLGeometrySprite.Factory.newInstance();
        this.m_renderTargetEntity.setGeometry(this.m_renderTargetGeometry);
        this.m_renderTargetEntity.setMaterial(Material.WHITE);
        this.m_renderTargetEntity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.m_renderTargetEntity.setTextureCoordinates(1.0f, 0.0f, 0.0f, 1.0f);
        this.m_renderTargetEntity2 = EntitySprite.Factory.newInstance();
        this.m_renderTargetEntity2.m_owner = this;
        this.m_renderTargetEntity2.setGeometry(((MemoryObject.ObjectFactory<GeometrySprite>)GLGeometrySprite.Factory).newInstance());
        this.m_renderTargetEntity2.setMaterial(Material.WHITE);
        this.m_renderTargetEntity2.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.m_renderTargetEntity2.setTextureCoordinates(1.0f, 0.0f, 0.0f, 1.0f);
    }
    
    public static EffectManager getInstance() {
        return EffectManager.m_instance;
    }
    
    public void setFactory(final Factory factory) {
        this.m_factory = factory;
    }
    
    public void addEffect(final String name, final String fileName, final String className) {
        this.m_effectsToLoad.put(name, fileName);
        final Effect effect = this.m_factory.createEffect(className);
        effect.setName(name);
        this.m_effects.put(effect.getName(), effect);
    }
    
    public void addEffect(final Effect effect) {
        this.m_effects.put(effect.getName(), effect);
    }
    
    public void removeEffect(final String name) {
        this.m_effects.remove(name);
    }
    
    public Effect getEffect(final String name) {
        return this.m_effects.get(name);
    }
    
    public void setBaseEffect(final String name) {
        this.m_baseEffect = name;
    }
    
    public Effect getBaseEffect() {
        return this.m_effects.get(this.m_baseEffect);
    }
    
    public void setUiEffect(final String name) {
        this.m_uiEffect = name;
    }
    
    public Effect getUiEffect() {
        return this.m_effects.get(this.m_uiEffect);
    }
    
    public void resetAllEffects() {
        for (final Effect effect : this.m_effects.values()) {
            effect.reset();
        }
    }
    
    public void createEffects() {
        this.m_effectsToLoad.forEachEntry(new TObjectObjectProcedure<String, String>() {
            @Override
            public boolean execute(final String effectName, final String file) {
                final Effect effect = EffectManager.this.getEffect(effectName);
                effect.load(effectName, file);
                return true;
            }
        });
        this.m_effectsToLoad = null;
        GLEffect.setCGContext(null);
    }
    
    public void update(final int timeIncrement) {
        this.m_timeIncrement = timeIncrement;
        this.m_elapsedTime += timeIncrement;
        for (int i = 0; i < this.m_postProcess.size(); ++i) {
            this.m_postProcess.get(i).update(timeIncrement);
        }
        this.m_worldEffectsToAdd.forEachEntry(this.m_addWorldEffectProcedure);
        this.m_worldEffectsToAdd.clear();
        this.m_updateEffectProcedure.setTimeIncrement(timeIncrement);
        this.m_worldEffects.forEachEntry(this.m_updateEffectProcedure);
    }
    
    public void enablePostProcess(final boolean enable) {
        this.m_enabledPostProcess = enable;
        this.refreshCurrentPostProcess();
    }
    
    public void addPostProcess(final PostProcess postProcess) {
        this.m_postProcess.add(postProcess);
        this.refreshCurrentPostProcess();
    }
    
    public void removePostProcess(final PostProcess postProcess) {
        this.m_postProcess.remove(postProcess);
        this.refreshCurrentPostProcess();
    }
    
    public void addWorldEffect(final EffectBase worldEffect) {
        assert worldEffect != null;
        this.m_worldEffectsToAdd.put(worldEffect.getId(), worldEffect);
    }
    
    public void removeWorldEffect(final EffectBase worldEffect) {
        this.m_worldEffects.remove(worldEffect.getId());
        this.m_worldEffectsToAdd.remove(worldEffect.getId());
    }
    
    public EffectBase getWorldEffect(final int id) {
        final EffectBase worldEffect = this.m_worldEffects.get(id);
        if (worldEffect != null) {
            return worldEffect;
        }
        return this.m_worldEffectsToAdd.get(id);
    }
    
    public boolean canDoPostProcess(final Renderer renderer) {
        return renderer.supportRenderTarget() && !this.m_createRenderTargetFailed && this.hasPostProcess();
    }
    
    private void refreshCurrentPostProcess() {
        this.m_currentPostProcess.clear();
        for (final PostProcess p : this.m_postProcess) {
            if (this.m_enabledPostProcess || p.isForced()) {
                this.m_currentPostProcess.add(p);
            }
        }
    }
    
    private boolean hasPostProcess() {
        for (final PostProcess p : this.m_currentPostProcess) {
            if (p.isActivated() && p.getEffect().isTechniqueValide(p.getTechniqueCRC())) {
                return true;
            }
        }
        return false;
    }
    
    public void createRenderTargets(final int width, final int height) {
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        if (!renderer.supportRenderTarget()) {
            return;
        }
        this.clearRenderTargets();
        this.m_fullScreenRenderTarget0 = this.getRenderTarget(width, height);
        this.m_fullScreenRenderTarget1 = this.getRenderTarget(width, height);
        if (this.m_fullScreenRenderTarget0 == null || this.m_fullScreenRenderTarget1 == null) {
            this.clearRenderTargets();
            this.m_createRenderTargetFailed = true;
        }
        this.m_renderTargetEntity.setBounds(height / 2.0f, -width / 2.0f, width, height);
        this.m_renderTargetEntity2.setBounds(height / 2.0f, -width / 2.0f, width, height);
    }
    
    public void render(final RenderTreeInterface renderTree, final Renderer renderer) {
        if (!this.canDoPostProcess(renderer)) {
            renderTree.render(renderer);
            this.renderWorldEffects(renderer);
        }
        else {
            this.applyPostProcess(renderTree);
        }
    }
    
    private Texture getRenderTarget(final int width, final int height) {
        for (int numRenderTargets = this.m_renderTargets.size(), i = 0; i < numRenderTargets; ++i) {
            final Texture texture = this.m_renderTargets.get(i);
            final Layer layer = texture.getLayer(0);
            if (layer.getWidth() == width && layer.getHeight() == height && !texture.isUsed() && texture != this.m_fullScreenRenderTarget0 && texture != this.m_fullScreenRenderTarget1) {
                return texture;
            }
        }
        return this.createNewRenderTarget(width, height);
    }
    
    public void clearWorldEffects() {
        this.m_worldEffects.forEachValue(new TObjectProcedure<EffectBase>() {
            @Override
            public boolean execute(final EffectBase object) {
                object.activate(false);
                object.clear();
                return true;
            }
        });
        this.m_worldEffects.clear();
        this.m_postProcess.clear();
        this.refreshCurrentPostProcess();
    }
    
    private void renderWorldEffects(final Renderer renderer) {
        this.m_worldEffects.forEachValue(new TObjectProcedure<EffectBase>() {
            @Override
            public boolean execute(final EffectBase effectBase) {
                if (effectBase.isActivated()) {
                    effectBase.render(renderer);
                }
                return true;
            }
        });
    }
    
    private void applyPostProcess(final RenderTreeInterface renderTree) {
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        final GL gl = renderer.getDevice();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        renderer.setRenderTarget(this.m_fullScreenRenderTarget0);
        StencilStateManager.getInstance().clear(gl);
        renderTree.render(renderer);
        this.renderWorldEffects(renderer);
        final Matrix44 oldCameraMatrix = renderer.getCameraMatrix();
        renderer.setCameraMatrix(Matrix44.IDENTITY);
        assert this.hasPostProcess();
        final Layer layer = this.m_fullScreenRenderTarget0.getLayer(0);
        stateManager.applyViewport(gl, 0, 0, layer.getWidth(), layer.getHeight());
        this.m_renderTargetEntity.setMaterial(Material.WHITE_NO_SPECULAR);
        this.m_renderTargetGeometry.setBlendFunc(BlendModes.One, BlendModes.Zero);
        Texture renderOnTexture = this.m_fullScreenRenderTarget0;
        for (int i = 0; i < this.m_currentPostProcess.size(); ++i) {
            Texture texture;
            if (i != this.m_currentPostProcess.size() - 1) {
                texture = ((renderOnTexture == this.m_fullScreenRenderTarget1) ? this.m_fullScreenRenderTarget0 : this.m_fullScreenRenderTarget1);
            }
            else {
                texture = null;
            }
            renderer.setRenderTarget(texture);
            StencilStateManager.getInstance().clear(gl);
            this.m_renderTargetEntity.setTexture(renderOnTexture);
            this.m_currentPostProcess.get(i).prepare(this.m_renderTargetEntity);
            renderOnTexture = texture;
            StencilStateManager.getInstance().clear(gl);
            this.m_renderTargetEntity.render(renderer);
        }
        renderer.setCameraMatrix(oldCameraMatrix);
    }
    
    private Texture createNewRenderTarget(final int width, final int height) {
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        final Texture renderTarget = renderer.createRenderTarget(TextureConstants.getNextRenderTargetId(), width, height, false);
        renderTarget.prepare(renderer);
        if (!renderTarget.isReady()) {
            renderTarget.removeReference();
            renderTarget.removeReference();
            return null;
        }
        renderTarget.addReference();
        this.m_renderTargets.add(renderTarget);
        return renderTarget;
    }
    
    private void clearRenderTargets() {
        for (int numRenderTargets = this.m_renderTargets.size(), i = 0; i < numRenderTargets; ++i) {
            final Texture renderTarget = this.m_renderTargets.get(i);
            renderTarget.removeReference();
            renderTarget.removeReference();
        }
        this.m_renderTargets.clear();
    }
    
    void registerWorldEffect(final int key, final EffectBase effect) {
        this.m_worldEffects.put(key, effect);
    }
    
    void removeWorldEffect(final int key) {
        this.m_worldEffects.remove(key);
    }
    
    static {
        m_instance = new EffectManager();
    }
    
    private static class AddWorldEffect implements TIntObjectProcedure<EffectBase>
    {
        @Override
        public boolean execute(final int key, final EffectBase effect) {
            EffectManager.getInstance().registerWorldEffect(key, effect);
            return true;
        }
    }
    
    private static class UpdateEffect implements TIntObjectProcedure<EffectBase>
    {
        private int m_timeIncrement;
        
        @Override
        public boolean execute(final int key, final EffectBase effect) {
            if (effect.isActivated()) {
                effect.update(this.m_timeIncrement);
            }
            else {
                effect.clear();
                EffectManager.getInstance().removeWorldEffect(key);
            }
            return true;
        }
        
        public void setTimeIncrement(final int timeIncrement) {
            this.m_timeIncrement = timeIncrement;
        }
        
        @Override
        public String toString() {
            return "UpdateEffect{m_timeIncrement=" + this.m_timeIncrement + '}';
        }
    }
    
    public interface Factory
    {
        Effect createEffect(String p0);
    }
}
