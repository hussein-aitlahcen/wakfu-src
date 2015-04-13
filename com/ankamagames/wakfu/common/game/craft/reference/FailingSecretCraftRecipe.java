package com.ankamagames.wakfu.common.game.craft.reference;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.account.*;
import gnu.trove.*;

public class FailingSecretCraftRecipe extends CraftRecipe
{
    public static final RecipeResultItem NULL_RESULT_ITEM;
    public static final byte SECRET_FAILED_CRAFT_RECIPE_ID = -2;
    private final CraftRecipe m_secretRecipe;
    
    public FailingSecretCraftRecipe(final CraftRecipe recipe, final int craftId, final short requiredLevel) {
        super(-2, craftId, requiredLevel, recipe.getCraftDuration(), recipe.getXpRatio(), recipe.getCriterion(), null, EnumSet.noneOf(CraftRecipeProperty.class), null);
        this.m_secretRecipe = recipe;
    }
    
    @Override
    public int getId() {
        return this.m_secretRecipe.getId();
    }
    
    @Override
    public byte getType() {
        return -2;
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
        return FailingSecretCraftRecipe.NULL_RESULT_ITEM;
    }
    
    @Override
    public TIntShortIterator ingredientsIterator() {
        return this.m_secretRecipe.ingredientsIterator();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FailingSecretCraftRecipe");
        sb.append("{m_secretRecipe=").append(this.m_secretRecipe);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        NULL_RESULT_ITEM = new RecipeResultItem(-2, (short)0);
    }
}
