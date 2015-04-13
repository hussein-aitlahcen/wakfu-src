package com.ankamagames.wakfu.client.sound.ambiance2D;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.exporter.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.container.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.framework.sound.group.*;
import gnu.trove.*;

public class SoundContainerManager
{
    private static final Logger m_logger;
    private static final long EVALUATION_INTERVAL = 5000L;
    private final TIntObjectHashMap<AbstractSoundContainer> m_containers;
    private final ArrayList<SoundStrata> m_thisIsSTRATAAAAAAA;
    private long m_updateInterval;
    private long m_nextUpdate;
    private final Object m_updateMutex;
    private final ArrayList<SoundContainerManagerListener> m_listeners;
    private SoundContainerUpdateThread m_updateThread;
    
    public SoundContainerManager() {
        super();
        this.m_containers = new TIntObjectHashMap<AbstractSoundContainer>();
        this.m_thisIsSTRATAAAAAAA = new ArrayList<SoundStrata>();
        this.m_updateInterval = 5000L;
        this.m_updateMutex = new Object();
        this.m_listeners = new ArrayList<SoundContainerManagerListener>();
        (this.m_updateThread = new SoundContainerUpdateThread()).setName("SoundAmbEngine");
        this.m_updateThread.start();
    }
    
    public ArrayList<SoundStrata> getSoundStrata() {
        return this.m_thisIsSTRATAAAAAAA;
    }
    
    public long getUpdateInterval() {
        return this.m_updateInterval;
    }
    
    public void setUpdateInterval(final long updateInterval) {
        this.m_updateInterval = updateInterval;
        for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
            this.m_thisIsSTRATAAAAAAA.get(i).setUpdateInterval(this.m_updateInterval);
        }
    }
    
    public void addListener(final SoundContainerManagerListener l) {
        this.m_listeners.add(l);
    }
    
    public void removeListener(final SoundContainerManagerListener l) {
        this.m_listeners.remove(l);
    }
    
    public void load(final String path) {
        try {
            final ExtendedDataInputStream is = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path));
            final SoundContainerExporter sce = new SoundContainerExporter();
            sce.load(is);
            this.load(sce);
        }
        catch (IOException e) {
            SoundContainerManager.m_logger.error((Object)("Probl\u00e8me \u00e0 la lecture du fichier " + path));
        }
    }
    
    public void load(final SoundContainerExporter sce) {
        this.stop();
        this.m_thisIsSTRATAAAAAAA.clear();
        this.m_containers.clear();
        final ArrayList<RawSoundStrata> stratas = sce.getSoundStratas();
        for (int i = 0, size = stratas.size(); i < size; ++i) {
            this.m_thisIsSTRATAAAAAAA.add(this.convertRawSoundStrata(stratas.get(i)));
        }
        final ArrayList<RawSoundContainer> containers = sce.getSoundContainers();
        final TIntObjectHashMap<RawSoundContainer> rscMap = new TIntObjectHashMap<RawSoundContainer>();
        for (int j = 0, size2 = containers.size(); j < size2; ++j) {
            final RawSoundContainer rsc = containers.get(j);
            final AbstractSoundContainer sc = this.convertRawContainer(rsc);
            if (sc != null) {
                this.m_containers.put(sc.getId(), sc);
                rscMap.put(sc.getId(), rsc);
            }
        }
        final TIntObjectIterator<AbstractSoundContainer> it = (TIntObjectIterator<AbstractSoundContainer>)this.m_containers.iterator();
        while (it.hasNext()) {
            it.advance();
            final int id = it.key();
            final AbstractSoundContainer soundContainer = it.value();
            final RawSoundContainer rsc2 = rscMap.get(id);
            if (soundContainer instanceof ParentSoundContainer) {
                final ParentSoundContainer psc = (ParentSoundContainer)soundContainer;
                for (int k = 0, size3 = rsc2.m_soundSources.size(); k < size3; ++k) {
                    final AbstractSoundContainer toAdd = this.m_containers.get((int)rsc2.m_soundSources.get(k));
                    ((AbstractSoundContainer<AbstractSoundContainer>)psc).addSoundSource(toAdd);
                }
                if (rsc2.m_strataId == -1) {
                    continue;
                }
                for (int k = 0, size3 = this.m_thisIsSTRATAAAAAAA.size(); k < size3; ++k) {
                    final SoundStrata strata = this.m_thisIsSTRATAAAAAAA.get(k);
                    if (strata.getId() == rsc2.m_strataId) {
                        strata.addContainer(psc);
                        break;
                    }
                }
            }
            else {
                final SoundContainer sc2 = (SoundContainer)soundContainer;
                for (int k = 0, size3 = rsc2.m_soundSources.size(); k < size3; ++k) {
                    sc2.addSource(rsc2.m_soundSources.get(k));
                }
            }
        }
        this.m_containers.clear();
    }
    
    private SoundStrata convertRawSoundStrata(final RawSoundStrata rss) {
        final SoundStrata soundStrata = new SoundStrata();
        soundStrata.setId(rss.m_id);
        soundStrata.setName(rss.m_name);
        soundStrata.setUpdateInterval(this.m_updateInterval);
        return soundStrata;
    }
    
    private AbstractSoundContainer convertRawContainer(final RawSoundContainer rsc) {
        final AbstractSoundContainer container = this.createContainer(rsc);
        if (container == null) {
            return null;
        }
        container.setId(rsc.m_id);
        container.setName(rsc.m_name);
        final ArrayList<ContainerCriterion> criteria = rsc.m_criteria;
        for (int i = 0, size = criteria.size(); i < size; ++i) {
            container.addCriterion(criteria.get(i));
        }
        container.setGain((rsc.m_gain >= 0) ? (rsc.m_gain / 100.0f) : -1.0f);
        container.setMaxGain((rsc.m_maxGain >= 0) ? (rsc.m_maxGain / 100.0f) : -1.0f);
        return container;
    }
    
    private AbstractSoundContainer createContainer(final RawSoundContainer rsc) {
        if (rsc.m_audioMarkerContainer) {
            final AudioMarkerContainer c = new AudioMarkerContainer();
            c.setType(AudioMarkerType.getFromId(rsc.m_audioMarkerType));
            return c;
        }
        if (rsc.m_eventContainer) {
            final EventSoundContainer c2 = new EventSoundContainer();
            c2.setEvent(EventType.fromId(rsc.m_eventType));
            c2.setEventCriterion(rsc.m_eventCriterion);
            c2.setEventIsLocalized(rsc.m_eventIsLocalized);
            return c2;
        }
        if (rsc.m_parentContainer) {
            return new ParentSoundContainer();
        }
        SoundContainer container = null;
        final GameSoundGroup group = GameSoundGroup.fromId(rsc.m_busId);
        if (rsc.m_busType == 0 && group.getDefaultGroup() != null) {
            container = new DefaultSoundContainer();
            container.setGroup(group.getDefaultGroup());
        }
        else if (rsc.m_busType == 1 && group.getFieldSourceGroup() != null) {
            final FieldSoundContainer fsc = new FieldSoundContainer();
            fsc.setGroup(group.getFieldSourceGroup());
            fsc.setPseudoLocalized(true);
            fsc.setRollOff(rsc.m_rollOffId);
            container = fsc;
        }
        else if (rsc.m_busType == 2 && group.getFieldSourceGroup() != null) {
            final FieldSoundContainer fsc = new FieldSoundContainer();
            fsc.setGroup(group.getFieldSourceGroup());
            fsc.setRollOff(rsc.m_rollOffId);
            container = fsc;
        }
        if (container == null) {
            return null;
        }
        container.setPlayType(rsc.m_playType);
        container.setTransitionType(rsc.m_transition);
        container.setTransitionInMinDuration(rsc.m_transitionInMinDuration);
        container.setTransitionInMaxDuration(rsc.m_transitionInMaxDuration);
        container.setTransitionOutMinDuration(rsc.m_transitionOutMinDuration);
        container.setTransitionOutMaxDuration(rsc.m_transitionOutMaxDuration);
        container.setLoopMode(rsc.m_loopMode);
        container.setLoopDuration(rsc.m_loopDuration);
        container.setInitialDelay(rsc.m_initialDelay);
        return container;
    }
    
    public void play() {
        synchronized (this.m_updateMutex) {
            this.m_nextUpdate = System.currentTimeMillis();
        }
        this.m_updateThread.setRunning(true);
    }
    
    public void stop() {
        final long date = System.currentTimeMillis();
        this.m_updateThread.setRunning(false);
        for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
            final SoundStrata strata = this.m_thisIsSTRATAAAAAAA.get(i);
            strata.stop(date);
        }
    }
    
    public void onEvent(final SoundEvent event) {
        final long date = System.currentTimeMillis();
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onEvent(event);
        }
        for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
            final SoundStrata strata = this.m_thisIsSTRATAAAAAAA.get(i);
            strata.onEvent(event, date);
        }
    }
    
    public void onAudioMarkerSpawn(final long id, final ObservedSource source, final AudioMarkerType type) {
        final long date = System.currentTimeMillis();
        for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
            final SoundStrata strata = this.m_thisIsSTRATAAAAAAA.get(i);
            strata.onAudioMarkerSpawn(id, source, type, date);
        }
    }
    
    public void onAudioMarkerDespawn(final long id) {
        final long date = System.currentTimeMillis();
        for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
            final SoundStrata strata = this.m_thisIsSTRATAAAAAAA.get(i);
            strata.onAudioMarkerDespawn(id, date);
        }
    }
    
    private void update(final long date) {
        synchronized (this.m_updateMutex) {
            if (date < this.m_nextUpdate) {
                return;
            }
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                this.m_listeners.get(i).onUpdate(date);
            }
            for (int i = this.m_thisIsSTRATAAAAAAA.size() - 1; i >= 0; --i) {
                final SoundStrata soundStrata = this.m_thisIsSTRATAAAAAAA.get(i);
                soundStrata.needsUpdate(this.m_nextUpdate);
            }
            this.m_nextUpdate += this.m_updateInterval;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundContainerManager.class);
    }
    
    private class SoundContainerUpdateThread extends Thread
    {
        private boolean m_running;
        
        private SoundContainerUpdateThread() {
            super();
            this.m_running = false;
        }
        
        public void setRunning(final boolean running) {
            this.m_running = running;
        }
        
        @Override
        public void run() {
            while (true) {
                if (this.m_running) {
                    try {
                        SoundContainerManager.this.update(System.currentTimeMillis());
                    }
                    catch (Throwable t) {
                        SoundContainerManager.m_logger.warn((Object)"Exception dans le Thread de moteur d'ambiance.", t);
                    }
                }
                try {
                    Thread.sleep(10L);
                }
                catch (InterruptedException e) {}
            }
        }
    }
}
