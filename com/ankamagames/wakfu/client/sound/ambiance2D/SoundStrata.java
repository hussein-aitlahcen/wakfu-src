package com.ankamagames.wakfu.client.sound.ambiance2D;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.container.*;
import com.ankamagames.framework.sound.group.*;

public class SoundStrata implements AudioSourceGroupUpdateListener
{
    private static final Logger m_logger;
    private static final boolean DEBUG = false;
    private long m_updateInterval;
    private int m_id;
    private String m_name;
    private final IntObjectLightWeightMap<IntObjectLightWeightMap<ArrayList<SoundContainer>>> m_currentEventContainers;
    private final LongObjectLightWeightMap<ArrayList<SoundContainer>> m_currentAudioMarkers;
    private final ArrayList<ParentSoundContainer> m_containers;
    private final ArrayList<SoundContainer> m_currentSources;
    private final ArrayList<AudioSourceGroup> m_registeredGroups;
    private final TIntArrayList m_registeredGroupsUsage;
    private boolean m_needsUpdate;
    private final ArrayList<SoundStrataListener> m_listeners;
    private final ArrayList<SoundContainer> m_validSources;
    private final ArrayList<SoundContainer> m_validEventContainers;
    private final ArrayList<SoundContainer> m_validAudioMarkerTypeContainers;
    
    public SoundStrata() {
        super();
        this.m_currentEventContainers = new IntObjectLightWeightMap<IntObjectLightWeightMap<ArrayList<SoundContainer>>>(4);
        this.m_currentAudioMarkers = new LongObjectLightWeightMap<ArrayList<SoundContainer>>();
        this.m_containers = new ArrayList<ParentSoundContainer>();
        this.m_currentSources = new ArrayList<SoundContainer>();
        this.m_registeredGroups = new ArrayList<AudioSourceGroup>();
        this.m_registeredGroupsUsage = new TIntArrayList();
        this.m_needsUpdate = false;
        this.m_listeners = new ArrayList<SoundStrataListener>();
        this.m_validSources = new ArrayList<SoundContainer>();
        this.m_validEventContainers = new ArrayList<SoundContainer>();
        this.m_validAudioMarkerTypeContainers = new ArrayList<SoundContainer>();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public ArrayList<SoundContainer> getCurrentSources() {
        return this.m_currentSources;
    }
    
    public ArrayList<ParentSoundContainer> getContainers() {
        return this.m_containers;
    }
    
    public void addContainer(final ParentSoundContainer container) {
        this.m_containers.add(container);
    }
    
    public long getUpdateInterval() {
        return this.m_updateInterval;
    }
    
    public void setUpdateInterval(final long updateInterval) {
        this.m_updateInterval = updateInterval;
    }
    
    public void clear() {
        this.m_containers.clear();
    }
    
    public void addListener(final SoundStrataListener l) {
        this.m_listeners.add(l);
    }
    
    public void removeListener(final SoundStrataListener l) {
        this.m_listeners.remove(l);
    }
    
    public void needsUpdate(final long date) {
        if (this.m_currentSources.size() == 0) {
            this.evaluateAndPlay(date);
        }
        else {
            this.m_needsUpdate = true;
        }
    }
    
    private void tryToRegisterGroup(final AudioSourceGroup group) {
        final int index = this.m_registeredGroups.indexOf(group);
        if (index == -1) {
            this.m_registeredGroups.add(group);
            this.m_registeredGroupsUsage.add(1);
            group.addUpdateListener(this);
        }
        else {
            this.m_registeredGroupsUsage.setQuick(index, this.m_registeredGroupsUsage.getQuick(index) + 1);
        }
    }
    
    private void tryToUnregisterGroup(final AudioSourceGroup group) {
        final int index = this.m_registeredGroups.indexOf(group);
        if (index != -1) {
            final int usage = this.m_registeredGroupsUsage.getQuick(index);
            if (usage > 1) {
                this.m_registeredGroupsUsage.setQuick(index, usage - 1);
            }
            else {
                this.m_registeredGroups.remove(index);
                this.m_registeredGroupsUsage.remove(index);
                group.removeUpdateListener(this);
            }
        }
    }
    
    private void selectCurrentContainers() {
        this.m_validSources.clear();
        for (int i = this.m_containers.size() - 1; i >= 0; --i) {
            this.m_containers.get(i).getValidSoundSources(this.m_validSources);
        }
    }
    
    public void stop() {
        this.stop(System.currentTimeMillis());
    }
    
    public synchronized void stop(final long date) {
        for (int j = this.m_currentEventContainers.size() - 1; j >= 0; --j) {
            final IntObjectLightWeightMap<ArrayList<SoundContainer>> list = this.m_currentEventContainers.getQuickValue(j);
            if (list != null) {
                for (int k = list.size() - 1; k >= 0; --k) {
                    final ArrayList<SoundContainer> containers = list.getQuickValue(k);
                    for (int i = 0, size = containers.size(); i < size; ++i) {
                        final SoundContainer source = containers.get(i);
                        source.stop(date);
                        this.tryToUnregisterGroup(source.getGroup());
                    }
                }
                list.clear();
            }
        }
        for (int j = this.m_currentAudioMarkers.size() - 1; j >= 0; --j) {
            final ArrayList<SoundContainer> containers2 = this.m_currentAudioMarkers.getQuickValue(j);
            if (containers2 != null) {
                for (int l = 0, size2 = containers2.size(); l < size2; ++l) {
                    final SoundContainer source2 = containers2.get(l);
                    source2.stop(date);
                    this.tryToUnregisterGroup(source2.getGroup());
                }
                containers2.clear();
            }
        }
        this.m_currentAudioMarkers.clear();
        for (int m = 0, size3 = this.m_currentSources.size(); m < size3; ++m) {
            final SoundContainer source3 = this.m_currentSources.get(m);
            source3.stop(date);
            this.tryToUnregisterGroup(source3.getGroup());
        }
        this.m_currentSources.clear();
    }
    
    public void play() {
        this.play(System.currentTimeMillis());
    }
    
    public void play(final long date) {
        this.evaluateAndPlay(date);
    }
    
    private void playSource(@NotNull final SoundContainer source, final long date) {
        this.playSource(this.m_currentSources, source, date);
    }
    
    private void playSource(@NotNull final ArrayList<SoundContainer> list, @NotNull final SoundContainer source, final long date) {
        list.add(source);
        source.play(date);
        this.tryToRegisterGroup(source.getGroup());
    }
    
    private void stopSource(@NotNull final SoundContainer source, final long date) {
        this.stopSource(this.m_currentSources, source, date);
    }
    
    private void stopSource(@NotNull final ArrayList<SoundContainer> list, @NotNull final SoundContainer source, final long date) {
        list.remove(source);
        source.stop(date);
        this.tryToUnregisterGroup(source.getGroup());
    }
    
    public synchronized void onEvent(final SoundEvent event, final long date) {
        this.m_validEventContainers.clear();
        for (int i = this.m_containers.size() - 1; i >= 0; --i) {
            this.m_containers.get(i).getValidSoundSources(this.m_validEventContainers, event);
        }
        IntObjectLightWeightMap<ArrayList<SoundContainer>> containers = this.m_currentEventContainers.get(event.getTypeId());
        if (containers == null) {
            containers = new IntObjectLightWeightMap<ArrayList<SoundContainer>>();
            this.m_currentEventContainers.put(event.getTypeId(), containers);
        }
        final int eventSignature = event.getSignature();
        ArrayList<SoundContainer> sameContainers = containers.get(eventSignature);
        if (sameContainers == null) {
            sameContainers = new ArrayList<SoundContainer>();
            containers.put(eventSignature, sameContainers);
        }
        for (int j = sameContainers.size() - 1; j >= 0; --j) {
            final SoundContainer container = sameContainers.get(j);
            if (!this.m_validEventContainers.contains(container)) {
                this.stopSource(sameContainers, container, date);
            }
        }
        for (int j = 0, size = this.m_validEventContainers.size(); j < size; ++j) {
            final SoundContainer soundContainer = this.m_validEventContainers.get(j);
            if (event.isLocalized() && soundContainer instanceof FieldSoundContainer) {
                final FieldSoundContainer fsc = (FieldSoundContainer)soundContainer;
                fsc.setSoundSource(event.getSource());
            }
            if (!sameContainers.contains(soundContainer)) {
                this.playSource(sameContainers, soundContainer, date);
            }
        }
    }
    
    public synchronized void onAudioMarkerSpawn(final long id, final ObservedSource source, final AudioMarkerType type, final long date) {
        this.m_validAudioMarkerTypeContainers.clear();
        for (int i = this.m_containers.size() - 1; i >= 0; --i) {
            this.m_containers.get(i).getValidSoundSources(this.m_validAudioMarkerTypeContainers, type);
        }
        if (this.m_validAudioMarkerTypeContainers.size() == 0) {
            return;
        }
        ArrayList<SoundContainer> containers = this.m_currentAudioMarkers.get(id);
        if (containers == null) {
            containers = new ArrayList<SoundContainer>();
            this.m_currentAudioMarkers.put(id, containers);
        }
        for (int j = 0, size = this.m_validAudioMarkerTypeContainers.size(); j < size; ++j) {
            final SoundContainer soundContainer = this.m_validAudioMarkerTypeContainers.get(j).newInstanceWithParameters();
            if (soundContainer instanceof FieldSoundContainer) {
                final FieldSoundContainer fsc = (FieldSoundContainer)soundContainer;
                fsc.setSoundSource(source);
            }
            if (!containers.contains(soundContainer)) {
                this.playSource(containers, soundContainer, date);
            }
        }
    }
    
    public synchronized void onAudioMarkerDespawn(final long id, final long date) {
        final ArrayList<SoundContainer> containers = this.m_currentAudioMarkers.remove(id);
        if (containers == null) {
            return;
        }
        for (int i = 0, size = containers.size(); i < size; ++i) {
            final SoundContainer source = containers.get(i);
            source.stop(date);
            this.tryToUnregisterGroup(source.getGroup());
        }
    }
    
    private void evaluateAndPlay(final long date) {
        this.selectCurrentContainers();
        for (int i = this.m_currentSources.size() - 1; i >= 0; --i) {
            final SoundContainer soundContainer = this.m_currentSources.get(i);
            if (!this.m_validSources.contains(soundContainer)) {
                this.stopSource(soundContainer, date);
            }
        }
        for (int i = 0, size = this.m_validSources.size(); i < size; ++i) {
            final SoundContainer soundContainer2 = this.m_validSources.get(i);
            if (!this.m_currentSources.contains(soundContainer2)) {
                this.playSource(soundContainer2, date);
            }
        }
        this.m_needsUpdate = false;
    }
    
    @Override
    public synchronized void beforeUpdate(final AudioSourceGroup group, final long date) {
        if (this.m_needsUpdate) {
            this.evaluateAndPlay(date);
        }
        for (int i = this.m_currentSources.size() - 1; i >= 0; --i) {
            this.removeFromGroup(group, date, this.m_currentSources, i);
        }
        for (int j = this.m_currentEventContainers.size() - 1; j >= 0; --j) {
            final IntObjectLightWeightMap<ArrayList<SoundContainer>> listBySignature = this.m_currentEventContainers.getQuickValue(j);
            for (int k = listBySignature.size() - 1; k >= 0; --k) {
                final ArrayList<SoundContainer> list = listBySignature.getQuickValue(k);
                for (int l = list.size() - 1; l >= 0; --l) {
                    this.removeFromGroup(group, date, list, l);
                }
            }
        }
        for (int j = this.m_currentAudioMarkers.size() - 1; j >= 0; --j) {
            final ArrayList<SoundContainer> list2 = this.m_currentAudioMarkers.getQuickValue(j);
            for (int m = list2.size() - 1; m >= 0; --m) {
                this.removeFromGroup(group, date, list2, m);
            }
        }
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onUpdate(this);
        }
    }
    
    private void removeFromGroup(final AudioSourceGroup group, final long date, final ArrayList<SoundContainer> list, final int index) {
        final SoundContainer soundContainer = list.get(index);
        if (soundContainer.getGroup() != group) {
            return;
        }
        if (!soundContainer.update(date)) {
            list.remove(soundContainer);
            this.tryToUnregisterGroup(soundContainer.getGroup());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundStrata.class);
    }
}
