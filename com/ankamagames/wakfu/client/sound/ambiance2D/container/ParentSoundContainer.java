package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import java.util.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.exporter.*;
import gnu.trove.*;

public class ParentSoundContainer extends AbstractSoundContainer<SoundSource>
{
    @Override
    public void play(final long time) {
    }
    
    @Override
    public void stop(final long time) {
    }
    
    @Override
    public SoundContainer getValidSoundSource() {
        if (!this.isValid()) {
            return null;
        }
        for (int i = this.m_sources.size() - 1; i >= 0; --i) {
            final SoundContainer source = this.m_sources.get(i).getValidSoundSource();
            if (source != null) {
                return source;
            }
        }
        return null;
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list) {
        if (!this.isValid()) {
            return;
        }
        for (int i = this.m_sources.size() - 1; i >= 0; --i) {
            this.m_sources.get(i).getValidSoundSources(list);
        }
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final SoundEvent event) {
        if (!this.isValid()) {
            return;
        }
        for (int i = this.m_sources.size() - 1; i >= 0; --i) {
            this.m_sources.get(i).getValidSoundSources(list, event);
        }
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final AudioMarkerType type) {
        if (!this.isValid()) {
            return;
        }
        for (int i = this.m_sources.size() - 1; i >= 0; --i) {
            this.m_sources.get(i).getValidSoundSources(list, type);
        }
    }
    
    @Override
    protected void fillRawSoundContainer(final RawSoundContainer rsc) {
        super.fillRawSoundContainer(rsc);
        rsc.m_parentContainer = true;
        for (int i = 0, size = this.m_sources.size(); i < size; ++i) {
            rsc.m_soundSources.add(this.m_sources.get(i).getId());
        }
    }
    
    @Override
    public void forEachSource(final TObjectProcedure<SoundContainer> proc) {
        for (int i = this.m_sources.size() - 1; i >= 0; --i) {
            this.m_sources.get(i).forEachSource(proc);
        }
    }
}
