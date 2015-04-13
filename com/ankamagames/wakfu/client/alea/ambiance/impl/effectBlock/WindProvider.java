package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class WindProvider extends FloatEffectProvider
{
    private WindProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        return (weatherInfo == null) ? 0.0f : weatherInfo.getWindForce();
    }
    
    public static class Model extends ProviderModel<WindProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public WindProvider createInstance() {
            return new WindProvider(this, null);
        }
    }
}
