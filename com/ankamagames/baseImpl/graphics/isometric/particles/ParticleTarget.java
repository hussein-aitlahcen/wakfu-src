package com.ankamagames.baseImpl.graphics.isometric.particles;

public interface ParticleTarget
{
    void onParticleAttached(FreeParticleSystem p0);
    
    void onParticleDetached(FreeParticleSystem p0);
    
    void onDestroy();
}
