package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import com.ankamagames.wakfu.client.core.game.ia.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.microbotCombination.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class SteamerRailsPathFinder extends TackleAwarePathFinder
{
    private List<MicrobotSet> m_microbotSets;
    private final Point3 m_point3;
    
    public SteamerRailsPathFinder() {
        super();
        this.m_microbotSets = null;
        this.m_point3 = new Point3();
    }
    
    public void initialize(final Fight fight, final byte fighterTeamId, final List<MicrobotSet> microbotSets) {
        this.initialize(fight, fighterTeamId);
        this.m_microbotSets = microbotSets;
        this.setNegativeZoneCost(99.0f);
        this.setEnemyTackleCost(99.0f);
    }
    
    @Override
    public void reset() {
        this.m_microbotSets = null;
    }
    
    @Override
    protected boolean isMovementBlockedFromTopology(final int nextX, final int nextY, final short nextZ, final int currentX, final int currentY, final short currentZ) {
        this.m_point3.set(nextX, nextY, nextZ);
        return !MicrobotSet.isPositionInMicrobotSets(this.m_point3, this.m_microbotSets) || super.isMovementBlockedFromTopology(nextX, nextY, nextZ, currentX, currentY, currentZ);
    }
}
