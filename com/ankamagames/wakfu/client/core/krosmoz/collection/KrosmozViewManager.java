package com.ankamagames.wakfu.client.core.krosmoz.collection;

import com.ankamagames.wakfu.client.core.krosmoz.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import gnu.trove.*;

public class KrosmozViewManager
{
    public static final KrosmozViewManager INSTANCE;
    private final TIntObjectHashMap<KrosmozFigureView> m_figureViews;
    private final TIntObjectHashMap<KrosmozCollectionSeasonView> m_seasonViews;
    
    public KrosmozViewManager() {
        super();
        this.m_figureViews = new TIntObjectHashMap<KrosmozFigureView>();
        this.m_seasonViews = new TIntObjectHashMap<KrosmozCollectionSeasonView>();
    }
    
    public KrosmozFigureView getFigureView(final int figureId) {
        KrosmozFigureView view = this.m_figureViews.get(figureId);
        if (view == null) {
            final KrosmozFigureData figureData = KrosmozFigureDataManager.INSTANCE.getFigureData(figureId);
            if (figureData == null) {
                return null;
            }
            view = new KrosmozFigureView(figureData);
            this.m_figureViews.put(figureId, view);
        }
        return view;
    }
    
    public void forEachFigureView(final TObjectProcedure<KrosmozFigureView> procedure) {
        this.m_figureViews.forEachValue(procedure);
    }
    
    public KrosmozCollectionSeasonView getSeasonView(final int seasonId) {
        KrosmozCollectionSeasonView view = this.m_seasonViews.get(seasonId);
        if (view == null) {
            view = new KrosmozCollectionSeasonView(seasonId);
            this.m_seasonViews.put(seasonId, view);
        }
        return view;
    }
    
    public void forEachSeasonView(final TObjectProcedure<KrosmozCollectionSeasonView> procedure) {
        this.m_seasonViews.forEachValue(procedure);
    }
    
    static {
        INSTANCE = new KrosmozViewManager();
    }
}
