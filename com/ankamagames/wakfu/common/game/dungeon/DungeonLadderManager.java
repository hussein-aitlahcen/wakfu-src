package com.ankamagames.wakfu.common.game.dungeon;

import gnu.trove.*;

public class DungeonLadderManager
{
    public static final DungeonLadderManager INSTANCE;
    private final TShortObjectHashMap<AbstractDungeonLadder> m_laddersByInstanceId;
    
    private DungeonLadderManager() {
        super();
        this.m_laddersByInstanceId = new TShortObjectHashMap<AbstractDungeonLadder>();
    }
    
    public void registerLadder(final AbstractDungeonLadder ladder) {
        this.m_laddersByInstanceId.put(ladder.getInstanceId(), ladder);
    }
    
    public AbstractDungeonLadder getLadder(final short instanceId) {
        return this.m_laddersByInstanceId.get(instanceId);
    }
    
    public TShortObjectHashMap<AbstractDungeonLadder> getLaddersByInstanceId() {
        return this.m_laddersByInstanceId;
    }
    
    public void clear() {
        this.m_laddersByInstanceId.forEachValue(new ResetLadder());
        this.m_laddersByInstanceId.clear();
    }
    
    @Override
    public String toString() {
        return "DungeonLadderManager{m_laddersByInstanceId=" + this.m_laddersByInstanceId + '}';
    }
    
    static {
        INSTANCE = new DungeonLadderManager();
    }
    
    private static class ResetLadder implements TObjectProcedure<AbstractDungeonLadder>
    {
        @Override
        public boolean execute(final AbstractDungeonLadder object) {
            object.reset();
            return true;
        }
    }
}
