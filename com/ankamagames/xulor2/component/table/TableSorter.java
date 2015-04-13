package com.ankamagames.xulor2.component.table;

import java.util.*;

public class TableSorter
{
    private Comparator m_comparator;
    
    public TableSorter() {
        super();
    }
    
    public TableSorter(final Comparator comparator) {
        super();
        this.m_comparator = comparator;
    }
    
    public int[] getIndexesTranslation(final ArrayList values, final boolean direct) {
        final Object[] vals = values.toArray(new Object[values.size()]);
        Arrays.sort(vals, this.m_comparator);
        final int[] indexes = new int[values.size()];
        for (int i = 0, size = vals.length; i < size; ++i) {
            if (direct) {
                indexes[i] = values.indexOf(vals[i]);
            }
            else {
                indexes[size - i - 1] = values.indexOf(vals[i]);
            }
        }
        return indexes;
    }
}
