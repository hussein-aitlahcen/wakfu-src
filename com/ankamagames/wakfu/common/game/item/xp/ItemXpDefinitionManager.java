package com.ankamagames.wakfu.common.game.item.xp;

import gnu.trove.*;

public class ItemXpDefinitionManager
{
    public static final ItemXpDefinitionManager INSTANCE;
    public static final int CLASSIC_XP_ID = 1;
    public static final int ADMIN_XP_ID = 2;
    private final TIntObjectHashMap<ItemXpDefinition> m_definitions;
    
    public ItemXpDefinitionManager() {
        super();
        (this.m_definitions = new TIntObjectHashMap<ItemXpDefinition>()).put(1, new ItemXpDefinition(1));
        this.m_definitions.put(2, new ItemXpDefinition(2));
    }
    
    public void add(final ItemXpDefinition def) {
        this.m_definitions.put(def.getId(), def);
    }
    
    public ItemXpDefinition get(final int definitionId) {
        return this.m_definitions.get(definitionId);
    }
    
    public boolean contains(final int definitionId) {
        return this.m_definitions.contains(definitionId);
    }
    
    @Override
    public String toString() {
        return "PetDefinitionManager{m_definitions=" + this.m_definitions.size() + '}';
    }
    
    static {
        INSTANCE = new ItemXpDefinitionManager();
    }
}
