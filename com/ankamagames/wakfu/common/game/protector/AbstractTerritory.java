package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;

public class AbstractTerritory<TCH extends TerritoryChaosHandler>
{
    protected static Logger m_logger;
    private int m_id;
    private ProtectorBase m_protector;
    private final ArrayList<TerritoryEventListener> m_territoryEventListeners;
    private TCH m_chaosHandler;
    
    protected AbstractTerritory(final int id) {
        super();
        this.m_id = id;
        this.m_territoryEventListeners = new ArrayList<TerritoryEventListener>();
    }
    
    protected void setId(final int id) {
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public TCH getChaosHandler() {
        return this.m_chaosHandler;
    }
    
    public void setChaosHandler(final TCH chaosHandler) {
        this.m_chaosHandler = chaosHandler;
    }
    
    @Nullable
    public ProtectorBase getProtector() {
        return this.m_protector;
    }
    
    public void setProtector(final ProtectorBase protector) {
        this.m_protector = protector;
    }
    
    public void addTerritoryEventListener(final TerritoryEventListener listener) {
        if (!this.m_territoryEventListeners.contains(listener)) {
            this.m_territoryEventListeners.add(listener);
        }
        else {
            AbstractTerritory.m_logger.warn((Object)("on essaye d'ajouter 2 fois le listener=" + listener + " sur le territoire d'id=" + this.m_id));
        }
    }
    
    public void removeTerritoryEventListener(final TerritoryEventListener listener) {
        this.m_territoryEventListeners.remove(listener);
    }
    
    public void firePlayerEnterTerritory(final BasicCharacterInfo characterInfo) {
        for (int i = 0, size = this.m_territoryEventListeners.size(); i < size; ++i) {
            try {
                this.m_territoryEventListeners.get(i).onPlayerEnterTerritory(characterInfo, this);
            }
            catch (Exception e) {
                AbstractTerritory.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void firePlayerLeaveTerritory(final BasicCharacterInfo characterInfo) {
        for (int i = 0, size = this.m_territoryEventListeners.size(); i < size; ++i) {
            try {
                this.m_territoryEventListeners.get(i).onPlayerLeaveTerritory(characterInfo, this);
            }
            catch (Exception e) {
                AbstractTerritory.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public String toString() {
        return "AbstractTerritory {" + "id=" + this.m_id + ", protector=" + (Object)((this.m_protector != null) ? this.m_protector.getId() : null) + '}';
    }
    
    static {
        AbstractTerritory.m_logger = Logger.getLogger((Class)AbstractTerritory.class);
    }
}
