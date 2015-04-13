package com.ankamagames.wakfu.client.core.havenWorld;

import gnu.trove.*;

public class HavenWorldBuildingVisualDefinitionManager
{
    public static final HavenWorldBuildingVisualDefinitionManager INSTANCE;
    private final TIntObjectHashMap<HavenWorldBuildingVisualDefinition> m_definitions;
    
    private HavenWorldBuildingVisualDefinitionManager() {
        super();
        this.m_definitions = new TIntObjectHashMap<HavenWorldBuildingVisualDefinition>();
    }
    
    public void register(final HavenWorldBuildingVisualDefinition definition) {
        this.m_definitions.put(definition.getBuildingId(), definition);
    }
    
    public HavenWorldBuildingVisualDefinition get(final int buildingReferenceId) {
        return this.m_definitions.get(buildingReferenceId);
    }
    
    static {
        INSTANCE = new HavenWorldBuildingVisualDefinitionManager();
    }
}
