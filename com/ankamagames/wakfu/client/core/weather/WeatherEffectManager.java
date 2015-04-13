package com.ankamagames.wakfu.client.core.weather;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.weather.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class WeatherEffectManager implements Runnable
{
    public static final WeatherEffectManager INSTANCE;
    protected static final Logger m_logger;
    public static final int DEFAULT_CHANGE_DURATION = 5000;
    private Weather m_currentWeather;
    private WeatherEffect m_currentEffect;
    private final FloatParameter m_windStrength;
    private final FloatParameter m_windDirection;
    private final Wind m_windEffect;
    private boolean m_lockChanging;
    private final HashMap<Weather, WeatherEffect> m_effects;
    private WeatherEffectUpdater m_debugModifier;
    private boolean m_activated;
    private final ArrayList<WeatherEffect> m_effectList;
    
    private WeatherEffectManager() {
        super();
        this.m_currentWeather = Weather.SUNNY;
        this.m_windStrength = new FloatParameter(0.0f);
        this.m_windDirection = new FloatParameter(WindDirection.EAST.getDirection());
        this.m_windEffect = new Wind();
        this.m_effects = new HashMap<Weather, WeatherEffect>();
        this.m_activated = true;
        this.m_effectList = new ArrayList<WeatherEffect>();
    }
    
    public void setLockChanging(final boolean lockChanging) {
        this.m_lockChanging = lockChanging;
    }
    
    private long now() {
        return System.currentTimeMillis();
    }
    
    public void intialize() {
        final Rain rainEffect = new Rain();
        final Snow snowEffect = new Snow();
        this.addEffect(Weather.CLOUDY, rainEffect);
        this.addEffect(Weather.RAIN, rainEffect);
        this.addEffect(Weather.CHAOS, new Storm());
        this.addEffect(Weather.SNOW, snowEffect);
        this.addEffect(Weather.FOGGY, new Freeze());
        this.addEffect(Weather.STORM, rainEffect);
        this.addEffect(Weather.BLIZZARD, snowEffect);
        this.m_effectList.add(this.m_windEffect);
    }
    
    public void setRainHighlight(final float strength) {
        assert strength >= 0.0f && strength <= 1.0f;
        for (final WeatherEffect effect : this.m_effectList) {
            if (effect instanceof Rain) {
                ((Rain)effect).setHighlight(strength);
            }
        }
    }
    
    private void addEffect(final Weather weather, final WeatherEffect effect) {
        this.m_effects.put(weather, effect);
        if (!this.m_effectList.contains(effect)) {
            this.m_effectList.add(effect);
        }
    }
    
    public void start() {
        ProcessScheduler.getInstance().schedule(this, 50L);
    }
    
    public void setActivated(final boolean activated) {
        if (!activated) {
            this.m_activated = false;
            return;
        }
        if (!this.m_activated) {
            this.m_activated = true;
            for (final WeatherEffect effect : this.m_effectList) {
                if (effect != null) {
                    effect.setStrength(0.0f);
                }
            }
        }
    }
    
    public void stopAllEffects() {
        if (this.m_currentEffect != null) {
            this.stopSound(this.m_currentWeather);
        }
        for (final WeatherEffect effect : this.m_effectList) {
            if (effect != null) {
                effect.setStrength(0.0f);
                effect.stop();
            }
        }
        this.m_currentWeather = null;
        this.m_currentEffect = null;
    }
    
    public void setEffectsVisibility(final boolean visible) {
        for (final WeatherEffect effect : this.m_effectList) {
            if (effect != null) {
                effect.setVisible(visible);
            }
        }
    }
    
    private WeatherEventType getWeatherSoundType(final Weather weather) {
        if (weather == null) {
            return null;
        }
        switch (weather) {
            case RAIN:
            case STORM:
            case CHAOS: {
                return WeatherEventType.RAIN_BEGIN;
            }
            case SNOW:
            case BLIZZARD: {
                return WeatherEventType.SNOW_BEGIN;
            }
            default: {
                return null;
            }
        }
    }
    
    public void playCurrentSoundEvent() {
        this.playSound(this.m_currentWeather);
    }
    
    private void playSound(final Weather weather) {
        if (weather == null) {
            return;
        }
        switch (weather) {
            case RAIN:
            case STORM:
            case CHAOS: {
                WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.RAIN_BEGIN));
                break;
            }
            case SNOW:
            case BLIZZARD: {
                WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.SNOW_BEGIN));
                break;
            }
        }
    }
    
    private void stopSound(final Weather weather) {
        if (weather == null) {
            return;
        }
        switch (weather) {
            case RAIN:
            case STORM:
            case CHAOS: {
                WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.RAIN_END));
                break;
            }
            case SNOW:
            case BLIZZARD: {
                WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.SNOW_END));
                break;
            }
        }
    }
    
    public void changeTo(final Weather weather, final int duration) {
        if (this.m_lockChanging) {
            return;
        }
        if (weather == this.m_currentWeather) {
            return;
        }
        final long now = this.now();
        final WeatherEffect effect = this.m_effects.get(weather);
        if (effect != this.m_currentEffect) {
            if (this.m_currentEffect != null) {
                this.m_currentEffect.fadeTo(0.0f, duration, now);
            }
            this.m_currentEffect = effect;
        }
        if (this.getWeatherSoundType(weather) != this.getWeatherSoundType(this.m_currentWeather)) {
            this.stopSound(this.m_currentWeather);
            this.playSound(weather);
        }
        this.m_currentWeather = weather;
        if (this.m_currentEffect != null) {
            switch (this.m_currentWeather) {
                case CLOUDY:
                case FOGGY: {
                    this.m_currentEffect.fadeTo(0.33f, duration, now);
                    break;
                }
                case RAIN:
                case SNOW: {
                    this.m_currentEffect.fadeTo(0.66f, duration, now);
                    break;
                }
                case STORM:
                case BLIZZARD: {
                    this.m_currentEffect.fadeTo(1.0f, duration, now);
                    break;
                }
                case SUNNY: {
                    this.m_currentEffect.fadeTo(1.0f, duration, now);
                    break;
                }
                case CHAOS: {
                    this.m_currentEffect.fadeTo(1.0f, duration, now);
                    break;
                }
            }
        }
    }
    
    public void changeTo(final Weather weather) {
        this.changeTo(weather, 5000);
    }
    
    public void changeWindStrength(final float strength) {
        this.changeWindStrength(strength, 5000);
    }
    
    private void changeWindStrength(final float strength, final int duration) {
        this.m_windStrength.changeTo(strength, duration, this.now());
    }
    
    public void changeWindDirection(final WindDirection direction) {
        this.changeWindDirection(direction, 5000);
    }
    
    private void changeWindDirection(final WindDirection direction, final int duration) {
        this.m_windDirection.changeTo(direction.getDirection(), duration, this.now());
    }
    
    @Override
    public void run() {
        if (this.m_currentWeather == null) {
            return;
        }
        try {
            final IsoWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
            if (!this.m_activated) {
                for (int i = 0; i < this.m_effectList.size(); ++i) {
                    final WeatherEffect effect = this.m_effectList.get(i);
                    if (effect != null) {
                        effect.update(scene, 0.0f, 0.0f);
                    }
                }
                return;
            }
            final long now = this.now();
            if (this.m_debugModifier == null) {
                this.updateParameters(now, scene);
            }
            else {
                this.m_debugModifier.updateParameters(now, this.m_windStrength, this.m_windDirection, this.m_windEffect, this.m_effects);
            }
            float windDirection = this.m_windDirection.getCurrent();
            windDirection += Math.abs(windDirection) * Perlin2D.perlinNoise(0.0f, now, 1.0f, 5, Perlin2D.Interpolation.COSINE);
            float windStrength = this.m_windStrength.getCurrent();
            windStrength += Math.abs(windStrength) * Perlin2D.perlinNoise(1.0f, now, 1.0f, 5, Perlin2D.Interpolation.COSINE);
            for (int j = 0; j < this.m_effectList.size(); ++j) {
                final WeatherEffect effect2 = this.m_effectList.get(j);
                if (effect2 != null) {
                    if (effect2.getStrength() > 0.0f && !effect2.isRunning()) {
                        effect2.start(scene);
                    }
                    effect2.update(scene, windDirection, windStrength);
                }
            }
        }
        catch (Exception e) {
            WeatherEffectManager.m_logger.error((Object)"Exception ", (Throwable)e);
        }
    }
    
    private void updateParameters(final long now, final IsoWorldScene scene) {
        this.m_windStrength.update(now);
        this.m_windDirection.update(now);
        if (this.m_windStrength.getCurrent() > 0.0f && !this.m_windEffect.isRunning()) {
            this.m_windEffect.start(scene);
        }
        for (int i = 0; i < this.m_effectList.size(); ++i) {
            final WeatherEffect weatherEffect = this.m_effectList.get(i);
            if (weatherEffect != null) {
                weatherEffect.updateStrength(now);
            }
        }
    }
    
    public void setParamUpdater(final WeatherEffectUpdater updater) {
        this.m_debugModifier = updater;
    }
    
    public static void throwThunder(final IsoCamera camera, final Color flashColor) {
        final int count = MathHelper.random(1, 3);
        int wait = 0;
        int duration = 0;
        for (int i = 0; i < count; ++i) {
            final int d = createFlash(wait, flashColor);
            duration = wait + d;
            wait += MathHelper.random(200, 1000);
        }
        duration = Math.max(800, duration);
        final CameraEffectShake shake = new CameraEffectShake();
        shake.setCamera(camera);
        shake.activate(true);
        shake.setParams(70.0f, -2.0f);
        shake.start(new WaitTimedStrength(100, 100, duration / 2, duration / 2));
        EffectManager.getInstance().addWorldEffect(shake);
        WakfuSoundManager.getInstance().onEvent(new WeatherSoundEvent(WeatherEventType.THUNDER));
    }
    
    private static int createFlash(final int wait, final Color color) {
        final FullScreenSprite sprite = new FullScreenSprite();
        sprite.setTexture("snow.tga");
        sprite.setBlendFunc(BlendModes.SrcAlpha, BlendModes.One);
        sprite.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        final float size = MathHelper.random(0.7f, 1.0f) + 2.0f;
        final float posX = MathHelper.random(-2.0f, 2.0f);
        final float posY = MathHelper.random(0.0f, 1.0f) + size * 0.5f;
        sprite.setPosition(posX, posY);
        sprite.setSize(size, size);
        final int duration = MathHelper.random(0, 200) + 100;
        sprite.start(new WaitTimedStrength(wait, 25, duration, 75));
        EffectManager.getInstance().addWorldEffect(sprite);
        return duration;
    }
    
    static {
        INSTANCE = new WeatherEffectManager();
        m_logger = Logger.getLogger((Class)WeatherEffectManager.class);
    }
}
