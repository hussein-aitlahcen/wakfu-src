package com.ankamagames.framework.sound;

public class SoundValidatorAll implements SoundValidator
{
    public static final SoundValidatorAll INSTANCE;
    
    @Override
    public boolean canPlayGroundSound() {
        return true;
    }
    
    @Override
    public boolean canPlaySound(final long id) {
        return true;
    }
    
    static {
        INSTANCE = new SoundValidatorAll();
    }
}
