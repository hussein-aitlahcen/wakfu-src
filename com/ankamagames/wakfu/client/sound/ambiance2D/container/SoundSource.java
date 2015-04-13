package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import java.util.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import gnu.trove.*;

public interface SoundSource
{
    void play(long p0);
    
    void stop(long p0);
    
    boolean isValid();
    
    SoundContainer getValidSoundSource();
    
    void getValidSoundSources(ArrayList<SoundContainer> p0);
    
    void getValidSoundSources(ArrayList<SoundContainer> p0, SoundEvent p1);
    
    void getValidSoundSources(ArrayList<SoundContainer> p0, AudioMarkerType p1);
    
    int getId();
    
    void setParent(AbstractSoundContainer p0);
    
    AbstractSoundContainer getParent();
    
    void forEachSource(TObjectProcedure<SoundContainer> p0);
}
