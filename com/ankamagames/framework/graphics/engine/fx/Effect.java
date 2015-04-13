package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import java.io.*;

public abstract class Effect
{
    protected final IntObjectLightWeightMap<Technique> m_techniques;
    protected Technique m_selectedTechnique;
    private int m_selectTechniqueCrc;
    private boolean m_isLoaded;
    private String m_name;
    private String m_fileName;
    private long m_loadTime;
    
    public Effect() {
        super();
        this.m_techniques = new IntObjectLightWeightMap<Technique>();
        this.m_isLoaded = false;
        this.m_selectedTechnique = null;
        this.m_selectTechniqueCrc = 0;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void load(final String name, final String fileName) {
        this.m_isLoaded = true;
        this.m_name = name;
        this.m_fileName = fileName;
        this.m_loadTime = this.getLastModifiedTime();
        this.m_selectedTechnique = null;
        this.m_selectTechniqueCrc = 0;
    }
    
    public void reload() {
        this.load(this.m_name, this.m_fileName);
    }
    
    public void create(final String name, final String effect) {
        this.m_isLoaded = true;
        this.m_name = name;
        this.m_selectedTechnique = null;
        this.m_selectTechniqueCrc = 0;
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public void render(final Renderer renderer, final Entity entity, final EffectParams params) {
        if (!this.m_isLoaded) {
            EffectManager.getInstance().createEffects();
        }
    }
    
    public final void selectTechnique(final int techniqueCRC) {
        if (this.m_selectTechniqueCrc == techniqueCRC) {
            return;
        }
        this.m_selectedTechnique = this.m_techniques.get(techniqueCRC);
        this.m_selectTechniqueCrc = techniqueCRC;
        this.reset();
    }
    
    public final boolean isTechniqueValide(final int techniqueCRC) {
        return this.m_techniques.contains(techniqueCRC);
    }
    
    public void reset() {
        if (this.m_selectedTechnique == null) {
            return;
        }
        this.m_selectedTechnique.reset();
    }
    
    public final boolean useFixedPipeline() {
        return this.m_selectedTechnique == null;
    }
    
    public void parse() {
    }
    
    private long getLastModifiedTime() {
        if (this.m_fileName == null) {
            return 0L;
        }
        final int index = this.m_fileName.indexOf("file:");
        if (index == -1) {
            return 0L;
        }
        final String filename = this.m_fileName.substring(index + 5);
        return new File(filename).lastModified();
    }
    
    public final void reloadIfNeeded() {
        if (this.getLastModifiedTime() > this.m_loadTime) {
            this.reload();
        }
    }
    
    protected abstract void applyParams(final EffectParams p0);
}
