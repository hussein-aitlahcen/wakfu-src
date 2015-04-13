package com.ankamagames.wakfu.client.core.krosmoz;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import java.util.*;

public class KrosmozFigureDataManager
{
    public static final KrosmozFigureDataManager INSTANCE;
    private final TIntObjectHashMap<KrosmozFigureData> m_figures;
    private final TIntObjectHashMap<ArrayList<KrosmozFigureData>> m_figuresBySeason;
    
    private KrosmozFigureDataManager() {
        super();
        this.m_figures = new TIntObjectHashMap<KrosmozFigureData>();
        this.m_figuresBySeason = new TIntObjectHashMap<ArrayList<KrosmozFigureData>>();
    }
    
    public void addFigureData(final int figureId, final int year, final int addon, final int season) {
        ArrayList<KrosmozFigureData> figuresBySeason = this.m_figuresBySeason.get(season);
        if (figuresBySeason == null) {
            figuresBySeason = new ArrayList<KrosmozFigureData>();
            this.m_figuresBySeason.put(season, figuresBySeason);
        }
        final int figureIndex = figuresBySeason.size();
        final KrosmozFigureData data = new KrosmozFigureData(figureId, year, addon, season, figureIndex);
        this.m_figures.put(figureId, data);
        figuresBySeason.add(data);
    }
    
    public KrosmozFigureData getFigureData(final int figureId) {
        return this.m_figures.get(figureId);
    }
    
    public ArrayList<KrosmozFigureData> getFiguresInSeason(final int season) {
        return this.m_figuresBySeason.get(season);
    }
    
    public int getSeasonCount() {
        return this.m_figuresBySeason.size();
    }
    
    public int getNumFiguresInSeason(final int season) {
        final ArrayList<KrosmozFigureData> list = this.m_figuresBySeason.get(season);
        return (list != null) ? list.size() : 0;
    }
    
    static {
        INSTANCE = new KrosmozFigureDataManager();
    }
}
