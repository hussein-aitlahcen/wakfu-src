package com.ankamagames.wakfu.client.core.havenWorld;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;

public class HavenWorldCatalogCategoryManager
{
    private static final Logger m_logger;
    private static final HavenWorldCatalogCategoryManager m_instance;
    private final TIntShortHashMap m_categoryOrder;
    
    public static HavenWorldCatalogCategoryManager getInstance() {
        return HavenWorldCatalogCategoryManager.m_instance;
    }
    
    private HavenWorldCatalogCategoryManager() {
        super();
        this.m_categoryOrder = new TIntShortHashMap();
    }
    
    public String getBuildingCategoryName(final int id) {
        return WakfuTranslator.getInstance().getString(128, id, new Object[0]);
    }
    
    public String getPatchCategoryName(final int id) {
        return WakfuTranslator.getInstance().getString(129, id, new Object[0]);
    }
    
    public void loadCategoriesOrder() {
        this.m_categoryOrder.compact();
    }
    
    public int getCategoryOrder(final int categoryId) {
        return categoryId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldCatalogCategoryManager.class);
        m_instance = new HavenWorldCatalogCategoryManager();
    }
}
