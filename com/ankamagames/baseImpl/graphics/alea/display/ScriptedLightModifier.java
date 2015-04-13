package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.graphics.engine.*;

public class ScriptedLightModifier implements LitSceneModifier
{
    private float m_red;
    private float m_green;
    private float m_blue;
    private boolean m_useless;
    
    @Override
    public void update(final int deltaTime) {
    }
    
    public void setColor(final float red, final float green, final float blue) {
        this.m_red = red;
        this.m_green = green;
        this.m_blue = blue;
        this.m_useless = (Engine.isEqualColor(this.m_red, 0.0f) && Engine.isEqualColor(this.m_green, 0.0f) && Engine.isEqualColor(this.m_blue, 0.0f));
    }
    
    @Override
    public int getPriority() {
        return 500;
    }
    
    @Override
    public boolean useless() {
        return this.m_useless;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        final int n = 0;
        colors[n] += this.m_red;
        final int n2 = 1;
        colors[n2] += this.m_green;
        final int n3 = 2;
        colors[n3] += this.m_blue;
    }
}
