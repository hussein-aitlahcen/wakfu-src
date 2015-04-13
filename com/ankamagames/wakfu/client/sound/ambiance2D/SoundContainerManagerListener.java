package com.ankamagames.wakfu.client.sound.ambiance2D;

import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;

public interface SoundContainerManagerListener
{
    void onUpdate(long p0);
    
    void onEvent(SoundEvent p0);
}
