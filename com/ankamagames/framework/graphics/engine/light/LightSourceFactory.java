package com.ankamagames.framework.graphics.engine.light;

public interface LightSourceFactory<T extends LightSource>
{
    T createLightSource();
}
