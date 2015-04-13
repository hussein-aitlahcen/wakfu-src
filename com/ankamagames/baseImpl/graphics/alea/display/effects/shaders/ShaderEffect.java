package com.ankamagames.baseImpl.graphics.alea.display.effects.shaders;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.graphics.engine.fx.postProcess.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.*;

public class ShaderEffect extends Effect
{
    private PostProcess m_postProcess;
    protected final EffectParams m_params;
    private final String m_shaderFilename;
    private final String m_techniqueName;
    private boolean m_forced;
    
    public ShaderEffect(final String shaderFilename, final String techniqueName) {
        this(shaderFilename, techniqueName, new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float) }));
    }
    
    public ShaderEffect(final String shaderFilename, final String techniqueName, final EffectParams params) {
        super();
        this.m_params = params;
        this.m_shaderFilename = shaderFilename;
        this.m_techniqueName = techniqueName;
    }
    
    public void setForced(final boolean forced) {
        this.m_forced = forced;
    }
    
    @Override
    public void activate(final boolean activate) {
        super.activate(activate);
        if (activate) {
            if (this.m_postProcess == null) {
                (this.m_postProcess = new PostProcess(this.m_shaderFilename, this.m_techniqueName, this.m_params)).setForced(this.m_forced);
                EffectManager.getInstance().addPostProcess(this.m_postProcess);
            }
        }
        else if (this.m_postProcess != null) {
            EffectManager.getInstance().removePostProcess(this.m_postProcess);
            this.m_postProcess = null;
        }
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        this.m_params.setFloat("gStrength", this.getStrength());
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public void render(final Renderer renderer) {
    }
}
