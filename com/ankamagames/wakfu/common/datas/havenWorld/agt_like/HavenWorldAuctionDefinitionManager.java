package com.ankamagames.wakfu.common.datas.havenWorld.agt_like;

import com.ankamagames.wakfu.common.game.havenWorld.auction.*;
import gnu.trove.*;

public final class HavenWorldAuctionDefinitionManager
{
    public static final HavenWorldAuctionDefinitionManager INSTANCE;
    private final TIntObjectHashMap<HavenWorldAuctionDefinition> m_definitions;
    
    private HavenWorldAuctionDefinitionManager() {
        super();
        this.m_definitions = new TIntObjectHashMap<HavenWorldAuctionDefinition>();
    }
    
    public void add(final HavenWorldAuctionDefinition definition) {
        if (this.m_definitions.contains(definition.getHavenWorldId())) {
            throw new IllegalArgumentException("definition d\u00e9j\u00e0 ins\u00e9r\u00e9e avec l'id " + definition.getHavenWorldId());
        }
        this.m_definitions.put(definition.getHavenWorldId(), definition);
    }
    
    public HavenWorldAuctionDefinition getDefinition(final int havenWorldId) {
        return this.m_definitions.get(havenWorldId);
    }
    
    public boolean forEachValue(final TObjectProcedure<HavenWorldAuctionDefinition> procedure) {
        return this.m_definitions.forEachValue(procedure);
    }
    
    static {
        INSTANCE = new HavenWorldAuctionDefinitionManager();
    }
}
