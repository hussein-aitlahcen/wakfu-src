package com.ankamagames.wakfu.client.core.game.dungeon;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class DungeonListView extends ImmutableFieldProvider
{
    public static final String CURRENT_FILTER = "currentFilter";
    public static final String CURRENT_DUNGEON = "currentDungeon";
    public static final String FILTERS = "filters";
    public static final String DUNGEONS = "dungeons";
    private final ArrayList<DungeonFilterTypeView> m_filters;
    private final TIntObjectHashMap<DungeonView> m_allViews;
    private DungeonFilterTypeView m_currentFilter;
    private final ArrayList<DungeonView> m_filteredList;
    private int m_currentDungeonId;
    
    public DungeonListView() {
        super();
        this.m_filters = new ArrayList<DungeonFilterTypeView>();
        this.m_allViews = new TIntObjectHashMap<DungeonView>();
        this.m_filteredList = new ArrayList<DungeonView>();
        this.m_currentDungeonId = -1;
        this.m_currentDungeonId = -1;
        this.createFilters();
        this.createAllViews();
        this.computeFilteredList();
    }
    
    private void createFilters() {
        for (final DungeonFilterType type : DungeonFilterType.values()) {
            this.m_filters.add(new DungeonFilterTypeView(type));
        }
        this.m_currentFilter = this.m_filters.get(0);
    }
    
    private void createAllViews() {
        DungeonManager.INSTANCE.forEachValue(new TObjectProcedure<DungeonDefinition>() {
            @Override
            public boolean execute(final DungeonDefinition def) {
                final DungeonView view = new DungeonView(def.getId());
                DungeonListView.this.m_allViews.put(def.getId(), view);
                return true;
            }
        });
    }
    
    public void setCurrentDungeon(final int id) {
        this.m_currentDungeonId = id;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentDungeon");
    }
    
    public void setCurrentFilter(final byte id) {
        for (int i = 0, size = this.m_filters.size(); i < size; ++i) {
            final DungeonFilterTypeView view = this.m_filters.get(i);
            if (view.getType().getId() == id) {
                this.setCurrentFilter(view);
                return;
            }
        }
    }
    
    public void setCurrentFilter(final DungeonFilterTypeView currentFilter) {
        if (this.m_currentFilter == currentFilter) {
            return;
        }
        this.m_currentFilter = currentFilter;
        this.computeFilteredList();
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentDungeon")) {
            return this.m_allViews.get(this.m_currentDungeonId);
        }
        if (fieldName.equals("dungeons")) {
            return this.m_filteredList;
        }
        if (fieldName.equals("filters")) {
            return this.m_filters;
        }
        if (fieldName.equals("currentFilter")) {
            return this.m_currentFilter;
        }
        return null;
    }
    
    private void computeFilteredList() {
        this.m_filteredList.clear();
        this.m_allViews.forEachValue(new TObjectProcedure<DungeonView>() {
            @Override
            public boolean execute(final DungeonView dungeon) {
                if (DungeonListView.this.m_currentFilter.getType().isValid(dungeon)) {
                    DungeonListView.this.m_filteredList.add(dungeon);
                }
                else if (DungeonListView.this.m_currentDungeonId == dungeon.getId()) {
                    DungeonListView.this.m_currentDungeonId = -1;
                    PropertiesProvider.getInstance().firePropertyValueChanged(DungeonListView.this, "currentDungeon");
                }
                return true;
            }
        });
        Collections.sort(this.m_filteredList, DungeonViewComparator.INSTANCE);
        if (this.m_currentDungeonId == -1 && !this.m_filteredList.isEmpty()) {
            this.m_currentDungeonId = this.m_filteredList.get(0).getId();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "dungeons", "currentDungeon");
    }
}
