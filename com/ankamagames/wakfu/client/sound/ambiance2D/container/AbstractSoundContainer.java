package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.exporter.*;
import java.util.*;

public abstract class AbstractSoundContainer<SS extends SoundSource> implements SoundSource
{
    private int m_id;
    private String m_name;
    private ArrayList<ContainerCriterion> m_criteria;
    protected ArrayList<SS> m_sources;
    protected AbstractSoundContainer m_parent;
    private float m_gain;
    private float m_maxGain;
    
    public AbstractSoundContainer() {
        super();
        this.m_criteria = new ArrayList<ContainerCriterion>();
        this.m_sources = new ArrayList<SS>();
        this.m_gain = -1.0f;
        this.m_maxGain = -1.0f;
    }
    
    @Override
    public boolean isValid() {
        for (int i = this.m_criteria.size() - 1; i >= 0; --i) {
            if (!this.m_criteria.get(i).isValid()) {
                return false;
            }
        }
        return true;
    }
    
    public void addCriterion(final ContainerCriterion criterion) {
        this.m_criteria.add(criterion);
    }
    
    public void addSoundSource(final SS source) {
        this.m_sources.add(source);
        source.setParent(this);
    }
    
    public ArrayList<SS> getSoundSources() {
        return this.m_sources;
    }
    
    @Override
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
    
    @Override
    public AbstractSoundContainer getParent() {
        return this.m_parent;
    }
    
    @Override
    public void setParent(final AbstractSoundContainer parent) {
        this.m_parent = parent;
    }
    
    public RawSoundContainer toRawSoundContainer() {
        final RawSoundContainer rsc = new RawSoundContainer();
        this.fillRawSoundContainer(rsc);
        return rsc;
    }
    
    protected void fillRawSoundContainer(final RawSoundContainer rsc) {
        rsc.m_id = this.m_id;
        rsc.m_criteria.addAll(this.m_criteria);
        rsc.m_name = this.m_name;
    }
    
    protected void copy(final AbstractSoundContainer container) {
        container.m_id = this.m_id;
        container.m_name = this.m_name;
        container.m_parent = this.m_parent;
        container.m_gain = this.m_gain;
        container.m_maxGain = this.m_maxGain;
    }
    
    public float getGain() {
        if (this.m_gain != -1.0f) {
            return this.m_gain;
        }
        if (this.m_parent != null) {
            return this.m_parent.getGain();
        }
        return 1.0f;
    }
    
    public void setGain(final float gain) {
        this.m_gain = gain;
    }
    
    public float getMaxGain() {
        if (this.m_maxGain != -1.0f) {
            return this.m_maxGain;
        }
        if (this.m_parent != null) {
            return this.m_parent.getMaxGain();
        }
        return -1.0f;
    }
    
    public void setMaxGain(final float maxGain) {
        this.m_maxGain = maxGain;
    }
    
    protected boolean isAudioMarker() {
        return this.m_parent != null && this.m_parent.isAudioMarker();
    }
}
