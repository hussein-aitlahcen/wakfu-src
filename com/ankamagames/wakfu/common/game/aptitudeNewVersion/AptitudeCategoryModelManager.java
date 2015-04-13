package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import java.util.*;
import gnu.trove.*;

public final class AptitudeCategoryModelManager
{
    public static final AptitudeCategoryModelManager INSTANCE;
    private final TIntObjectHashMap<AptitudeCategoryModel> m_idToCategory;
    private final TIntIntHashMap m_bonusIdToCategoryId;
    
    public AptitudeCategoryModelManager() {
        super();
        this.m_idToCategory = new TIntObjectHashMap<AptitudeCategoryModel>();
        this.m_bonusIdToCategoryId = new TIntIntHashMap();
    }
    
    public void addCategory(final AptitudeCategoryModel categoryModel) {
        final int categoryId = categoryModel.getId();
        this.m_idToCategory.put(categoryId, categoryModel);
        final Set<AptitudeBonusModel> bonusSet = categoryModel.getBonusSet();
        for (final AptitudeBonusModel bonusModel : bonusSet) {
            if (bonusModel != null) {
                this.m_bonusIdToCategoryId.put(bonusModel.getId(), categoryId);
            }
        }
    }
    
    public int getBonusCategoryId(final int bonusId) {
        return this.m_bonusIdToCategoryId.get(bonusId);
    }
    
    public TIntHashSet getActivatedCategoriesAtLevel(final short level) {
        final TIntHashSet result = new TIntHashSet();
        this.m_idToCategory.forEachValue(new TObjectProcedure<AptitudeCategoryModel>() {
            @Override
            public boolean execute(final AptitudeCategoryModel category) {
                if (category.hasActivationLevel(level)) {
                    result.add(category.getId());
                }
                return true;
            }
        });
        return result;
    }
    
    public boolean forEachCategory(final TObjectProcedure<AptitudeCategoryModel> procedure) {
        return this.m_idToCategory.forEachValue(procedure);
    }
    
    static {
        INSTANCE = new AptitudeCategoryModelManager();
    }
}
