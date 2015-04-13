package com.ankamagames.framework.graphics.engine.particleSystem;

import java.util.*;

public abstract class Affectorable
{
    private ArrayList<Affector> m_affectors;
    private ArrayList<Affector> m_keyFramedAffectors;
    
    public final boolean hasKeyFramedAffector() {
        return this.m_keyFramedAffectors != null;
    }
    
    public final int getKeyFramedAffectorsSize() {
        return this.m_keyFramedAffectors.size();
    }
    
    public final Affector getKeyFramedAffectors(final int index) {
        return this.m_keyFramedAffectors.get(index);
    }
    
    public final boolean hasAffector() {
        return this.m_affectors != null;
    }
    
    public final int getAffectorsSize() {
        return this.m_affectors.size();
    }
    
    public final Affector getAffectors(final int index) {
        return this.m_affectors.get(index);
    }
    
    public final void addAffector(final Affector affector) {
        if (this.m_affectors == null) {
            this.m_affectors = new ArrayList<Affector>(1);
        }
        this.m_affectors.add(affector);
    }
    
    public final void removeAffector(final Affector affector) {
        if (this.m_affectors == null) {
            return;
        }
        this.m_affectors.remove(affector);
    }
    
    public final void addKeyFramedAffector(final Affector affector) {
        if (this.m_keyFramedAffectors == null) {
            this.m_keyFramedAffectors = new ArrayList<Affector>(1);
        }
        this.m_keyFramedAffectors.add(affector);
    }
    
    public final void removeKeyFramedAffector(final Affector affector) {
        if (this.m_keyFramedAffectors == null) {
            return;
        }
        this.m_keyFramedAffectors.remove(affector);
    }
    
    protected final void resetAffectors() {
        if (this.m_affectors != null) {
            this.m_affectors.clear();
            this.m_affectors = null;
        }
        if (this.m_keyFramedAffectors != null) {
            this.m_keyFramedAffectors.clear();
            this.m_keyFramedAffectors = null;
        }
    }
}
