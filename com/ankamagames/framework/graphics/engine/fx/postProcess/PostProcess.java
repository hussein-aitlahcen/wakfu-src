package com.ankamagames.framework.graphics.engine.fx.postProcess;

import com.ankamagames.framework.graphics.engine.opengl.Cg.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class PostProcess
{
    private final GLEffect m_effect;
    public final String m_effectFileName;
    public final String m_technique;
    private final int m_techniqueCRC;
    private boolean m_enabled;
    private float m_strength;
    protected long m_elapsedTime;
    protected final EffectParams m_params;
    private boolean m_forced;
    
    public PostProcess(final String effectFileName, final String technique, final EffectParams params) {
        super();
        this.m_strength = 1.0f;
        this.m_elapsedTime = 0L;
        this.m_enabled = true;
        this.m_effectFileName = effectFileName;
        this.m_technique = technique;
        this.m_techniqueCRC = Engine.getTechnic(technique);
        (this.m_effect = new GLEffect()).load(FileHelper.getNameWithoutExt(effectFileName), effectFileName);
        EffectManager.getInstance().addEffect(this.m_effect);
        this.m_params = params;
    }
    
    public void update(final int timeIncrement) {
        this.m_elapsedTime += timeIncrement;
    }
    
    public final boolean isEnabled() {
        return this.m_enabled;
    }
    
    public final void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public final boolean isActivated() {
        return this.m_strength > 0.0f && this.m_enabled;
    }
    
    public final GLEffect getEffect() {
        return this.m_effect;
    }
    
    public final int getTechniqueCRC() {
        return this.m_techniqueCRC;
    }
    
    public void prepare(final EntitySprite entity) {
        entity.setEffect(this.getEffect(), this.getTechniqueCRC(), this.m_params);
        entity.setTexture2(Engine.getInstance().m_perturbMap);
    }
    
    public void setForced(final boolean forced) {
        this.m_forced = forced;
    }
    
    public boolean isForced() {
        return this.m_forced;
    }
}
