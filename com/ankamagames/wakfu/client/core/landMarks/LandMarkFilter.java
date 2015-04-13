package com.ankamagames.wakfu.client.core.landMarks;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.java.util.*;

public class LandMarkFilter implements FieldProvider
{
    public static final String TYPE_FILTER_LIST_FIELD = "typeFilterList";
    public static final String[] FIELDS;
    protected ArrayList<LandMarkFilterElement> m_types;
    
    public LandMarkFilter() {
        super();
        this.m_types = new ArrayList<LandMarkFilterElement>();
        this.init();
    }
    
    protected final void init() {
        this.m_types.clear();
        for (final LandMarkEnum e : LandMarkEnum.values()) {
            if (this.acceptLandMark(e)) {
                this.m_types.add(this.createFilter(e));
            }
        }
    }
    
    private boolean acceptLandMark(final LandMarkEnum e) {
        return e != LandMarkEnum.NONE;
    }
    
    protected LandMarkFilterElement createFilter(final LandMarkEnum e) {
        return new LandMarkFilterElement(e.getType());
    }
    
    @Override
    public String[] getFields() {
        return LandMarkFilter.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("typeFilterList")) {
            return this.m_types;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    public LandMarkFilterElement getFilter(final byte type) {
        for (final LandMarkFilterElement filter : this.m_types) {
            if (filter.getId() == type) {
                return filter;
            }
        }
        return null;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void setSelected(final LandMarkEnum landMark, final boolean selected) {
        this.setSelected(landMark.getType(), selected);
    }
    
    public void setSelected(final byte landMarkType, final boolean selected) {
        this.setSelected(landMarkType, selected, true);
    }
    
    protected void setSelected(final byte landMarkType, final boolean selected, final boolean updatePreferences) {
        for (final LandMarkFilterElement elem : this.m_types) {
            if (elem.getId() == landMarkType) {
                elem.setSelected(selected);
                break;
            }
        }
        if (updatePreferences) {
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.SELECTED_LANDMARK_FILTERS, this.getSelectedLandMarksPreference());
        }
    }
    
    public void selectAllFilters() {
        for (final LandMarkFilterElement elem : this.m_types) {
            elem.setSelected(true);
        }
    }
    
    public void unselectAllFilters() {
        for (final LandMarkFilterElement elem : this.m_types) {
            elem.setSelected(false);
        }
    }
    
    public void updateFromPreferences() {
        this.setSelectedLandMarksFromPreferences(WakfuClientInstance.getInstance().getGamePreferences().getStringValue(WakfuKeyPreferenceStoreEnum.SELECTED_LANDMARK_FILTERS));
    }
    
    private String getSelectedLandMarksPreference() {
        final StringBuilder sb = new StringBuilder();
        for (final LandMarkFilterElement elem : this.m_types) {
            if (elem.isSelected(null)) {
                sb.append(elem.getId()).append(";");
            }
        }
        return sb.toString();
    }
    
    public void setSelectedLandMarksFromPreferences(final String preferences) {
        for (final LandMarkFilterElement elem : this.m_types) {
            elem.setSelected(false);
        }
        final String[] arr$;
        final String[] types = arr$ = preferences.split(";");
        for (final String type : arr$) {
            final byte typeByte = PrimitiveConverter.getByte(type, (byte)(-1));
            if (typeByte != -1) {
                this.setSelected(typeByte, true, false);
            }
        }
    }
    
    public ArrayList<LandMarkFilterElement> getTypes() {
        return this.m_types;
    }
    
    public boolean isFiltered(final DisplayableMapPoint point, final byte type) {
        final LandMarkFilterElement filter = this.getFilter(type);
        return filter != null && !filter.isSelected(point);
    }
    
    static {
        FIELDS = new String[] { "typeFilterList" };
    }
}
