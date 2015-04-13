package com.ankamagames.framework.external;

import java.util.*;

public abstract class ParameterListSet implements Iterable<ParameterList>
{
    private final HashMap<Integer, ParameterList> m_listSet;
    
    protected ParameterListSet(final ParameterList... lists) {
        super();
        this.m_listSet = new HashMap<Integer, ParameterList>();
        for (final ParameterList list : lists) {
            if (this.m_listSet.containsKey(list.getParametersCount())) {
                throw new RuntimeException("D\u00e9finition des listes de param\u00e8tres impossibles : liste \u00e0 " + list.getParametersCount() + " param\u00e8tres d\u00e9j\u00e0 d\u00e9finie");
            }
            this.m_listSet.put(list.getParametersCount(), list);
        }
    }
    
    public ParameterList getParameterList(final int idx) {
        return this.m_listSet.get(idx);
    }
    
    public int getMaxParams() {
        int num = 0;
        for (final ParameterList l : this.m_listSet.values()) {
            num = Math.max(num, l.getParametersCount());
        }
        return num;
    }
    
    @Override
    public final Iterator<ParameterList> iterator() {
        return this.m_listSet.values().iterator();
    }
    
    public final boolean mapParameterCount(final int count) {
        if (count > 0) {
            return this.m_listSet.containsKey(count);
        }
        return this.m_listSet.isEmpty() || this.m_listSet.containsKey(0);
    }
    
    public int size() {
        return this.m_listSet.size();
    }
    
    public abstract boolean mapValueCount(final int p0);
}
