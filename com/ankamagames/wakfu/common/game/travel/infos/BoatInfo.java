package com.ankamagames.wakfu.common.game.travel.infos;

import com.ankamagames.wakfu.common.game.travel.*;

public class BoatInfo extends TravelInfo
{
    private final int m_exitX;
    private final int m_exitY;
    private final int m_exitWorldId;
    
    public BoatInfo(final long id, final int visualId, final int exitX, final int exitY, final int exitWorldId) {
        super(id, visualId);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_exitWorldId = exitWorldId;
    }
    
    public BoatInfo(final long id, final int visualId, final int exitX, final int exitY, final int exitWorldId, final int uiGfxId, final TravelType landmarkTravelType) {
        super(id, visualId, uiGfxId, landmarkTravelType);
        this.m_exitX = exitX;
        this.m_exitY = exitY;
        this.m_exitWorldId = exitWorldId;
    }
    
    public int getExitX() {
        return this.m_exitX;
    }
    
    public int getExitY() {
        return this.m_exitY;
    }
    
    public int getExitWorldId() {
        return this.m_exitWorldId;
    }
}
