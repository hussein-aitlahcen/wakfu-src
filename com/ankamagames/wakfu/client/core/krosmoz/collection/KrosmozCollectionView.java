package com.ankamagames.wakfu.client.core.krosmoz.collection;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.krosmoz.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;
import java.util.*;
import gnu.trove.*;

public class KrosmozCollectionView extends ImmutableFieldProvider
{
    public static final int FIRST_SEASON = 1;
    public static final String CURRENT_SEASON = "currentSeason";
    public static final String CAN_GO_BEFORE = "canGoBefore";
    public static final String CAN_GO_AFTER = "canGoAfter";
    public static final KrosmozCollectionView INSTANCE;
    private final THashMap<String, KrosmozFigure> m_figures;
    private boolean m_needsToBeFilled;
    private int m_currentSeason;
    
    private void computeViews() {
        final TIntIntHashMap countMap = new TIntIntHashMap();
        KrosmozViewManager.INSTANCE.forEachFigureView(new TObjectProcedure<KrosmozFigureView>() {
            @Override
            public boolean execute(final KrosmozFigureView object) {
                countMap.put(object.getFigure().getFigureId(), 0);
                return true;
            }
        });
        for (final KrosmozFigure figure : this.m_figures.values()) {
            countMap.adjustOrPutValue(figure.getCharacter(), 1, 1);
        }
        final TIntIntIterator it = countMap.iterator();
        while (it.hasNext()) {
            it.advance();
            final int figureId = it.key();
            final int figureCount = it.value();
            final KrosmozFigureView view = KrosmozViewManager.INSTANCE.getFigureView(figureId);
            if (view != null) {
                view.setSize(figureCount);
            }
        }
    }
    
    private KrosmozCollectionView() {
        super();
        this.m_figures = new THashMap<String, KrosmozFigure>();
        this.m_needsToBeFilled = true;
        this.m_currentSeason = 1;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    private boolean canGoAfter() {
        return this.m_currentSeason + 1 <= KrosmozFigureDataManager.INSTANCE.getSeasonCount();
    }
    
    private boolean canGoBefore() {
        return this.m_currentSeason > 1;
    }
    
    private void setCurrentSeason(final int season) {
        this.m_currentSeason = season;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentSeason");
    }
    
    public void nextSeason() {
        if (this.canGoAfter()) {
            this.setCurrentSeason(this.m_currentSeason + 1);
        }
    }
    
    public void previousSeason() {
        if (this.canGoBefore()) {
            this.setCurrentSeason(this.m_currentSeason - 1);
        }
    }
    
    public KrosmozFigure getFirstWithId(final int figureId) {
        boolean found = false;
        for (final KrosmozFigure figure : this.m_figures.values()) {
            if (figure.getCharacter() == figureId) {
                if (KrosmozFigureHelper.isValidPedestal(figure.getPedestal())) {
                    return figure;
                }
                found = true;
            }
        }
        if (found) {
            return KrosmozFigureHelper.INVALID_KROSMOZ_FIGURE;
        }
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentSeason")) {
            return KrosmozViewManager.INSTANCE.getSeasonView(this.m_currentSeason);
        }
        if (fieldName.equals("canGoAfter")) {
            return this.canGoAfter();
        }
        if (fieldName.equals("canGoBefore")) {
            return this.canGoBefore();
        }
        return null;
    }
    
    public void onFigureAdded(final KrosmozFigure figure) {
        this.m_figures.put(figure.getGuid(), figure);
        final KrosmozFigureView view = KrosmozViewManager.INSTANCE.getFigureView(figure.getCharacter());
        view.addFigure();
    }
    
    public void onFigureRemoved(final KrosmozFigure figure) {
        this.m_figures.remove(figure.getGuid());
        final KrosmozFigureView view = KrosmozViewManager.INSTANCE.getFigureView(figure.getCharacter());
        view.removeFigure();
    }
    
    public void setFigures(final ArrayList<KrosmozFigure> figures) {
        this.m_figures.clear();
        for (int i = 0, size = figures.size(); i < size; ++i) {
            final KrosmozFigure figure = figures.get(i);
            if (!figure.isBound()) {
                this.m_figures.put(figure.getGuid(), figure);
            }
        }
        this.computeViews();
        this.m_needsToBeFilled = false;
    }
    
    public void clear() {
        this.m_figures.clear();
        this.m_needsToBeFilled = true;
    }
    
    public boolean needsToBeFilled() {
        return this.m_needsToBeFilled;
    }
    
    static {
        INSTANCE = new KrosmozCollectionView();
    }
}
