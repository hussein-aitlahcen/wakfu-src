package com.ankamagames.baseImpl.graphics.alea.display.lights;

public abstract class LitSceneModifierMap implements LitSceneModifier
{
    public abstract void precompute();
    
    public void setBounds(final int minX, final int minY, final int width, final int height) {
    }
}
