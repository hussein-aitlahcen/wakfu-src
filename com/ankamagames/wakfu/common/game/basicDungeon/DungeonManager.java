package com.ankamagames.wakfu.common.game.basicDungeon;

import gnu.trove.*;

public class DungeonManager
{
    public static final DungeonManager INSTANCE;
    private final TIntObjectHashMap<DungeonDefinition> m_dungeons;
    private final TIntIntHashMap m_tpToDungeon;
    
    public DungeonManager() {
        super();
        this.m_dungeons = new TIntObjectHashMap<DungeonDefinition>();
        this.m_tpToDungeon = new TIntIntHashMap();
    }
    
    public void addDungeon(final DungeonDefinition def) {
        this.m_dungeons.put(def.getId(), def);
        def.forEachDungeon(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                DungeonManager.this.m_tpToDungeon.put(value, def.getId());
                return true;
            }
        });
    }
    
    public DungeonDefinition getDungeon(final int id) {
        return this.m_dungeons.get(id);
    }
    
    public void forEachValue(final TObjectProcedure<DungeonDefinition> procedure) {
        this.m_dungeons.forEachValue(procedure);
    }
    
    public DungeonDefinition getDungeonByTpId(final int tp) {
        return this.getDungeon(this.m_tpToDungeon.get(tp));
    }
    
    static {
        INSTANCE = new DungeonManager();
    }
}
