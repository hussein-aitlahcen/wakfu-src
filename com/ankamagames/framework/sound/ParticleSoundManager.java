package com.ankamagames.framework.sound;

public abstract class ParticleSoundManager
{
    private static ParticleSoundManager m_instance;
    
    public static ParticleSoundManager getInstance() {
        return ParticleSoundManager.m_instance;
    }
    
    public static void setInstance(final ParticleSoundManager instance) {
        ParticleSoundManager.m_instance = instance;
    }
    
    public abstract boolean canPlaySoundById(final long p0, final long p1);
    
    public abstract void registerSound(final int p0, final long p1, final long p2, final long p3);
    
    public abstract void cleanAps(final int p0, final boolean p1);
    
    public abstract boolean playApsSound(final int p0, final int p1, final int p2, final int p3);
}
