package com.ankamagames.wakfu.client.alea.ambiance.impl;

import com.ankamagames.wakfu.client.alea.ambiance.core.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock.*;

public final class WakfuEffectBlockFactory extends AbstractWakfuEffectBlockFactory
{
    private static final WakfuEffectBlockFactory m_instance;
    
    public static WakfuEffectBlockFactory getInstance() {
        return WakfuEffectBlockFactory.m_instance;
    }
    
    @Override
    protected AbstractModel createModel(final WakfuEffectBlockEnum c) {
        switch (c) {
            case WAKFU_PLAYER: {
                return new WakfuPlayerProvider.Model(c.getTypeId());
            }
            case HUMIDITY: {
                return new HumidityProvider.Model(c.getTypeId());
            }
            case WIND: {
                return new WindProvider.Model(c.getTypeId());
            }
            case TEMPERATURE: {
                return new TemperatureProvider.Model(c.getTypeId());
            }
            case WAKFU_ZONE: {
                return new WakfuZoneProvider.Model(c.getTypeId());
            }
            case DAY_RATIO: {
                return new DayRatioProvider.Model(c.getTypeId());
            }
            case SEASON: {
                return new SeasonProvider.Model(c.getTypeId());
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        m_instance = new WakfuEffectBlockFactory();
    }
}
