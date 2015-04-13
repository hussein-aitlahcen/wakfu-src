package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import gnu.trove.*;

public final class FightPlacementManager
{
    public static FightPlacementManager INSTANCE;
    private final TLongObjectHashMap<Point3> m_positionsByFighterId;
    private final TLongObjectHashMap<PlaceSeveralActorsAction> m_actionByFighter;
    
    private FightPlacementManager() {
        super();
        this.m_positionsByFighterId = new TLongObjectHashMap<Point3>();
        this.m_actionByFighter = new TLongObjectHashMap<PlaceSeveralActorsAction>();
    }
    
    public void removePlacement(final long fighterId) {
        this.m_positionsByFighterId.remove(fighterId);
        this.m_actionByFighter.remove(fighterId);
    }
    
    public void setActionForFighter(final long fighterId, final PlaceSeveralActorsAction action) {
        this.m_actionByFighter.put(fighterId, action);
    }
    
    public boolean isFighterCorrespondingAction(final long fighterId, final PlaceSeveralActorsAction action) {
        return this.m_actionByFighter.get(fighterId) == action;
    }
    
    public void updatePositions(final TLongObjectHashMap<Point3> positionsByFighterId) {
        final TLongObjectIterator<Point3> it = positionsByFighterId.iterator();
        while (it.hasNext()) {
            it.advance();
            this.m_positionsByFighterId.put(it.key(), it.value());
        }
    }
    
    public Point3 getPlacement(final long characterId) {
        return this.m_positionsByFighterId.get(characterId);
    }
    
    static {
        FightPlacementManager.INSTANCE = new FightPlacementManager();
    }
}
