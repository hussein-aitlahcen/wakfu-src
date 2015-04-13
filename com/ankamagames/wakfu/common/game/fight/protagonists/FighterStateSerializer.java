package com.ankamagames.wakfu.common.game.fight.protagonists;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class FighterStateSerializer
{
    public static byte[] serialize(final FighterState fighterState) {
        final ByteArray ba = new ByteArray();
        ba.put(fighterState.getPlayState().getId());
        ba.put(fighterState.getTeamId());
        ba.put(fighterState.getOriginalTeamId());
        ba.put((byte)(fighterState.isLocalToFight() ? 1 : 0));
        ba.putLong(fighterState.getOriginalControllerId());
        ba.put((byte)(fighterState.isTeamLeader() ? 1 : 0));
        ba.putLong(fighterState.getCurrentControllerId());
        return ba.toArray();
    }
    
    public static FighterState unserialize(final byte[] data) {
        final ByteBuffer bb = ByteBuffer.wrap(data);
        final byte playStateId = bb.get();
        final byte teamId = bb.get();
        final byte originalTeamId = bb.get();
        final boolean isLocalToFight = bb.get() == 1;
        final long originalControllerId = bb.getLong();
        final boolean isTeamLeader = bb.get() == 1;
        final long currentControllerId = bb.getLong();
        final FighterState res = new FighterState(originalTeamId, isTeamLeader, isLocalToFight, originalControllerId);
        res.forcePlayState(FighterPlayState.fromId(playStateId));
        res.setCurrentControllerId(currentControllerId, teamId);
        return res;
    }
}
