package com.ankamagames.wakfu.client.core.protector.ecosystem;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class ProtectorEcosystemView extends ImmutableFieldProvider
{
    public static final String CATEGORIES = "categories";
    public static final String SELECTED_CATEGORY = "selectedCategory";
    private final ArrayList<ProtectorEcosystemCategory> m_categories;
    private boolean m_isMonsterSelected;
    
    public ProtectorEcosystemView(final ProtectorBase protector) {
        super();
        this.m_categories = new ArrayList<ProtectorEcosystemCategory>();
        this.m_isMonsterSelected = true;
        this.m_categories.add(new ProtectorEcosystemCategory(protector, true));
        this.m_categories.add(new ProtectorEcosystemCategory(protector, false));
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("categories")) {
            return this.m_categories;
        }
        if (fieldName.equals("selectedCategory")) {
            return this.m_isMonsterSelected ? this.m_categories.get(0) : this.m_categories.get(1);
        }
        return null;
    }
    
    public void setSelected(final boolean monster) {
        this.m_isMonsterSelected = monster;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedCategory");
    }
    
    public ProtectorEcosystemElement getElement(final int id, final boolean monster) {
        return this.m_categories.get(monster ? 0 : 1).getElement(id);
    }
}
