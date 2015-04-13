package com.ankamagames.wakfu.common.game.fight.time;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

class TimeEventToken
{
    final DelayableTimeEvent m_storedEvent;
    final RelativeFightTime m_eventDate;
    
    public RelativeFightTime getEventDate() {
        return this.m_eventDate;
    }
    
    public DelayableTimeEvent getStoredEvent() {
        return this.m_storedEvent;
    }
    
    public int serializedSize() {
        return this.m_storedEvent.serializedSize() + 2 + 1;
    }
    
    public void serialize(final ByteBuffer bb) {
        bb.putShort(this.m_eventDate.getTableTurn());
        bb.put((byte)(this.m_eventDate.isAtEndOfTurn() ? 1 : 0));
        this.m_storedEvent.serialize(bb);
    }
    
    public static TimeEventToken deserialize(final TimelineUnmarshallingContext ctx, final long fighterId, final ByteBuffer bb) {
        final short tableTurn = bb.getShort();
        final boolean isAtEndOfTurn = bb.get() == 1;
        return new TimeEventToken(DelayableTimeEvent.deserialize(ctx, bb), RelativeFightTime.forFighter(fighterId).atTableTurn(tableTurn).atEndOfTurn(isAtEndOfTurn));
    }
    
    public static TimeEventToken checkOut(final DelayableTimeEvent event, final RelativeFightTime relativeFightTime) {
        return new TimeEventToken(event, relativeFightTime);
    }
    
    TimeEventToken(final DelayableTimeEvent event, final RelativeFightTime relativeFightTime) {
        super();
        this.m_storedEvent = event;
        this.m_eventDate = relativeFightTime;
    }
}
