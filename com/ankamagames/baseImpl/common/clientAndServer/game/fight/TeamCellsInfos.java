package com.ankamagames.baseImpl.common.clientAndServer.game.fight;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

final class TeamCellsInfos
{
    private final ByteObjectLightWeightMap<TLongArrayList> m_teamCells;
    
    TeamCellsInfos() {
        super();
        this.m_teamCells = new ByteObjectLightWeightMap<TLongArrayList>(2);
    }
    
    TLongArrayList getTeamCells(final byte teamId) {
        TLongArrayList res = this.m_teamCells.get(teamId);
        if (res == null) {
            res = new TLongArrayList();
            this.m_teamCells.put(teamId, res);
        }
        return res;
    }
    
    void putTeamCell(final byte teamId, final int x, final int y) {
        final TLongArrayList teamCells = this.getTeamCells(teamId);
        teamCells.add(MathHelper.getLongFromTwoInt(x, y));
    }
}
