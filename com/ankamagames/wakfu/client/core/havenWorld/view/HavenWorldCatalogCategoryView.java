package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import gnu.trove.*;
import java.util.*;

public class HavenWorldCatalogCategoryView extends ImmutableFieldProvider
{
    public static final Comparator<HavenWorldCatalogSubCategoryView> SUBCATEGORY_SORTER;
    public static final String ELEMENTS_FIELD = "elements";
    public static final String NAME_FIELD = "name";
    public static final String ICON_STYLE_FIELD = "iconStyle";
    public static final String ENABLED_FIELD = "enabled";
    public static final String[] FIELDS;
    private final HavenWorldCatalogView.CatalogCategory m_category;
    final TIntObjectHashMap<HavenWorldCatalogSubCategoryView> m_entries;
    
    @Override
    public String[] getFields() {
        return HavenWorldCatalogCategoryView.FIELDS;
    }
    
    public HavenWorldCatalogCategoryView(final HavenWorldCatalogView.CatalogCategory catalogCategory) {
        super();
        this.m_entries = new TIntObjectHashMap<HavenWorldCatalogSubCategoryView>();
        this.m_category = catalogCategory;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("elements")) {
            final ArrayList<HavenWorldCatalogSubCategoryView> subCategoryViews = this.getSortedSubCategories();
            final HavenWorldCatalogSubCategoryView categoryView = subCategoryViews.get(0);
            if (!categoryView.isOpen()) {
                categoryView.toggleOpen();
            }
            return subCategoryViews;
        }
        if (fieldName.equals("name")) {
            final int categoryId = this.m_category.getId();
            return (categoryId == -1) ? WakfuTranslator.getInstance().getString(this.m_category.getTag()) : HavenWorldCatalogCategoryManager.getInstance().getBuildingCategoryName(categoryId);
        }
        if (fieldName.equals("iconStyle")) {
            return this.m_category.getStyle();
        }
        if (fieldName.equals("enabled")) {
            return !this.m_entries.isEmpty();
        }
        return null;
    }
    
    private ArrayList<HavenWorldCatalogSubCategoryView> getSortedSubCategories() {
        final ArrayList<HavenWorldCatalogSubCategoryView> subCategoryViews = new ArrayList<HavenWorldCatalogSubCategoryView>();
        this.m_entries.forEachValue(new TObjectProcedure<HavenWorldCatalogSubCategoryView>() {
            @Override
            public boolean execute(final HavenWorldCatalogSubCategoryView subCateg) {
                subCategoryViews.add(subCateg);
                return true;
            }
        });
        Collections.sort(subCategoryViews, HavenWorldCatalogCategoryView.SUBCATEGORY_SORTER);
        return subCategoryViews;
    }
    
    public void putCategory(final HavenWorldCatalogSubCategoryView subCategory) {
        this.m_entries.put(subCategory.getCategoryId(), subCategory);
    }
    
    public HavenWorldCatalogSubCategoryView getCategory(final int categoryId) {
        return this.m_entries.get(categoryId);
    }
    
    public byte getId() {
        return (byte)this.m_category.getId();
    }
    
    public byte order() {
        return (byte)this.m_category.ordinal();
    }
    
    public void sort(final Comparator<ImmutableFieldProvider> comparator) {
    }
    
    static {
        SUBCATEGORY_SORTER = new Comparator<HavenWorldCatalogSubCategoryView>() {
            @Override
            public int compare(final HavenWorldCatalogSubCategoryView o1, final HavenWorldCatalogSubCategoryView o2) {
                return o1.getOrder() - o2.getOrder();
            }
        };
        FIELDS = new String[] { "elements" };
    }
}
