package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.ai.pathfinder.*;
import java.nio.*;

public class FighterActorMovementRequestMessage extends OutputOnlyProxyMessage
{
    private PathFindResult m_pathResult;
    private long m_fighterId;
    
    @Override
    public byte[] encode() {
        final int numCells = this.m_pathResult.getPathLength();
        final int lCells = numCells * 10;
        final ByteBuffer bb = ByteBuffer.allocate(8 + lCells);
        bb.putLong(this.m_fighterId);
        for (int i = 0; i < numCells; ++i) {
            final int[] step = this.m_pathResult.getPathStep(i);
            bb.putInt(step[0]);
            bb.putInt(step[1]);
            bb.putShort((short)step[2]);
        }
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 4503;
    }
    
    public void setPathResult(final PathFindResult pathResult) {
        this.m_pathResult = pathResult;
    }
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
}
