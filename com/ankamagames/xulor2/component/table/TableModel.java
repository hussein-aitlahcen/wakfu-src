package com.ankamagames.xulor2.component.table;

import java.util.*;

public class TableModel
{
    private final HashMap<String, TableSorter> m_sorters;
    private String m_currentSorterId;
    private boolean m_direct;
    public TableModelChangeListener m_listener;
    
    public TableModel() {
        super();
        this.m_sorters = new HashMap<String, TableSorter>();
        this.m_currentSorterId = null;
        this.m_direct = true;
        this.m_listener = null;
    }
    
    public void setChangeListener(final TableModelChangeListener listener) {
        this.m_listener = listener;
    }
    
    public void putSorter(final String columnId, final TableSorter sorter) {
        this.m_sorters.put(columnId, sorter);
        if (this.m_currentSorterId == null) {
            this.m_currentSorterId = columnId;
        }
    }
    
    public void clear() {
        this.m_sorters.clear();
        this.m_currentSorterId = null;
        this.m_direct = true;
    }
    
    public int[] regenerateIndexes(final ArrayList values) {
        if (this.m_currentSorterId == null) {
            return null;
        }
        final TableSorter sorter = this.m_sorters.get(this.m_currentSorterId);
        if (sorter != null) {
            return sorter.getIndexesTranslation(values, this.m_direct);
        }
        return null;
    }
    
    public int[] getIndexes(final ArrayList values, final String columnId) {
        assert columnId != null : "columnId == null !";
        if (columnId.equals(this.m_currentSorterId)) {
            this.m_direct = !this.m_direct;
        }
        else {
            this.m_direct = true;
        }
        this.m_currentSorterId = columnId;
        if (this.m_listener != null) {
            this.m_listener.onColumnSortChanged(this.m_currentSorterId, this.m_direct);
        }
        final TableSorter sorter = this.m_sorters.get(this.m_currentSorterId);
        if (sorter == null) {
            return null;
        }
        return sorter.getIndexesTranslation(values, this.m_direct);
    }
    
    public boolean isDirect() {
        return this.m_direct;
    }
    
    public static int[] getSimpleOrder(final int size) {
        final int[] indexes = new int[size];
        for (int i = 0; i < size; ++i) {
            indexes[i] = i;
        }
        return indexes;
    }
    
    public interface TableModelChangeListener
    {
        void onColumnSortChanged(String p0, boolean p1);
    }
}
