package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

public class AbsoluteFightTime implements Comparable<AbsoluteFightTime>
{
    private int m_position;
    private short m_tableTurn;
    private boolean m_atEndOfTurn;
    private static AbsoluteFightTime m_never;
    
    public AbsoluteFightTime(final int position, final short tableTurn, final boolean atEndOfTurn) {
        super();
        this.m_atEndOfTurn = atEndOfTurn;
        this.m_position = position;
        this.m_tableTurn = tableTurn;
    }
    
    public static AbsoluteFightTime never() {
        return AbsoluteFightTime.m_never;
    }
    
    @Override
    public int compareTo(final AbsoluteFightTime o) {
        if (this == o) {
            return 0;
        }
        if (o == null) {
            return -1;
        }
        final int dTableTurn = Integer.signum(this.m_tableTurn - o.m_tableTurn);
        if (dTableTurn != 0) {
            return dTableTurn;
        }
        final int dFighterTurn = Integer.signum(this.m_position - o.m_position);
        if (dFighterTurn != 0) {
            return dFighterTurn;
        }
        return (this.m_atEndOfTurn ? 1 : 0) - (o.m_atEndOfTurn ? 1 : 0);
    }
    
    static {
        AbsoluteFightTime.m_never = new AbsoluteFightTime(0, (short)(-1), false);
    }
}
