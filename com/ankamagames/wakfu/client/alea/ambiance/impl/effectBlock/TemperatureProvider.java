package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class TemperatureProvider extends FloatEffectProvider
{
    private TemperatureProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        return (weatherInfo == null) ? 0.0f : weatherInfo.getTemperature();
    }
    
    public static class Model extends ProviderModel<TemperatureProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public TemperatureProvider createInstance() {
            return new TemperatureProvider(this, null);
        }
    }
}
