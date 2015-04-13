package com.ankamagames.wakfu.common.game.travel.infos;

public class ZaapLink
{
    private final long m_id;
    private final int m_startZaapId;
    private final int m_endZaapId;
    private final int m_cost;
    
    public ZaapLink(final long id, final int startZaapId, final int endZaapId, final int cost) {
        super();
        this.m_id = id;
        this.m_startZaapId = startZaapId;
        this.m_endZaapId = endZaapId;
        this.m_cost = cost;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getStartZaapId() {
        return this.m_startZaapId;
    }
    
    public int getEndZaapId() {
        return this.m_endZaapId;
    }
    
    public int getCost() {
        return this.m_cost;
    }
}
