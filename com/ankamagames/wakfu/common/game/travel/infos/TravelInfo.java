package com.ankamagames.wakfu.common.game.travel.infos;

import com.ankamagames.wakfu.common.game.travel.*;

public abstract class TravelInfo
{
    protected final long m_id;
    protected final int m_visualId;
    protected final int m_uiGfxId;
    protected final TravelType m_landmarkTravelType;
    
    public TravelInfo(final long id, final int visualId) {
        this(id, visualId, -1, TravelType.BOAT);
    }
    
    public TravelInfo(final long id, final int visualId, final int uiGfxId, final TravelType landmarkTravelType) {
        super();
        this.m_id = id;
        this.m_visualId = visualId;
        this.m_uiGfxId = uiGfxId;
        this.m_landmarkTravelType = landmarkTravelType;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getUiGfxId() {
        return this.m_uiGfxId;
    }
    
    public TravelType getLandmarkTravelType() {
        return this.m_landmarkTravelType;
    }
}
