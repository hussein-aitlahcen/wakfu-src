package com.ankamagames.wakfu.common.game.dungeon;

import gnu.trove.*;

public class DungeonLadderDefinitionManager
{
    public static final DungeonLadderDefinitionManager INSTANCE;
    private final TShortObjectHashMap<DungeonLadderDefinition> m_definitionsByInstanceId;
    
    private DungeonLadderDefinitionManager() {
        super();
        this.m_definitionsByInstanceId = new TShortObjectHashMap<DungeonLadderDefinition>();
    }
    
    public void add(final DungeonLadderDefinition ladderDefinition) {
        this.m_definitionsByInstanceId.put(ladderDefinition.getInstanceId(), ladderDefinition);
    }
    
    public DungeonLadderDefinition get(final short instanceId) {
        return this.m_definitionsByInstanceId.get(instanceId);
    }
    
    public TShortObjectHashMap<DungeonLadderDefinition> getDefinitionsByInstanceId() {
        return this.m_definitionsByInstanceId;
    }
    
    static {
        INSTANCE = new DungeonLadderDefinitionManager();
    }
}
