package com.ankamagames.wakfu.common.game.protector;

public class ProtectorBuffList
{
    private final int m_id;
    private final int[] m_buffIds;
    
    protected ProtectorBuffList(final int id, final int[] buffIds) {
        super();
        this.m_id = id;
        this.m_buffIds = buffIds;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int[] getBuffIds() {
        return this.m_buffIds;
    }
}
