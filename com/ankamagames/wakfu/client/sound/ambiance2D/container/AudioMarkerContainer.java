package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import java.util.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;

public class AudioMarkerContainer extends ParentSoundContainer
{
    private AudioMarkerType m_type;
    
    public AudioMarkerType getType() {
        return this.m_type;
    }
    
    public void setType(final AudioMarkerType type) {
        this.m_type = type;
    }
    
    @Override
    public void stop(final long time) {
        this.forEachSource(EventSoundContainer.STOP_PROCEDURE);
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list) {
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final SoundEvent event) {
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final AudioMarkerType type) {
        if (this.m_type == type) {
            super.getValidSoundSources(list);
        }
    }
    
    @Override
    protected boolean isAudioMarker() {
        return true;
    }
}
