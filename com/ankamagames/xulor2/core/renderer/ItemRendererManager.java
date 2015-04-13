package com.ankamagames.xulor2.core.renderer;

import com.ankamagames.xulor2.component.*;
import java.util.*;

public class ItemRendererManager
{
    private ArrayList<ItemRenderer> m_renderers;
    private ItemRenderer m_defaultRenderer;
    private static final ItemRenderer EMPTY_RENDERER;
    private ArrayList<RenderableContainer> m_renderables;
    
    public ItemRendererManager(final ArrayList<ItemRenderer> renderers) {
        super();
        this.m_defaultRenderer = null;
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.setRenderers(renderers);
    }
    
    public ItemRendererManager() {
        super();
        this.m_defaultRenderer = null;
        this.m_renderables = new ArrayList<RenderableContainer>();
    }
    
    public void addRenderable(final RenderableContainer renderable) {
        this.m_renderables.add(renderable);
    }
    
    public void removeRenderable(final RenderableContainer renderable) {
        this.m_renderables.remove(renderable);
    }
    
    public void clear() {
        this.m_renderables.clear();
    }
    
    public void invalidateRenderers() {
        for (int i = this.m_renderables.size() - 1; i >= 0; --i) {
            this.m_renderables.get(i).invalidateRenderer();
        }
    }
    
    protected void registerDefaultRenderer() {
        this.m_defaultRenderer = null;
        if (this.m_renderers == null || this.m_renderers.size() == 0) {
            return;
        }
        if (this.m_defaultRenderer == null) {
            this.m_defaultRenderer = this.m_renderers.get(0);
        }
    }
    
    public boolean assign(final RenderableContainer renderable) {
        if (this.m_defaultRenderer == null || renderable == null) {
            return false;
        }
        ItemRenderer found = null;
        for (final ItemRenderer renderer : this.m_renderers) {
            if (renderer.isRenderableCompatible(renderable)) {
                found = renderer;
                break;
            }
        }
        if (found == null) {
            if (ItemRendererManager.EMPTY_RENDERER.isRenderableCompatible(renderable)) {
                found = ItemRendererManager.EMPTY_RENDERER;
            }
            else {
                found = this.m_defaultRenderer;
            }
        }
        if (renderable.getRenderer() != found) {
            renderable.setRenderer(found);
            return true;
        }
        return false;
    }
    
    public ArrayList<ItemRenderer> getRenderers() {
        return this.m_renderers;
    }
    
    public void setRenderers(final ArrayList<ItemRenderer> renderers) {
        this.m_renderers = renderers;
        if (this.m_renderers != null) {
            for (int i = this.m_renderers.size() - 1; i >= 0; --i) {
                this.m_renderers.get(i).setManager(this);
            }
        }
        this.registerDefaultRenderer();
    }
    
    public void addRenderer(final ItemRenderer renderer) {
        if (this.m_renderers == null) {
            this.m_renderers = new ArrayList<ItemRenderer>();
        }
        this.m_renderers.add(renderer);
        renderer.setManager(this);
        this.registerDefaultRenderer();
    }
    
    static {
        EMPTY_RENDERER = new EmptyItemRenderer();
    }
}
