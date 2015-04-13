package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public abstract class AleaWorldSceneWithParallax extends AleaWorldScene
{
    private final ArrayList<ParallaxWorldScene> m_backgrounds;
    private final ArrayList<ParallaxWorldScene> m_foregrounds;
    private final RenderTreeInterface m_globalRenderTree;
    private boolean m_displayParallax;
    
    protected AleaWorldSceneWithParallax(final float minZoom, final float maxZoom) {
        super(minZoom, maxZoom);
        this.m_backgrounds = new ArrayList<ParallaxWorldScene>(3);
        this.m_foregrounds = new ArrayList<ParallaxWorldScene>(3);
        this.m_globalRenderTree = new RenderTreeInterface() {
            @Override
            public void render(final Renderer renderer) {
                final GL gl = renderer.getDevice();
                this.renderParallax(renderer, gl, AleaWorldSceneWithParallax.this.m_backgrounds);
                AleaWorldSceneWithParallax.this.setScene(gl);
                AleaWorldSceneWithParallax.this.m_renderTree.render(renderer);
                this.renderParallax(renderer, gl, AleaWorldSceneWithParallax.this.m_foregrounds);
            }
            
            private void renderParallax(final Renderer renderer, final GL gl, final ArrayList<ParallaxWorldScene> parallax) {
                if (!AleaWorldSceneWithParallax.this.m_displayParallax) {
                    return;
                }
                for (int i = 0; i < parallax.size(); ++i) {
                    final ParallaxWorldScene bg = parallax.get(i);
                    bg.setScene(gl);
                    bg.m_renderTree.render(renderer);
                }
            }
            
            @Override
            public void pushAtEnd(final Entity entity) {
            }
            
            @Override
            public void push(final Entity entity, final int level) {
            }
            
            @Override
            public void clear() {
            }
            
            @Override
            public void getAddEntities(final HashSet<Entity> entities) {
            }
        };
        this.m_displayParallax = true;
    }
    
    public void displayParallax(final boolean displayParallax) {
        this.m_displayParallax = displayParallax;
    }
    
    public void addParallax(final ParallaxWorldScene parallax) {
        if (!parallax.isValidWorld()) {
            return;
        }
        parallax.setFrustumSize((int)this.m_frustumWidth, (int)this.m_frustumHeight);
        if (parallax.isBackground()) {
            if (!this.m_backgrounds.contains(parallax)) {
                this.m_backgrounds.add(parallax);
            }
        }
        else if (!this.m_foregrounds.contains(parallax)) {
            this.m_foregrounds.add(parallax);
        }
    }
    
    public void removeParallax(final ParallaxWorldScene parallax) {
        if (parallax.isBackground()) {
            this.m_backgrounds.remove(parallax);
        }
        else {
            this.m_foregrounds.remove(parallax);
        }
    }
    
    @Override
    public void cleanAfterFade() {
        super.cleanAfterFade();
        for (int i = this.m_backgrounds.size() - 1; i >= 0; --i) {
            this.m_backgrounds.get(i).cleanAfterFade();
        }
        for (int i = this.m_foregrounds.size() - 1; i >= 0; --i) {
            this.m_foregrounds.get(i).cleanAfterFade();
        }
    }
    
    @Override
    public void setFrustumSize(final int frustumWidth, final int frustumHeight) {
        super.setFrustumSize(frustumWidth, frustumHeight);
        for (int i = this.m_backgrounds.size() - 1; i >= 0; --i) {
            this.m_backgrounds.get(i).setFrustumSize(frustumWidth, frustumHeight);
        }
        for (int i = this.m_foregrounds.size() - 1; i >= 0; --i) {
            this.m_foregrounds.get(i).setFrustumSize(frustumWidth, frustumHeight);
        }
    }
    
    @Override
    protected void drawAllWithoutEffect(final GLRenderer renderer) {
    }
    
    @Override
    protected void renderEffect(final GLRenderer renderer) {
        EffectManager.getInstance().render(this.m_globalRenderTree, renderer);
    }
    
    @Override
    public void process(final int deltaTime) {
        super.process(deltaTime);
        if (!this.m_displayParallax) {
            return;
        }
        for (int i = this.m_backgrounds.size() - 1; i >= 0; --i) {
            this.m_backgrounds.get(i).process(deltaTime);
        }
        for (int i = this.m_foregrounds.size() - 1; i >= 0; --i) {
            this.m_foregrounds.get(i).process(deltaTime);
        }
    }
    
    @Override
    public void clean(final boolean forceUpdate) {
        super.clean(forceUpdate);
        this.cleanParallaxes();
    }
    
    public final void cleanParallaxes() {
        for (int i = this.m_backgrounds.size() - 1; i >= 0; --i) {
            this.m_backgrounds.get(i).reset();
        }
        for (int i = this.m_foregrounds.size() - 1; i >= 0; --i) {
            this.m_foregrounds.get(i).reset();
        }
        this.m_backgrounds.clear();
        this.m_foregrounds.clear();
    }
    
    @Override
    protected void copyFrom(final AleaWorldScene scene) {
        super.copyFrom(scene);
        final AleaWorldSceneWithParallax sp = (AleaWorldSceneWithParallax)scene;
        for (int i = 0; i < sp.m_backgrounds.size(); ++i) {
            final ParallaxWorldScene parallaxWorldScene = sp.m_backgrounds.get(i);
            this.m_backgrounds.add((ParallaxWorldScene)parallaxWorldScene.duplicate());
            parallaxWorldScene.resetRenderTree();
        }
        for (int i = 0; i < sp.m_foregrounds.size(); ++i) {
            final ParallaxWorldScene parallaxWorldScene = sp.m_foregrounds.get(i);
            this.m_foregrounds.add((ParallaxWorldScene)parallaxWorldScene.duplicate());
            parallaxWorldScene.resetRenderTree();
        }
    }
}
