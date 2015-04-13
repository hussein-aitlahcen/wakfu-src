package com.ankamagames.wakfu.common.game.fight.time;

import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import java.util.*;

class FighterToken implements Iterable<TimeEventToken>
{
    private final short m_lastTurnPlayed;
    private final List<TimeEventToken> m_timeEventTokens;
    
    public short getLastTurnPlayed() {
        return this.m_lastTurnPlayed;
    }
    
    public boolean willPlayIfUnshelvedAtTurn(final short turn) {
        return this.m_lastTurnPlayed < turn;
    }
    
    public void addTimeEventToken(final TimeEventToken timeEventToken) {
        this.m_timeEventTokens.add(timeEventToken);
    }
    
    @Override
    public Iterator<TimeEventToken> iterator() {
        if (this.m_timeEventTokens == null) {
            return Collections.emptyList().iterator();
        }
        return this.m_timeEventTokens.iterator();
    }
    
    public int serializedSize() {
        int size = 4;
        for (final TimeEventToken timeEventToken : this.m_timeEventTokens) {
            size += timeEventToken.serializedSize();
        }
        return size;
    }
    
    public void serialize(final ByteBuffer bb) {
        bb.putShort(this.m_lastTurnPlayed);
        bb.putShort((short)this.m_timeEventTokens.size());
        for (final TimeEventToken timeEventToken : this.m_timeEventTokens) {
            timeEventToken.serialize(bb);
        }
    }
    
    public static FighterToken deserialize(final TimelineUnmarshallingContext ctx, final long fighterId, final ByteBuffer bb) {
        final short lastTurnPlayed = bb.getShort();
        final FighterToken result = new FighterToken(lastTurnPlayed);
        final short numTokens = bb.getShort();
        for (int i = 0; i < numTokens; ++i) {
            final TimeEventToken token = TimeEventToken.deserialize(ctx, fighterId, bb);
            result.addTimeEventToken(token);
        }
        return result;
    }
    
    FighterToken(final short lastTurnPlayed) {
        super();
        this.m_timeEventTokens = new ArrayList<TimeEventToken>();
        this.m_lastTurnPlayed = lastTurnPlayed;
    }
}
