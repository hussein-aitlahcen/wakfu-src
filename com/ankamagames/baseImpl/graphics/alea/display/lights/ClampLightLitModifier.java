package com.ankamagames.baseImpl.graphics.alea.display.lights;

public class ClampLightLitModifier implements LitSceneModifier
{
    private float m_minValue;
    private static final ClampLightLitModifier m_instance;
    
    public static ClampLightLitModifier getInstance() {
        return ClampLightLitModifier.m_instance;
    }
    
    private ClampLightLitModifier() {
        super();
        this.m_minValue = 1.05f;
    }
    
    @Override
    public void update(final int deltaTime) {
    }
    
    @Override
    public int getPriority() {
        return 800;
    }
    
    @Override
    public boolean useless() {
        return false;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        final float value = this.m_minValue;
        if (colors[0] > this.m_minValue) {
            colors[0] = value;
        }
        if (colors[1] > this.m_minValue) {
            colors[1] = value;
        }
        if (colors[2] > this.m_minValue) {
            colors[2] = value;
        }
    }
    
    static {
        m_instance = new ClampLightLitModifier();
    }
}
