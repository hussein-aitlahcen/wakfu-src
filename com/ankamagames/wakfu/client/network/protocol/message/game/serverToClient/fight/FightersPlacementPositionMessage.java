package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public final class FightersPlacementPositionMessage extends AbstractFightMessage
{
    TLongObjectHashMap<Point3> m_positionsByFighterId;
    private boolean m_shouldTeleport;
    
    public FightersPlacementPositionMessage() {
        super();
        this.m_positionsByFighterId = new TLongObjectHashMap<Point3>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        final short size = bb.getShort();
        for (int i = 0; i < size; ++i) {
            final long fighter = bb.getLong();
            final Point3 pos = new Point3(bb.getInt(), bb.getInt(), bb.getShort());
            this.m_positionsByFighterId.put(fighter, pos);
        }
        this.m_shouldTeleport = (bb.get() == 1);
        return false;
    }
    
    public boolean isShouldTeleport() {
        return this.m_shouldTeleport;
    }
    
    public TLongObjectHashMap<Point3> getPositionsByFighterId() {
        return this.m_positionsByFighterId;
    }
    
    @Override
    public int getId() {
        return 7906;
    }
}
