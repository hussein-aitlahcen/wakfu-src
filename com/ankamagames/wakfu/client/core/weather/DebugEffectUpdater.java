package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.baseImpl.graphics.script.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;

public class DebugEffectUpdater implements WeatherEffectUpdater
{
    private float m_windStrength;
    private float m_windDirection;
    private TObjectFloatHashMap<Weather> m_params;
    private boolean m_activated;
    
    public DebugEffectUpdater() {
        super();
        this.m_windStrength = 0.0f;
        this.m_windDirection = 0.0f;
        this.m_params = new TObjectFloatHashMap<Weather>();
        this.m_activated = true;
    }
    
    @Override
    public void setActivated(final boolean activated) {
        this.m_activated = activated;
    }
    
    @Override
    public void setWindStrength(final float windStrength) {
        this.m_windStrength = windStrength;
    }
    
    @Override
    public void setWindDirection(final float windDirection) {
        this.m_windDirection = windDirection;
    }
    
    @Override
    public void setStrength(final Weather weather, final float strength) {
        this.m_params.put(weather, strength);
    }
    
    @Override
    public void updateParameters(final long now, final FloatParameter windStrength, final FloatParameter windDirection, final WeatherEffect windEffect, final HashMap<Weather, WeatherEffect> effects) {
        if (this.m_activated) {
            windStrength.set(this.m_windStrength);
            windDirection.set(1.0f);
            final IsoWorldScene scene = EffectFunctionsLibrary.getInstance().getWorldScene();
            if (windStrength.getCurrent() > 0.0f && !windEffect.isRunning()) {
                windEffect.start(scene);
            }
            this.m_params.forEachEntry(new TObjectFloatProcedure<Weather>() {
                @Override
                public boolean execute(Weather weather, final float value) {
                    if (weather == Weather.RAIN) {
                        weather = ((value == 1.0f) ? Weather.STORM : Weather.RAIN);
                        final WeatherEffect effect = effects.get((value != 1.0f) ? Weather.STORM : Weather.RAIN);
                        if (effect != null) {
                            effect.setStrength(0.0f);
                        }
                    }
                    final WeatherEffect effect = effects.get(weather);
                    if (effect != null) {
                        effect.setStrength(value);
                    }
                    return true;
                }
            });
        }
        else {
            final float defaultValue = 0.0f;
            windStrength.set(0.0f);
            windStrength.set(0.0f);
            final IsoWorldScene scene2 = EffectFunctionsLibrary.getInstance().getWorldScene();
            if (windStrength.getCurrent() > 0.0f && !windEffect.isRunning()) {
                windEffect.start(scene2);
            }
            for (final Map.Entry<Weather, WeatherEffect> entry : effects.entrySet()) {
                final WeatherEffect effect = entry.getValue();
                if (effect != null) {
                    effect.setStrength(0.0f);
                }
            }
        }
    }
}
