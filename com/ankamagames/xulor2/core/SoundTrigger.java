package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.decorator.*;

public class SoundTrigger extends AbstractTrigger
{
    public static final String TAG = "Sound";
    private int m_soundId;
    
    public SoundTrigger() {
        super();
        this.m_soundId = -1;
    }
    
    @Override
    public String getTag() {
        return "Sound";
    }
    
    public void setSoundId(final int id) {
        this.m_soundId = id;
    }
    
    public int getSoundId() {
        return this.m_soundId;
    }
    
    @Override
    public void run() {
        if (this.m_soundId != -1) {
            XulorSoundManager.getInstance().playSound(this.m_soundId);
        }
    }
}
