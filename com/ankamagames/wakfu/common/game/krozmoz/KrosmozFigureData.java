package com.ankamagames.wakfu.common.game.krozmoz;

public class KrosmozFigureData
{
    private final int m_figureId;
    private final int m_year;
    private final int m_addon;
    private final int m_season;
    private final int m_index;
    
    public KrosmozFigureData(final int figureId, final int year, final int addon, final int season, final int index) {
        super();
        this.m_figureId = figureId;
        this.m_year = year;
        this.m_addon = addon;
        this.m_season = season;
        this.m_index = index;
    }
    
    public int getFigureId() {
        return this.m_figureId;
    }
    
    public int getYear() {
        return this.m_year;
    }
    
    public int getAddon() {
        return this.m_addon;
    }
    
    public int getSeason() {
        return this.m_season;
    }
    
    public int getIndexInSeason() {
        return this.m_index;
    }
}
