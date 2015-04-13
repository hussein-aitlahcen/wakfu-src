package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import com.ankamagames.xulor2.property.*;

public class HavenWorldCatalogSubCategoryView extends ImmutableFieldProvider
{
    public static final String ELEMENTS_FIELD = "elements";
    public static final String NAME_FIELD = "name";
    public static final String OPENNED_FIELD = "openned";
    public static final String[] FIELDS;
    private boolean m_openned;
    private int m_categoryId;
    private HavenWorldCatalogView.CatalogCategory m_catalogCategory;
    final ArrayList<FieldProvider> m_entries;
    
    @Override
    public String[] getFields() {
        return HavenWorldCatalogSubCategoryView.FIELDS;
    }
    
    public HavenWorldCatalogSubCategoryView(final HavenWorldCatalogView.CatalogCategory category, final int categoryId) {
        super();
        this.m_entries = new ArrayList<FieldProvider>();
        this.m_catalogCategory = category;
        this.m_categoryId = categoryId;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("elements")) {
            return this.m_entries;
        }
        if (fieldName.equals("name")) {
            switch (this.m_catalogCategory) {
                case PATCH: {
                    return HavenWorldCatalogCategoryManager.getInstance().getPatchCategoryName(this.m_categoryId);
                }
                case BUILDING: {
                    return HavenWorldCatalogCategoryManager.getInstance().getBuildingCategoryName(this.m_categoryId);
                }
                case BUILDING_DECO: {
                    return HavenWorldCatalogCategoryManager.getInstance().getBuildingCategoryName(this.m_categoryId);
                }
            }
        }
        if (fieldName.equals("openned")) {
            return this.m_openned;
        }
        return null;
    }
    
    public void addEntry(final FieldProvider havenWorldCatalogEntryView) {
        if (this.m_entries.contains(havenWorldCatalogEntryView)) {
            return;
        }
        this.m_entries.add(havenWorldCatalogEntryView);
    }
    
    public int getOrder() {
        return HavenWorldCatalogCategoryManager.getInstance().getCategoryOrder(this.m_categoryId);
    }
    
    public void toggleOpen() {
        this.m_openned = !this.m_openned;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "openned");
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public boolean isOpen() {
        return this.m_openned;
    }
    
    public int size() {
        return this.m_entries.size();
    }
    
    static {
        FIELDS = new String[] { "elements", "openned" };
    }
}
