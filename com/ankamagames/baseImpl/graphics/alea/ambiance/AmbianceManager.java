package com.ankamagames.baseImpl.graphics.alea.ambiance;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public final class AmbianceManager
{
    private static final Logger m_logger;
    public static final AmbianceManager INSTANCE;
    private final TIntObjectHashMap<Ambiance> m_ambiances;
    private final AmbiancePlayer m_player;
    
    private AmbianceManager() {
        super();
        this.m_ambiances = new TIntObjectHashMap<Ambiance>();
        this.m_player = new AmbiancePlayer();
    }
    
    public void init(@NotNull final EffectBlockFactory effectBlockFactory, final AleaWorldScene scene) {
        this.m_player.init(effectBlockFactory, scene);
    }
    
    public void update(final int deltaTime) {
        this.m_player.update(deltaTime);
    }
    
    public boolean playAmbiance(final int id, final Ambiance ambiance) {
        if (ambiance == null) {
            return false;
        }
        this.m_ambiances.put(id, ambiance);
        this.m_player.addAmbiance(ambiance);
        return true;
    }
    
    public boolean stopAmbiance(final int ambianceId) {
        final Ambiance ambiance = this.m_ambiances.get(ambianceId);
        if (ambiance == null) {
            return false;
        }
        this.m_player.removeAmbiance(ambiance);
        return true;
    }
    
    public void clear() {
        this.m_ambiances.clear();
        this.m_player.clear();
    }
    
    public void reset() {
        this.clear();
        this.m_player.reset();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AmbianceManager.class);
        INSTANCE = new AmbianceManager();
    }
}
