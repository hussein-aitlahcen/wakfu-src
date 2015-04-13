package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import java.nio.*;
import java.text.*;

public class RelativeFightTime implements Comparable<RelativeFightTime>
{
    private long m_fighterId;
    private int m_position;
    private short m_tableTurn;
    private boolean m_atEndOfTurn;
    private static final RelativeFightTime m_never;
    
    public RelativeFightTime() {
        super();
    }
    
    public RelativeFightTime(final long fighterId, final int position, final short tableTurn, final boolean atEndOfTurn) {
        super();
        this.m_atEndOfTurn = atEndOfTurn;
        this.m_fighterId = fighterId;
        this.m_position = position;
        this.m_tableTurn = tableTurn;
    }
    
    public RelativeFightTime(final RelativeFightTime absoluteTime) {
        this(absoluteTime.m_fighterId, absoluteTime.m_position, absoluteTime.m_tableTurn, absoluteTime.m_atEndOfTurn);
    }
    
    public static RelativeFightTime never() {
        return RelativeFightTime.m_never;
    }
    
    public boolean isInfinite() {
        return this.m_tableTurn < 0;
    }
    
    public long getFighterId() {
        return this.m_fighterId;
    }
    
    public boolean isAtEndOfTurn() {
        return this.m_atEndOfTurn;
    }
    
    public short getTableTurn() {
        return this.m_tableTurn;
    }
    
    public void setPosition(final int position) {
        this.m_position = position;
    }
    
    public int getPosition() {
        return this.m_position;
    }
    
    public static RelativeFightTime forFighter(final long fighterId) {
        final RelativeFightTime relativeFightTime = new RelativeFightTime();
        relativeFightTime.m_fighterId = fighterId;
        return relativeFightTime;
    }
    
    public RelativeFightTime atTableTurn(final int tableTurn) {
        this.m_tableTurn = (short)tableTurn;
        return this;
    }
    
    public RelativeFightTime atEndOfTurn(final boolean atEndOfTurn) {
        this.m_atEndOfTurn = atEndOfTurn;
        return this;
    }
    
    public static int serializedSize() {
        return 11;
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_fighterId);
        buffer.putShort(this.m_tableTurn);
        buffer.put((byte)(this.m_atEndOfTurn ? 1 : 0));
    }
    
    public static RelativeFightTime deserialize(final ByteBuffer buffer) {
        final RelativeFightTime result = new RelativeFightTime();
        result.read(buffer);
        return result;
    }
    
    void read(final ByteBuffer buffer) {
        this.m_fighterId = buffer.getLong();
        this.m_tableTurn = buffer.getShort();
        this.m_atEndOfTurn = (buffer.get() == 1);
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("@T{0}{2}({1})", this.m_tableTurn, this.m_fighterId, this.m_atEndOfTurn ? "+" : "-");
    }
    
    @Override
    public int compareTo(final RelativeFightTime other) {
        if (this == other) {
            return 0;
        }
        if (this.isNever()) {
            return -1;
        }
        if (other == null || other.isNever()) {
            return 1;
        }
        if (other.m_fighterId != this.m_fighterId) {
            return 0;
        }
        final int dTurn = Integer.signum(this.m_tableTurn - other.m_tableTurn);
        if (dTurn != 0) {
            return dTurn;
        }
        return (this.m_atEndOfTurn ? 1 : 0) - (other.m_atEndOfTurn ? 1 : 0);
    }
    
    public boolean isNever() {
        return this.getTableTurn() == RelativeFightTime.m_never.getTableTurn() && this.getFighterId() == RelativeFightTime.m_never.getFighterId() && this.isAtEndOfTurn() == RelativeFightTime.m_never.isAtEndOfTurn();
    }
    
    static {
        m_never = new RelativeFightTime(0L, 0, (short)(-1), false);
    }
}
