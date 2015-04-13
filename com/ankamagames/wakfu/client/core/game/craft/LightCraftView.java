package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;

public class LightCraftView extends UnknownCraftView
{
    public static final String RECIPES_FIELD = "recipes";
    private ArrayList<RecipeView> m_recipeViews;
    
    public LightCraftView(final int refCraftId, final int ingredientId) {
        super(refCraftId);
    }
    
    public void createRecipeViews(final int ingredientId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_recipeViews = new ArrayList<RecipeView>();
        final Iterator<CraftRecipe> it = CraftManager.INSTANCE.getCraft(this.m_refCraftId).ingredientsToRecipeIterator(ingredientId);
        if (it == null) {
            return;
        }
        while (it.hasNext()) {
            final CraftRecipe craftRecipe = it.next();
            if (craftRecipe.hasProperty(CraftRecipeProperty.SECRET) && !localPlayer.getCraftHandler().isKnownRecipe(this.m_refCraftId, craftRecipe.getId())) {
                continue;
            }
            final RecipeView view = new RecipeView(craftRecipe, this);
            this.m_recipeViews.add(view);
        }
    }
    
    public boolean hasRecipesWithIngredient() {
        return this.m_recipeViews != null && this.m_recipeViews.size() != 0;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("recipes")) {
            return this.m_recipeViews;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public short getLevel() {
        return 100;
    }
}
