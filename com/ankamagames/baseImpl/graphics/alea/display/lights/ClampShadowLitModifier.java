package com.ankamagames.baseImpl.graphics.alea.display.lights;

public class ClampShadowLitModifier implements LitSceneModifier
{
    private float m_minValue;
    private static final ClampShadowLitModifier m_instance;
    
    public static ClampShadowLitModifier getInstance() {
        return ClampShadowLitModifier.m_instance;
    }
    
    private ClampShadowLitModifier() {
        super();
        this.m_minValue = 0.3f;
    }
    
    @Override
    public void update(final int deltaTime) {
    }
    
    @Override
    public int getPriority() {
        return 300;
    }
    
    @Override
    public boolean useless() {
        return false;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        final float value = this.m_minValue;
        if (colors[0] < this.m_minValue) {
            colors[0] = value;
        }
        if (colors[1] < this.m_minValue) {
            colors[1] = value;
        }
        if (colors[2] < this.m_minValue) {
            colors[2] = value;
        }
    }
    
    static {
        m_instance = new ClampShadowLitModifier();
    }
}
