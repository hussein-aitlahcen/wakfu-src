package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.framework.ai.pathfinder.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;
import org.jetbrains.annotations.*;

public class FighterMoveMessage extends AbstractFightActionMessage
{
    private long m_fighterId;
    private byte m_directionAtEnd;
    private PathFindResult m_pathFindResult;
    private byte m_movementTypeId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 20, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_fighterId = bb.getLong();
        this.m_directionAtEnd = bb.get();
        this.m_movementTypeId = bb.get();
        final int pathLength = rawDatas.length - 22;
        final byte[] encodedPath = new byte[pathLength];
        bb.get(encodedPath);
        this.m_pathFindResult = new PathFindResult(encodedPath);
        return true;
    }
    
    @Override
    public int getId() {
        return 4524;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public byte getDirectionAtEnd() {
        return this.m_directionAtEnd;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.MOVE;
    }
    
    public PathFindResult getPathResult() {
        return this.m_pathFindResult;
    }
    
    @Nullable
    public FightMovementType getMovementType() {
        return FightMovementType.fromOrdinal(this.m_movementTypeId);
    }
}
