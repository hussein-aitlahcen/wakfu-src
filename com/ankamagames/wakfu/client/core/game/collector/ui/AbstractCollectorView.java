package com.ankamagames.wakfu.client.core.game.collector.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.collector.*;

public abstract class AbstractCollectorView extends ImmutableFieldProvider
{
    public static final String CONTENT_FIELD = "content";
    public static final String KAMA_FIELD = "kama";
    public static final String NAME_FIELD = "name";
    public static final String LOCKED_FIELD = "locked";
    public static final String VALID_FIELD = "valid";
    public static final String FREE_BAG_SLOTS_SIZE_FIELD = "freeBagSlotsSize";
    public static final String C_FIELD = "freeBagSlotsSize";
    private static final String[] FIELDS;
    protected CollectorOccupationProvider m_collector;
    protected CollectorContentView m_kamaContentView;
    
    public AbstractCollectorView(final CollectorOccupationProvider Collector) {
        super();
        this.m_collector = Collector;
    }
    
    @Override
    public String[] getFields() {
        return AbstractCollectorView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_collector.getInteractiveElement().getName();
        }
        if (fieldName.equals("locked")) {
            return this.m_collector.getInfo().isLocked();
        }
        if (fieldName.equals("valid")) {
            return this.isValid();
        }
        return null;
    }
    
    public CollectorOccupationProvider getCollector() {
        return this.m_collector;
    }
    
    protected abstract boolean isValid();
    
    public abstract void validForm();
    
    public abstract int getKamaQuantity();
    
    static {
        FIELDS = new String[] { "content", "kama", "name", "locked", "valid", "freeBagSlotsSize" };
    }
}
