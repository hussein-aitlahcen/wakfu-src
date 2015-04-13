package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class CraftLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CraftBinaryData(), new LoadProcedure<CraftBinaryData>() {
            @Override
            public void load(final CraftBinaryData data) {
                try {
                    final ReferenceCraft craft = fromBinary(data);
                    CraftManager.INSTANCE.addCraft(craft);
                    final TIntObjectIterator<CraftRecipe> it = craft.recipesIterator();
                    while (it.hasNext()) {
                        it.advance();
                        for (final RecipeResultItem resultItem : it.value().getResultItems()) {
                            ((ItemManagerImpl)ReferenceItemManager.getInstance()).addItemCraftedId(resultItem.getItemId());
                        }
                    }
                }
                catch (Exception e) {
                    CraftLoader.m_logger.error((Object)("[GD] Exception au chargement du m\u00e9tier " + data.getCraftId()), (Throwable)e);
                }
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    private static ReferenceCraft fromBinary(final CraftBinaryData data) throws Exception {
        final ReferenceCraft craft = new ReferenceCraft(data.getCraftId(), data.getBookItemId(), data.getXpFactor(), data.isInnate(), data.isConceptualCraft(), data.isHiddenCraft());
        final TIntObjectHashMap<ArrayList<CraftRecipe>> ingredientsToRecipes = new TIntObjectHashMap<ArrayList<CraftRecipe>>();
        BinaryDocumentManager.getInstance().foreach(new RecipeBinaryData(), "craft_id", craft.getId(), new LoadProcedure<RecipeBinaryData>() {
            @Override
            public void load(final RecipeBinaryData recipeData) {
                try {
                    final CraftRecipe recipe = CraftLoader.recipeFromBinary(recipeData);
                    craft.putRecipe(recipeData.getId(), recipe);
                    final TIntShortIterator it = recipe.ingredientsIterator();
                    while (it.hasNext()) {
                        it.advance();
                        final int ingredientId = it.key();
                        ArrayList<CraftRecipe> recipes = ingredientsToRecipes.get(ingredientId);
                        if (recipes == null) {
                            recipes = new ArrayList<CraftRecipe>();
                            ingredientsToRecipes.put(ingredientId, recipes);
                        }
                        recipes.add(recipe);
                        if (!recipe.hasProperty(CraftRecipeProperty.SECRET)) {
                            IngredientManager.INSTANCE.addIngredientTo(ingredientId, craft.getId());
                        }
                    }
                }
                catch (Exception e) {
                    CraftLoader.m_logger.error((Object)"", (Throwable)e);
                }
            }
        });
        craft.setIngredientsToRecipes(ingredientsToRecipes);
        return craft;
    }
    
    public static CraftRecipe recipeFromBinary(final RecipeBinaryData recipeBs) throws Exception {
        final int[] properties = recipeBs.getProperties();
        final EnumSet<CraftRecipeProperty> set = EnumSet.noneOf(CraftRecipeProperty.class);
        for (int i = 0; i < properties.length; ++i) {
            final int propertyId = properties[i];
            final CraftRecipeProperty prop = CraftRecipeProperty.getProperty(propertyId);
            if (prop == null) {
                throw new Exception("Impossible de trouver la propri\u00e9t\u00e9 " + propertyId + " de la recette " + recipeBs.getId());
            }
            if (set.contains(prop)) {
                throw new Exception("Duplication de la propri\u00e9t\u00e9 " + propertyId + " sur la recette " + recipeBs.getId());
            }
            set.add(prop);
        }
        final CraftRecipe recipe = new CraftRecipe(recipeBs.getId(), recipeBs.getCategoryId(), (short)recipeBs.getLevel(), recipeBs.getDuration(), recipeBs.getXpRatio(), CriteriaCompiler.compileBoolean(recipeBs.getCriteria()), CriteriaCompiler.compileBoolean(recipeBs.getVisibilityCriteria()), set, recipeBs.getMachinesUsingRecipe());
        for (final RecipeBinaryData.RecipeIngredient ingredient : recipeBs.getIngredients()) {
            if (recipe.containsIngredient(ingredient.getItemId())) {
                throw new Exception("Duplication d'ingredient " + ingredient.getItemId() + " sur la recette " + recipeBs.getId());
            }
            final int ingredientItemId = ingredient.getItemId();
            recipe.putIngredient(ingredientItemId, ingredient.getQuantity());
        }
        for (final RecipeBinaryData.RecipeProduct product : recipeBs.getProducts()) {
            recipe.addResultItem(product.getItemId(), product.getQuantity());
        }
        return recipe;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.craft");
    }
    
    static {
        m_logger = Logger.getLogger((Class)CraftLoader.class);
    }
}
