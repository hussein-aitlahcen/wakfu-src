package com.ankamagames.wakfu.client.core.game.achievements.mercenary;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class MercenaryFilterView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    private static final Map<MercenaryFilter, MercenaryFilterView> VIEWS;
    private final MercenaryFilter m_filter;
    
    public static MercenaryFilterView getView(final MercenaryFilter filter) {
        return MercenaryFilterView.VIEWS.get(filter);
    }
    
    private MercenaryFilterView(final MercenaryFilter filter) {
        super();
        this.m_filter = filter;
    }
    
    public MercenaryFilter getFilter() {
        return this.m_filter;
    }
    
    @Override
    public String[] getFields() {
        return MercenaryFilterView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("mercenary.filter." + this.m_filter.ordinal());
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "MercenaryFilterView{m_filter=" + this.m_filter + '}';
    }
    
    static {
        VIEWS = new EnumMap<MercenaryFilter, MercenaryFilterView>(MercenaryFilter.class);
        for (final MercenaryFilter f : MercenaryFilter.values()) {
            MercenaryFilterView.VIEWS.put(f, new MercenaryFilterView(f));
        }
    }
}
