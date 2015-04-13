package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.external.*;

public class WakfuRunningEffectParameter extends Parameter
{
    private final WakfuRunningEffectParameterType m_parameterType;
    
    public WakfuRunningEffectParameter(final String name, final WakfuRunningEffectParameterType type) {
        super(name);
        this.m_parameterType = type;
    }
    
    public WakfuRunningEffectParameterType getParameterType() {
        return this.m_parameterType;
    }
}
