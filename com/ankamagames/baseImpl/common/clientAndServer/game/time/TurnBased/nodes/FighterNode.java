package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes;

import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public class FighterNode
{
    long m_fighterId;
    short m_tableTurn;
    
    FighterNode() {
        super();
    }
    
    long getFighterId() {
        return this.m_fighterId;
    }
    
    void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
    
    public void endTurn() {
        ++this.m_tableTurn;
    }
    
    public short getNextTurnToPlay() {
        return this.m_tableTurn;
    }
    
    public int serializedSize() {
        return 2;
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putShort(this.m_tableTurn);
    }
    
    protected void read(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        this.m_tableTurn = buffer.getShort();
    }
    
    public static FighterNode deserialize(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        final FighterNode result = new FighterNode();
        result.read(ctx, buffer);
        return result;
    }
    
    public FighterNode(final long fighterId, final short tableTurn) {
        this();
        this.m_tableTurn = tableTurn;
        this.m_fighterId = fighterId;
    }
}
