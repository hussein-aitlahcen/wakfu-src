package com.ankamagames.baseImpl.common.clientAndServer.utils;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;

public class Constants<O> implements Iterable<ConstantDefinition<O>>
{
    private final TIntObjectHashMap<ConstantDefinition<O>> m_list;
    
    public Constants() {
        super();
        this.m_list = new TIntObjectHashMap<ConstantDefinition<O>>();
    }
    
    public void addConstantDefinition(final ConstantDefinition<O> def) {
        this.m_list.put(def.getId(), def);
    }
    
    public final ConstantDefinition<O> getConstantDefinition(final int id) {
        return this.m_list.get(id);
    }
    
    public final O getObjectFromId(final int id) {
        final ConstantDefinition<O> cd = this.m_list.get(id);
        if (cd != null) {
            return cd.getObject();
        }
        return null;
    }
    
    @Override
    public Iterator<ConstantDefinition<O>> iterator() {
        return new TroveIntHashMapValueIterator<ConstantDefinition<O>>(this.m_list);
    }
}
