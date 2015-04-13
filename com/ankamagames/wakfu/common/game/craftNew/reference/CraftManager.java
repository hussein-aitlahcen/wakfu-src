package com.ankamagames.wakfu.common.game.craftNew.reference;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.game.craft.*;
import gnu.trove.*;

public class CraftManager
{
    public static final CraftManager INSTANCE;
    private final IntObjectLightWeightMap<CraftConcept> m_craftConcepts;
    private final IntObjectLightWeightMap<ReferenceCraft> m_crafts;
    
    public CraftManager() {
        super();
        this.m_craftConcepts = new IntObjectLightWeightMap<CraftConcept>();
        this.m_crafts = new IntObjectLightWeightMap<ReferenceCraft>();
    }
    
    public void addCraft(final ReferenceCraft craft) {
        final int id = craft.getId();
        this.m_crafts.put(id, craft);
        if (craft.isConceptualCraft()) {
            this.m_craftConcepts.put(id, new CraftConcept(id));
        }
    }
    
    public ReferenceCraft getCraft(final int craftId) {
        return this.m_crafts.get(craftId);
    }
    
    public CraftConcept getCraftConcept(final int craftId) {
        return this.m_craftConcepts.get(craftId);
    }
    
    public void foreachInnateCraft(final TObjectProcedure<ReferenceCraft> procedure) {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final ReferenceCraft craft = this.m_crafts.getQuickValue(i);
            if (craft.isInnate()) {
                if (!procedure.execute(craft)) {
                    return;
                }
            }
        }
    }
    
    public int getCraftsCount() {
        return this.m_crafts.size();
    }
    
    public void foreachCraft(final TObjectProcedure<ReferenceCraft> procedure) {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final ReferenceCraft craft = this.m_crafts.getQuickValue(i);
            if (!procedure.execute(craft)) {
                return;
            }
        }
    }
    
    public boolean hasCraftUsingAnyIngredient(final int... itemIds) {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final ReferenceCraft craft = this.m_crafts.getQuickValue(i);
            for (int j = 0; j < itemIds.length; ++j) {
                final int itemId = itemIds[j];
                if (craft.hasIngredientToRecipe(itemId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public CraftRecipeReference getCraftRecipe(final int recipeId) {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final ReferenceCraft craft = this.m_crafts.getQuickValue(i);
            final CraftRecipeReference recipe = craft.getRecipe(recipeId);
            if (recipe != null) {
                return recipe;
            }
        }
        return null;
    }
    
    static {
        INSTANCE = new CraftManager();
    }
}
