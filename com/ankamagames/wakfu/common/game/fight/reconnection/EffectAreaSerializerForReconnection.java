package com.ankamagames.wakfu.common.game.fight.reconnection;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class EffectAreaSerializerForReconnection
{
    private static final int NO_OWNER = 0;
    
    public static byte[] serializeForBaseParameters(final AbstractEffectArea activeEffectArea) {
        final ByteArray ba = new ByteArray();
        ba.putLong(activeEffectArea.getId());
        ba.putInt(activeEffectArea.getWorldCellX());
        ba.putInt(activeEffectArea.getWorldCellY());
        ba.putShort(activeEffectArea.getWorldCellAltitude());
        final EffectUser owner = activeEffectArea.getOwner();
        ba.putLong((owner != null) ? owner.getId() : 0L);
        ba.putShort(activeEffectArea.getLevel());
        ba.putInt(activeEffectArea.getDirection().getIndex());
        return ba.toArray();
    }
    
    public static EffectAreaParameters createBaseEffectAreaParameters(final ByteBuffer bb, final BasicFight fight) {
        final long id = bb.getLong();
        final int x = bb.getInt();
        final int y = bb.getInt();
        final short z = bb.getShort();
        final long ownerId = bb.getLong();
        final EffectUser effectUserFromId = fight.getEffectUserFromId(ownerId);
        final short level = bb.getShort();
        final int direction = bb.getInt();
        return new EffectAreaParameters(id, x, y, z, fight.getContext(), effectUserFromId, level, Direction8.getDirectionFromIndex(direction));
    }
    
    public static byte[] serializeInfoForAfterInit(final AbstractEffectArea abstractEffectArea) {
        return abstractEffectArea.serializeSpecificInfoForReconnection();
    }
    
    public static void unserializeInfoForAfterInit(final AbstractEffectArea abstractEffectArea, final ByteBuffer bb) {
        abstractEffectArea.unserializeSpecificInfoForReconnection(bb);
    }
}
