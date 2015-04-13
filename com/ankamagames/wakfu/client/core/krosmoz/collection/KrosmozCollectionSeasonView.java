package com.ankamagames.wakfu.client.core.krosmoz.collection;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.krosmoz.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;

public class KrosmozCollectionSeasonView extends ImmutableFieldProvider
{
    public static final String FIGURES = "figures";
    public static final String NAME = "name";
    private final int m_season;
    private final ArrayList<KrosmozFigureView> m_views;
    
    public KrosmozCollectionSeasonView(final int season) {
        super();
        this.m_season = season;
        this.m_views = new ArrayList<KrosmozFigureView>();
        this.createViews();
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("figures")) {
            return this.m_views;
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("krosmoz.figure.season", this.m_season);
        }
        return null;
    }
    
    private void createViews() {
        this.m_views.clear();
        final ArrayList<KrosmozFigureData> list = KrosmozFigureDataManager.INSTANCE.getFiguresInSeason(this.m_season);
        for (int i = 0, size = list.size(); i < size; ++i) {
            final KrosmozFigureData figureData = list.get(i);
            final KrosmozFigureView view = KrosmozViewManager.INSTANCE.getFigureView(figureData.getFigureId());
            this.m_views.add(view);
        }
    }
}
