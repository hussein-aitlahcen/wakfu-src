package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public interface TimelineNodes
{
    void remove(long p0);
    
    boolean contains(long p0);
    
    byte currentPosition();
    
    long currentFighter();
    
    boolean hasNextFighter();
    
    void nextFighter();
    
    long peekAtNextFighter();
    
    void sortInitially();
    
    void newTableTurn();
    
    void updateOrder();
    
    TLongArrayList getOrderThisTurn();
    
    TLongArrayList getOrderNextTurn();
    
    void clear();
    
    boolean canStartFighterTurn(long p0);
    
    boolean canEndFighterTurn(long p0);
    
    void endCurrentFighterTurn();
    
    short howLongInTurnsUntil(RelativeFightTime p0);
    
    short getLastTurnPlayed(long p0);
    
    int serializedSize();
    
    void serialize(ByteBuffer p0);
    
    void read(TimelineUnmarshallingContext p0, ByteBuffer p1);
    
    boolean isOutdated(RelativeFightTime p0, short p1);
    
    boolean hasCurrentFighter();
    
    boolean isCurrentFighter(long p0);
    
    @Deprecated
    void setDynamicLists(DynamicLists p0);
    
    void addFromThisTurn(long p0, short p1);
    
    void addFromNextTurn(long p0, short p1);
    
    void addThisTurnOnly(long p0, short p1);
}
