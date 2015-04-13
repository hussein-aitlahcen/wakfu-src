package com.ankamagames.baseImpl.graphics.alea.ambiance;

import org.apache.log4j.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;

class AmbiancePlayer
{
    protected static final Logger m_logger;
    private final EffectMixer m_mixer;
    private final ArrayList<AmbianceContext> m_ambiances;
    private final TIntArrayList _ambiancesIndexToRemove;
    private final ArrayList<EffectContext> m_activeEffectsContexts;
    
    AmbiancePlayer() {
        super();
        this.m_mixer = new EffectMixer();
        this.m_ambiances = new ArrayList<AmbianceContext>();
        this._ambiancesIndexToRemove = new TIntArrayList();
        this.m_activeEffectsContexts = new ArrayList<EffectContext>();
    }
    
    void update(final int deltaTime) {
        this.m_activeEffectsContexts.clear();
        this._ambiancesIndexToRemove.resetQuick();
        for (int i = 0, size = this.m_ambiances.size(); i < size; ++i) {
            final AmbianceContext ambiance = this.m_ambiances.get(i);
            ambiance.update(deltaTime);
            ambiance.getEffectContexts(this.m_activeEffectsContexts);
            if (ambiance.getStrength() == 0.0f) {
                this._ambiancesIndexToRemove.add(i);
            }
        }
        for (int i = this._ambiancesIndexToRemove.size() - 1; i >= 0; --i) {
            this.m_ambiances.remove(this._ambiancesIndexToRemove.getQuick(i));
        }
        final ArrayList<EffectApplyer> blocks = this.m_mixer.process(this.m_activeEffectsContexts);
        for (int j = 0, size2 = blocks.size(); j < size2; ++j) {
            blocks.get(j).apply();
        }
    }
    
    void addAmbiance(final Ambiance ambiance) {
        AmbianceContext context = this.getAmbience(ambiance.getId());
        if (context == null) {
            context = new AmbianceContext(ambiance);
            this.m_ambiances.add(context);
        }
        context.fadeTo(1.0f);
    }
    
    void removeAmbiance(final Ambiance ambiance) {
        final AmbianceContext context = this.getAmbience(ambiance.getId());
        if (context != null) {
            context.fadeTo(0.0f);
        }
    }
    
    private AmbianceContext getAmbience(final int id) {
        for (int i = this.m_ambiances.size() - 1; i >= 0; --i) {
            final AmbianceContext context = this.m_ambiances.get(i);
            if (context.m_ambiance.getId() == id) {
                return context;
            }
        }
        return null;
    }
    
    void init(final EffectBlockFactory effectBlockFactory, final AleaWorldScene scene) {
        this.m_mixer.init(effectBlockFactory, scene);
    }
    
    void clear() {
        this.m_activeEffectsContexts.clear();
        this.m_ambiances.clear();
        this.m_mixer.clear();
    }
    
    public void reset() {
        this.m_mixer.reset();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AmbiancePlayer.class);
    }
    
    private static class AmbianceContext
    {
        private final Interpolator m_strength;
        private final Ambiance m_ambiance;
        
        private AmbianceContext(final Ambiance ambiance) {
            super();
            this.m_strength = new Interpolator();
            this.m_ambiance = ambiance;
            this.m_strength.setSpeed(1.0f);
            this.m_strength.setDelta(0.001f);
            this.m_strength.set(0.0f);
        }
        
        public void fadeTo(final float value) {
            this.m_strength.setEnd(value);
        }
        
        public void update(final int deltaTime) {
            this.m_strength.process(deltaTime);
            this.m_ambiance.update(deltaTime);
        }
        
        public void getEffectContexts(final ArrayList<EffectContext> contexts) {
            final float strength = this.getStrength();
            final ArrayList<EffectContext> effects = this.m_ambiance.getEffects();
            for (int size = effects.size(), i = 0; i < size; ++i) {
                final EffectContext context = effects.get(i);
                context.setStrength(strength);
                contexts.add(context);
            }
        }
        
        public final float getStrength() {
            return this.m_strength.getValue();
        }
    }
}
