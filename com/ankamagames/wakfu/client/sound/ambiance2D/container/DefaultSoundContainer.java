package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import com.ankamagames.framework.sound.group.defaultSound.*;
import com.ankamagames.framework.sound.openAL.*;

public class DefaultSoundContainer extends SoundContainer<DefaultSourceGroup>
{
    @Override
    protected AudioSource createAudioSource(final long sourceId, final boolean loop) {
        return ((DefaultSourceGroup)this.m_group).addSource(sourceId, false, false, loop, -1L);
    }
    
    @Override
    protected SoundContainer newInstance() {
        return new DefaultSoundContainer();
    }
}
