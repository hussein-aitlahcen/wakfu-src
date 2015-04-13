package com.ankamagames.wakfu.common.game.personalSpace.data;

public class DimensionalBagModelView
{
    private final int m_id;
    private final short m_backgroundMapId;
    private final boolean m_allowedInWorld;
    private final boolean m_allowedOnMarket;
    private final boolean m_isInnate;
    
    public DimensionalBagModelView(final int id, final boolean worldRestriction, final boolean marketRestriction, final short backgroundMapId, final boolean isInnate) {
        super();
        this.m_id = id;
        this.m_allowedInWorld = !worldRestriction;
        this.m_allowedOnMarket = !marketRestriction;
        this.m_backgroundMapId = backgroundMapId;
        this.m_isInnate = isInnate;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isAllowedInWorld() {
        return this.m_allowedInWorld;
    }
    
    public boolean isAllowedOnMarket() {
        return this.m_allowedOnMarket;
    }
    
    public short getBackgroundMapId() {
        return this.m_backgroundMapId;
    }
    
    public boolean isInnate() {
        return this.m_isInnate;
    }
}
