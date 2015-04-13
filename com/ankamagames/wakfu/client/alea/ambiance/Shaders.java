package com.ankamagames.wakfu.client.alea.ambiance;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.shaders.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class Shaders
{
    private static final Logger m_logger;
    private static final int SIRROCO_DURATION_IN_MS = 50000;
    private static final int SIRROCO_TO_HEAT_TRANSITION_DURATION_IN_MS = 6000;
    private static final int HEAT_WAVE_DURATION_IN_MS = 70000;
    private static final int HEAT_TO_SIRROCO_TRANSITION_DURATION_IN_MS = 15000;
    private static final int TOTAL_SAHARACH_EFFECT_DURATION = 141000;
    
    public static String getShaderPath() {
        try {
            return WakfuConfiguration.getInstance().getString("shadersPath");
        }
        catch (PropertyException e) {
            Shaders.m_logger.error((Object)"", (Throwable)e);
            return "";
        }
    }
    
    public static int applyPast() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "death.cgfx", "death0");
        effect.setForced(true);
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyDeath() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "death.cgfx", "death0");
        start(effect, new EaseInStrength(3000));
        return effect.getId();
    }
    
    public static int applyIncarnam() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "incarnam.cgfx", "incarnam");
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyInBag() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "bag.cgfx", "bag");
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyZinit() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "zinit.cgfx", "zinit");
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyMine() {
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "mine.cgfx", "mine");
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyMoonLight() {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("position", Variable.VariableType.Vector2) });
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "moon_light.cgfx", "moon_light", params) {
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                params.setVector2("position", player.getWorldX(), player.getWorldY());
            }
        };
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyShukrute() {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2) });
        params.setFloat("gStrength", 1.0f);
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "postprocess.cgfx", "shukrute1", params) {
            int m_elapsedTime = 0;
            
            @Override
            public void update(final int timeIncrement) {
                this.m_elapsedTime += timeIncrement;
                super.update(timeIncrement);
                final float x0 = MathHelper.cosf(0.017453292f * this.m_elapsedTime / 271.0f);
                final float y0 = MathHelper.sinf(0.017453292f * this.m_elapsedTime / 233.0f);
                final float x = 2.0f * MathHelper.cosf(0.017453292f * this.m_elapsedTime / 2503.0f);
                final float y = 2.0f * MathHelper.sinf(0.017453292f * this.m_elapsedTime / 3373.0f);
                params.setVector2("gDistordLow", x0, y0);
                params.setVector2("gDistordHigh", x, y);
            }
        };
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static void start(final ShaderEffect effect, final StrengthHandler strengthHandler) {
        effect.setCamera(WakfuClientInstance.getInstance().getWorldScene().getIsoCamera());
        effect.start(strengthHandler);
        EffectManager.getInstance().addWorldEffect(effect);
    }
    
    public static void removeEffect(final int effectId) {
        final EffectBase worldEffect = EffectManager.getInstance().getWorldEffect(effectId);
        if (worldEffect != null) {
            worldEffect.stop(500);
        }
    }
    
    public static int applyFog() {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        params.setFloat("gStrength", 1.0f);
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "brume.cgfx", "brume", params) {
            int m_elapsedTime = 0;
            
            @Override
            public void update(final int timeIncrement) {
                this.m_elapsedTime += timeIncrement;
                super.update(timeIncrement);
                final float timeLow = this.m_elapsedTime * 0.0011f;
                final float timeHigh = this.m_elapsedTime * 0.0013f;
                params.setVector2("gDistordLow", -timeLow * 0.0013f, timeLow * 0.0011f);
                params.setVector2("gDistordHigh", timeHigh * 0.011f + timeHigh * 0.023f, timeHigh * 0.013f);
                final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX() / 1024.0f, isoCamera.getScreenFloatY() / 1024.0f, isoCamera.getZoomFactor());
            }
        };
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applyPixelate() {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("screen_size", Variable.VariableType.Vector2), new Variable("pixel_size", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        params.setVector2("pixel_size", 10.0f, 10.0f);
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "pixelate.cgfx", "pixelate", params) {
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
                params.setVector2("screen_size", scene.getFrustumWidth(), scene.getFrustumHeight());
                final AleaIsoCamera isoCamera = scene.getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX(), isoCamera.getScreenFloatY(), isoCamera.getZoomFactor());
            }
        };
        start(effect, new StrengthHandler(1.0f));
        return effect.getId();
    }
    
    public static int applySirocco(final StrengthHandler strengthHandler) {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("caniculeToSirroco", Variable.VariableType.Float), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2), new Variable("gHeatDistordLow", Variable.VariableType.Vector2), new Variable("gHeatDistordHigh", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "Sirocco.cgfx", "sirocco", params) {
            float m_timeLow = 0.0f;
            float m_timeHigh = 0.0f;
            float m_heatTimeLow = 0.0f;
            float m_heatTimeHigh = 0.0f;
            long m_time = 0L;
            
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                this.m_time += timeIncrement;
                final float strength = strengthHandler.getStrength();
                final float heatDelta = 7.0f * timeIncrement;
                final float cloudSpeed = 20.0f * strength * timeIncrement;
                final float heatWaveToSirroco = this.computeFactor();
                this.m_timeLow += cloudSpeed * 0.0011f;
                this.m_timeHigh += cloudSpeed * 0.0013f;
                this.m_heatTimeLow += heatDelta * 0.05f;
                this.m_heatTimeHigh += heatDelta * 0.06f;
                final float xx0 = MathHelper.cos(0.017453292f * this.m_heatTimeLow * 0.13f) * 0.2f;
                final float yy0 = MathHelper.sin(0.017453292f * this.m_heatTimeLow * 0.11f) * 0.2f;
                final float xx = MathHelper.cos(0.017453292f * this.m_heatTimeHigh * -0.34f) * 0.2f;
                final float yy = MathHelper.sin(0.017453292f * this.m_heatTimeHigh * 0.2f) * 0.2f;
                params.setFloat("gStrength", strength);
                params.setFloat("caniculeToSirroco", heatWaveToSirroco);
                params.setVector2("gDistordLow", this.m_timeLow * 0.0013f, this.m_timeLow * 0.0011f);
                params.setVector2("gDistordHigh", -(this.m_timeHigh * 0.011f + this.m_timeHigh * 0.023f), 1.5f * this.m_timeHigh * 0.013f);
                params.setVector2("gHeatDistordLow", xx0, yy0);
                params.setVector2("gHeatDistordHigh", xx, yy);
                final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX() / 1024.0f, isoCamera.getScreenFloatY() / 1024.0f, isoCamera.getZoomFactor());
            }
            
            private float computeFactor() {
                final long duration = this.m_time % 141000L;
                if (duration < 70000L) {
                    return 0.0f;
                }
                if (duration < 85000L) {
                    return (duration - 70000L) / 15000.0f;
                }
                if (duration < 135000L) {
                    return 1.0f;
                }
                final long durationInTransition = duration - 70000L - 15000L - 50000L;
                return 1.0f - durationInTransition / 6000.0f;
            }
        };
        start(effect, strengthHandler);
        return effect.getId();
    }
    
    public static int applyBlizzard(final StrengthHandler strengthHandler) {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "blizzard.cgfx", "blizzard", params) {
            float m_timeLow = 0.0f;
            float m_timeHigh = 0.0f;
            
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                final float strength = strengthHandler.getStrength();
                final float cloudSpeed = 20.0f * strength * timeIncrement;
                this.m_timeLow += cloudSpeed * 0.0011f;
                this.m_timeHigh += cloudSpeed * 0.0013f;
                params.setFloat("gStrength", strength);
                params.setVector2("gDistordLow", this.m_timeLow * 0.0013f, this.m_timeLow * 0.0011f);
                params.setVector2("gDistordHigh", -(this.m_timeHigh * 0.011f + this.m_timeHigh * 0.023f), 1.5f * this.m_timeHigh * 0.013f);
                final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX() / 1024.0f, isoCamera.getScreenFloatY() / 1024.0f, isoCamera.getZoomFactor());
            }
        };
        start(effect, strengthHandler);
        return effect.getId();
    }
    
    public static int applyHallucination(final StrengthHandler strengthHandler) {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("gColorVariation", Variable.VariableType.Vector3), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2), new Variable("gHeatDistordLow", Variable.VariableType.Vector2), new Variable("gHeatDistordHigh", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "hallucination.cgfx", "hallucination", params) {
            float m_timeLow = 0.0f;
            float m_timeHigh = 0.0f;
            float m_heatTimeLow = 0.0f;
            float m_heatTimeHigh = 0.0f;
            float m_colorTime = 0.0f;
            long m_time = 0L;
            
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                this.m_time += timeIncrement;
                final float strength = strengthHandler.getStrength();
                final float heatDelta = 7.0f * timeIncrement;
                final float cloudSpeed = 20.0f * strength * timeIncrement;
                final float colorDelta = 5.0f * timeIncrement;
                this.m_timeLow += cloudSpeed * 0.0011f;
                this.m_timeHigh += cloudSpeed * 0.0013f;
                this.m_heatTimeLow += heatDelta * 0.02f;
                this.m_heatTimeHigh += heatDelta * 0.09f;
                this.m_colorTime += colorDelta * 0.02f;
                final float xx0 = MathHelper.cos(0.017453292f * this.m_heatTimeLow * -0.34f) * 0.2f;
                final float yy0 = MathHelper.sin(0.017453292f * this.m_heatTimeLow * 0.2f) * 0.2f;
                final float xx = MathHelper.cos(0.017453292f * this.m_heatTimeHigh * -0.34f) * 0.2f;
                final float yy = MathHelper.sin(0.017453292f * this.m_heatTimeHigh * 0.2f) * 0.2f;
                final float r = MathHelper.cos(0.017453292f * this.m_colorTime * -0.1f) * 0.1f;
                final float g = MathHelper.cos(0.017453292f * this.m_colorTime * -0.4f) * 0.1f;
                final float b = MathHelper.cos(0.017453292f * this.m_colorTime * -0.7f) * 0.1f;
                params.setFloat("gStrength", strength);
                params.setVector2("gDistordLow", this.m_timeLow * 0.0013f, this.m_timeLow * 0.0011f);
                params.setVector2("gDistordHigh", -(this.m_timeHigh * 0.011f + this.m_timeHigh * 0.023f), 1.5f * this.m_timeHigh * 0.013f);
                params.setVector2("gHeatDistordLow", xx0, yy0);
                params.setVector2("gHeatDistordHigh", xx, yy);
                params.setVector3("gColorVariation", r, g, b);
                final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX() / 1024.0f, isoCamera.getScreenFloatY() / 1024.0f, isoCamera.getZoomFactor());
            }
        };
        start(effect, strengthHandler);
        return effect.getId();
    }
    
    public static int applyGloomy(final StrengthHandler strengthHandler) {
        final EffectParams params = new EffectParams(new Variable[] { new Variable("gStrength", Variable.VariableType.Float), new Variable("gDistordLow", Variable.VariableType.Vector2), new Variable("gDistordHigh", Variable.VariableType.Vector2), new Variable("camera", Variable.VariableType.Vector3) });
        final ShaderEffect effect = new ShaderEffect(getShaderPath() + "Gloomy.cgfx", "Gloomy", params) {
            float m_timeLow = 0.0f;
            float m_timeHigh = 0.0f;
            
            @Override
            public void update(final int timeIncrement) {
                super.update(timeIncrement);
                final float strength = strengthHandler.getStrength();
                final float cloudSpeed = 1.0f * strength * timeIncrement;
                this.m_timeLow += cloudSpeed * 0.0011f;
                this.m_timeHigh += cloudSpeed * 0.0013f;
                params.setFloat("gStrength", strength);
                params.setVector2("gDistordLow", this.m_timeLow * 0.0013f, this.m_timeLow * 0.0011f);
                params.setVector2("gDistordHigh", -(this.m_timeHigh * 0.011f + this.m_timeHigh * 0.023f), 1.5f * this.m_timeHigh * 0.013f);
                final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
                params.setVector3("camera", isoCamera.getScreenFloatX() / 1024.0f, isoCamera.getScreenFloatY() / 1024.0f, isoCamera.getZoomFactor());
            }
        };
        start(effect, strengthHandler);
        return effect.getId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Shaders.class);
    }
}
