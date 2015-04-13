package com.ankamagames.wakfu.client.core.weather.postprocess;

import com.ankamagames.framework.graphics.engine.fx.postProcess.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;

public class PostProcessRain extends PostProcess
{
    private static final Logger m_logger;
    private AleaIsoCamera m_camera;
    
    private static String getShaderPath() {
        try {
            return WakfuConfiguration.getInstance().getString("shadersPath");
        }
        catch (PropertyException e) {
            PostProcessRain.m_logger.error((Object)"", (Throwable)e);
            return "";
        }
    }
    
    public PostProcessRain() {
        super(getShaderPath() + "rain.cgfx", "rain0", new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("threshold", Variable.VariableType.Float), new Variable("camera", Variable.VariableType.Vector3), new Variable("high", Variable.VariableType.Vector2), new Variable("low", Variable.VariableType.Vector2), new Variable("highlight", Variable.VariableType.Float) }));
    }
    
    public void setHighlight(final float highlight) {
        this.m_params.setFloat("highlight", highlight);
    }
    
    public void setCamera(final AleaIsoCamera camera) {
        this.m_camera = camera;
    }
    
    public void setStrength(final float strength) {
        this.m_params.setFloat("gStrength", strength);
    }
    
    public void setThreshold(final float threshold) {
        this.m_params.setFloat("threshold", threshold);
    }
    
    @Override
    public void update(final int timeIncrement) {
        final float second = this.m_elapsedTime / 1000.0f;
        final float PERTURBATION_HIGH_SPEED = 0.1f;
        final float PERTURBATION_LOW_SPEED = 0.01f;
        final float x = this.m_camera.getCameraExactIsoWorldX();
        final float y = this.m_camera.getCameraExactIsoWorldY();
        final float highX = (float)Math.sin(second * 0.1f);
        final float highY = (float)Math.cos(second * 0.1f);
        final float lowX = (float)Math.sin(second * 0.01f);
        final float lowY = (float)Math.cos(second * 0.01f);
        this.m_params.setVector2("low", lowX, lowY);
        this.m_params.setVector2("high", highX, highY);
        this.m_params.setVector3("camera", x, y, this.m_camera.getZoomFactor());
        super.update(timeIncrement);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PostProcessRain.class);
    }
}
