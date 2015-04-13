package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.fx.postProcess.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class AngleDesatShader extends PostProcess
{
    private static final String MIN_ALPHA = "minAlpha";
    private static final String MAX_ALPHA = "maxAlpha";
    private static final String PX = "px";
    private static final String PY = "py";
    private static final String PW = "pw";
    private static final String PH = "ph";
    private static final String TEX_SIZE = "texSize";
    
    private static EffectParams createParams() {
        return new EffectParams(new Variable[] { new Variable("minAlpha", Variable.VariableType.Float), new Variable("maxAlpha", Variable.VariableType.Float), new Variable("px", Variable.VariableType.Float), new Variable("py", Variable.VariableType.Float), new Variable("pw", Variable.VariableType.Float), new Variable("ph", Variable.VariableType.Float), new Variable("texSize", Variable.VariableType.Float) });
    }
    
    public AngleDesatShader() {
        super(Xulor.getInstance().m_shaderPath + "gui.cgfx", "AngleDesat", createParams());
    }
    
    public void setMinAlpha(final float alpha) {
        this.m_params.setFloat("minAlpha", alpha);
    }
    
    public void setMaxAlpha(final float alpha) {
        this.m_params.setFloat("maxAlpha", alpha);
    }
    
    public void setPixmap(final Pixmap p) {
        this.m_params.setFloat("px", p.getX());
        this.m_params.setFloat("py", p.getY());
        this.m_params.setFloat("pw", p.getWidth());
        this.m_params.setFloat("ph", p.getHeight());
        final Point2i size = p.getTexture().getPowerOfTwoSize();
        this.m_params.setFloat("texSize", size.getX());
    }
    
    @Override
    public void prepare(final EntitySprite entity) {
        entity.setEffect(this.getEffect(), this.getTechniqueCRC(), this.m_params);
    }
    
    public EffectParams getParams() {
        if (!HardwareFeatureManager.INSTANCE.isShaderSupported()) {
            this.m_params.setFloat("gColorScale", FxConstants.COLOR_SCALE_FOR_UI_PARAMS.getFloat("gColorScale"));
        }
        return this.m_params;
    }
}
