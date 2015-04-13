package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import java.text.*;

public class RelativeFightTimeInterval
{
    short m_tableTurnsFromNow;
    boolean m_atEndOfTurn;
    short m_priority;
    boolean m_durationIsFullTurns;
    
    public short getPriority() {
        return this.m_priority;
    }
    
    public boolean isDurationFullTurns() {
        return this.m_durationIsFullTurns;
    }
    
    public static RelativeFightTimeInterval currentTime() {
        return new RelativeFightTimeInterval();
    }
    
    public static RelativeFightTimeInterval turnsFromNow(final int turns) {
        final RelativeFightTimeInterval fightTimeInterval = new RelativeFightTimeInterval();
        fightTimeInterval.m_tableTurnsFromNow = (short)turns;
        return fightTimeInterval;
    }
    
    public RelativeFightTimeInterval atEndOfTurn(final boolean atEndOfTurn) {
        this.m_atEndOfTurn = atEndOfTurn;
        return this;
    }
    
    public RelativeFightTimeInterval withPriority(final short priority) {
        this.m_priority = priority;
        return this;
    }
    
    public boolean isAtEndOfTurn() {
        return this.m_atEndOfTurn;
    }
    
    public short getTableTurnsFromNow() {
        return this.m_tableTurnsFromNow;
    }
    
    public RelativeFightTimeInterval inFullTurns(final boolean durationIsFullTurns) {
        this.m_durationIsFullTurns = durationIsFullTurns;
        return this;
    }
    
    public boolean isImmediate() {
        return this.m_tableTurnsFromNow <= 0;
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("{0}T{1}{2}", this.m_tableTurnsFromNow, this.m_atEndOfTurn ? "+" : "-", this.m_durationIsFullTurns ? "!" : "");
    }
    
    public void setTableTurnsFromNow(final int turn) {
        this.m_tableTurnsFromNow = (short)turn;
    }
}
