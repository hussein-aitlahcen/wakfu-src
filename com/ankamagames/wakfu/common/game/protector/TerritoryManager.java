package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import gnu.trove.*;

public final class TerritoryManager
{
    public static final TerritoryManager INSTANCE;
    protected static final Logger m_logger;
    private final TIntObjectHashMap<AbstractTerritory> m_territories;
    private TerritoryFactory m_territoryFactory;
    
    private TerritoryManager() {
        super();
        this.m_territories = new TIntObjectHashMap<AbstractTerritory>();
    }
    
    public TerritoryFactory getTerritoryFactory() {
        return this.m_territoryFactory;
    }
    
    public void setTerritoryFactory(final TerritoryFactory territoryFactory) {
        this.m_territoryFactory = territoryFactory;
    }
    
    public AbstractTerritory createTerritory(final int id) {
        if (this.m_territoryFactory != null) {
            return this.m_territoryFactory.createTerritory(id);
        }
        return null;
    }
    
    public boolean registerTerritory(final AbstractTerritory territory) {
        if (territory == null) {
            return false;
        }
        final int id = territory.getId();
        final AbstractTerritory t = this.m_territories.get(id);
        if (t != null && t != territory) {
            TerritoryManager.m_logger.error((Object)"Tentative d'\u00e9crase de territoire.");
            return false;
        }
        this.m_territories.put(id, territory);
        return true;
    }
    
    public AbstractTerritory getTerritory(final int id) {
        return this.m_territories.get(id);
    }
    
    public TIntObjectHashMap<AbstractTerritory> getTerritories() {
        return this.m_territories;
    }
    
    public int[] getTerritoriesId() {
        return this.m_territories.keys();
    }
    
    public void clear() {
        this.m_territories.clear();
    }
    
    static {
        INSTANCE = new TerritoryManager();
        m_logger = Logger.getLogger((Class)TerritoryManager.class);
    }
}
