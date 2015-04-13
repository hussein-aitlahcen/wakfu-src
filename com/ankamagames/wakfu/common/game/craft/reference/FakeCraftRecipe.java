package com.ankamagames.wakfu.common.game.craft.reference;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.account.*;

public class FakeCraftRecipe extends CraftRecipe
{
    public static final RecipeResultItem NULL_RESULT_ITEM;
    public static final byte FAKE_CRAFT_RECIPE_ID = -1;
    
    public FakeCraftRecipe(final int craftId, final short requiredLevel) {
        super(-1, craftId, requiredLevel, 10000L, 100, null, null, null, null);
    }
    
    @Override
    public byte getType() {
        return -1;
    }
    
    @Override
    public void addProperty(final CraftRecipeProperty prop) {
        throw new UnsupportedOperationException("Impossible de rajouter une propri\u00e9t\u00e9 sur une SecretRecipe");
    }
    
    @Override
    public void addProperties(final Collection<CraftRecipeProperty> props) {
        throw new UnsupportedOperationException("Impossible de rajouter une propri\u00e9t\u00e9 sur une SecretRecipe");
    }
    
    @Override
    public void removeProperty(final CraftRecipeProperty prop) {
        throw new UnsupportedOperationException("Impossible de rajouter une propri\u00e9t\u00e9 sur une SecretRecipe");
    }
    
    @Override
    public void removeProperties(final Collection<CraftRecipeProperty> props) {
        throw new UnsupportedOperationException("Impossible de rajouter une propri\u00e9t\u00e9 sur une SecretRecipe");
    }
    
    @Override
    public double getSuccessRate(final short playerLevel) {
        return 1.0;
    }
    
    @Override
    public long getXPGain(final short playerLevel, final WakfuAccountInformationHolder wakfuAccountInformationHolder) {
        return 0L;
    }
    
    @Override
    public RecipeResultItem getResultItem(final short playerLevel) {
        return FakeCraftRecipe.NULL_RESULT_ITEM;
    }
    
    public void addIngredients(final int refId, final short quantity) {
        this.m_ingredients.put(refId, quantity);
    }
    
    static {
        NULL_RESULT_ITEM = new RecipeResultItem(-1, (short)0);
    }
}
