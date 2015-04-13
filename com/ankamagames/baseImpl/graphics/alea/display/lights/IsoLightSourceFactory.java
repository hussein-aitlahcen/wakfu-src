package com.ankamagames.baseImpl.graphics.alea.display.lights;

import com.ankamagames.framework.graphics.engine.light.*;

class IsoLightSourceFactory implements LightSourceFactory<IsoLightSource>
{
    @Override
    public IsoLightSource createLightSource() {
        return new IsoLightSource();
    }
}
