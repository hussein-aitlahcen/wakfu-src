package com.ankamagames.wakfu.common.game.personalSpace.data;

import gnu.trove.*;

public class DimensionalBagModelViewManager
{
    public static final DimensionalBagModelViewManager INSTANCE;
    private final TIntObjectHashMap<DimensionalBagModelView> m_views;
    private final TIntHashSet m_innateViews;
    
    public DimensionalBagModelViewManager() {
        super();
        this.m_views = new TIntObjectHashMap<DimensionalBagModelView>();
        this.m_innateViews = new TIntHashSet();
    }
    
    public void addView(final DimensionalBagModelView view) {
        final int viewId = view.getId();
        this.m_views.put(viewId, view);
        if (view.isInnate()) {
            this.m_innateViews.add(viewId);
        }
    }
    
    public DimensionalBagModelView get(final int viewId) {
        return this.m_views.get(viewId);
    }
    
    public boolean isAllowedInWorld(final int viewId) {
        final DimensionalBagModelView view = this.get(viewId);
        return view == null || view.isAllowedInWorld();
    }
    
    public boolean isAllowedOnMarket(final int viewId) {
        final DimensionalBagModelView view = this.get(viewId);
        return view == null || view.isAllowedOnMarket();
    }
    
    public short getBackgroundMapId(final int viewId) {
        final DimensionalBagModelView view = this.get(viewId);
        return (short)((view != null) ? view.getBackgroundMapId() : -1);
    }
    
    public boolean exists(final int modelId) {
        return this.m_views.contains(modelId);
    }
    
    public TIntHashSet getInnateViews() {
        return this.m_innateViews;
    }
    
    static {
        INSTANCE = new DimensionalBagModelViewManager();
    }
}
