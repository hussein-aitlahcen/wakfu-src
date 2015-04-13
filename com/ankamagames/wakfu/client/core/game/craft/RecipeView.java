package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import gnu.trove.*;

public class RecipeView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_FIELD = "level";
    public static final String QUANTITY_TEXT_FIELD = "quantityText";
    public static final String LEVEL_SHORT_FIELD = "levelShort";
    public static final String INGREDIENTS_FIELD = "ingredients";
    public static final String PRODUCTION_PERCENTAGE_FIELD = "productionPercentage";
    public static final String PRODUCTION_PERCENTAGE_SHORT_FIELD = "productionPercentageShort";
    public static final String IS_SECRET_RECIPE_FIELD = "isSecretRecipe";
    public static final String HAS_CRITERION_FIELD = "hasCriterion";
    public static final String CRITERION_DESCRIPTION_FIELD = "criterionDescription";
    public static final String OBSOLETE_FIELD = "obsolete";
    public static final String CAN_AFFORD_FIELD = "canAfford";
    public static final String CAN_USE_AS_INGREDIENT = "canUseAsIngredient";
    public static final String MACHINES = "machines";
    public static final String[] FIELDS;
    private final CraftRecipe m_craftRecipe;
    private final AbstractCraftView m_craftView;
    private static final Color VERY_HARD_RECIPE_COLOR;
    private static final Color HARD_RECIPE_COLOR;
    private static final Color MEDIUM_RECIPE_COLOR;
    private boolean m_canAfford;
    private boolean m_canUseAsIngredient;
    
    public RecipeView(final CraftRecipe craftRecipe, final AbstractCraftView craftView) {
        super();
        this.m_canAfford = false;
        this.m_craftRecipe = craftRecipe;
        this.m_craftView = craftView;
        this.m_canUseAsIngredient = this.hasCraftUsingAnyIngredient();
    }
    
    private boolean hasCraftUsingAnyIngredient() {
        final RecipeResultItem[] resultItems = this.m_craftRecipe.getResultItems();
        final int[] itemIds = new int[resultItems.length];
        for (int i = 0; i < resultItems.length; ++i) {
            itemIds[i] = resultItems[i].getItemId();
        }
        return CraftManager.INSTANCE.hasCraftUsingAnyIngredient(itemIds);
    }
    
    private static boolean craftUseAnyIngredient(final ReferenceCraft craft, final RecipeResultItem[] resultItems) {
        for (final RecipeResultItem item : resultItems) {
            if (craft.ingredientsToRecipeIterator(item.getItemId()) != null) {
                return false;
            }
        }
        return true;
    }
    
    public void setCanAfford(final boolean canAfford) {
        this.m_canAfford = canAfford;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "canAfford");
    }
    
    @Override
    public String[] getFields() {
        return RecipeView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("level")) {
            final String levelString = new TextWidgetFormater().b().append(this.getRequiredLevel())._b().finishAndToString();
            return WakfuTranslator.getInstance().getString("craft.recipeLevel", levelString);
        }
        if (fieldName.equals("levelShort")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.getRequiredLevel());
        }
        if (fieldName.equals("quantityText")) {
            final short itemQuantity = this.getEffectiveItemQuantity();
            return (itemQuantity == 1) ? "" : ("x" + itemQuantity);
        }
        if (fieldName.equals("ingredients")) {
            return this.getIngredients();
        }
        if (fieldName.equals("productionPercentage")) {
            final long percentage = Math.round(this.m_craftRecipe.getSuccessRate(this.m_craftView.getLevel()) * 100.0);
            final TextWidgetFormater sb = new TextWidgetFormater();
            Color colorToUse = Color.WHITE;
            if (percentage < 100L) {
                if (percentage > 50L) {
                    colorToUse = RecipeView.MEDIUM_RECIPE_COLOR;
                }
                else if (percentage > 25L) {
                    colorToUse = RecipeView.HARD_RECIPE_COLOR;
                }
                else {
                    colorToUse = RecipeView.VERY_HARD_RECIPE_COLOR;
                }
            }
            final String percentageString = sb.b().addColor(colorToUse.getRGBtoHex()).append(percentage)._b().finishAndToString();
            return WakfuTranslator.getInstance().getString("craft.recipeChance", percentageString);
        }
        if (fieldName.equals("productionPercentageShort")) {
            final long percentage = Math.round(this.m_craftRecipe.getSuccessRate(this.m_craftView.getLevel()) * 100.0);
            final TextWidgetFormater sb = new TextWidgetFormater();
            Color colorToUse = Color.WHITE;
            if (percentage < 100L) {
                if (percentage >= 50L) {
                    colorToUse = RecipeView.HARD_RECIPE_COLOR;
                }
                else {
                    colorToUse = RecipeView.VERY_HARD_RECIPE_COLOR;
                }
            }
            sb.openText().addColor(colorToUse.getRGBtoHex()).append(percentage + "%").closeText();
            return sb.finishAndToString();
        }
        if (fieldName.equals("isSecretRecipe")) {
            return this.m_craftRecipe.hasProperty(CraftRecipeProperty.SECRET);
        }
        if (fieldName.equals("hasCriterion")) {
            return this.m_craftRecipe.getCriterion() != null;
        }
        if (fieldName.equals("criterionDescription")) {
            final SimpleCriterion criterion = this.m_craftRecipe.getCriterion();
            if (criterion == null) {
                return null;
            }
            final TextWidgetFormater twf = new TextWidgetFormater();
            final boolean valid = this.m_craftRecipe.isValid(WakfuGameEntity.getInstance().getLocalPlayer());
            twf.openText().addColor((valid ? Color.GREEN : Color.RED).getRGBtoHex());
            twf.append(CriterionDescriptionGenerator.getDescription(criterion));
            twf.closeText();
            return twf.finishAndToString();
        }
        else {
            if (fieldName.equals("canAfford")) {
                return this.m_canAfford;
            }
            if (fieldName.equals("canUseAsIngredient")) {
                return this.m_canUseAsIngredient;
            }
            if (fieldName.equals("obsolete")) {
                final ReferenceCraft craft = CraftManager.INSTANCE.getCraft(this.m_craftView.getCraftReferenceId());
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final short craftLevel = localPlayer.getCraftHandler().getLevel(this.m_craftView.getCraftReferenceId());
                return CraftXPUtil.getXPGain(craftLevel, this.getRequiredLevel(), craft, localPlayer) == 0L;
            }
            if (fieldName.equals("machines")) {
                return this.getMachines();
            }
            final RefItemFieldProvider item = (RefItemFieldProvider)this.getEffectiveItem();
            return (item != null) ? item.getReferenceItemDisplayer().getFieldValue(fieldName) : null;
        }
    }
    
    public short getRequiredLevel() {
        return this.m_craftRecipe.getRequiredLevel();
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(46, this.m_craftRecipe.getId(), new Object[0]);
    }
    
    public ArrayList<IngredientView> getIngredients() {
        final ArrayList<IngredientView> ingredientViews = new ArrayList<IngredientView>();
        final TIntShortIterator it = this.m_craftRecipe.ingredientsIterator();
        while (it.hasNext()) {
            it.advance();
            final MetaItem metaItem = MetaItemManager.INSTANCE.get(it.key());
            final int itemId = (metaItem != null) ? metaItem.getFirstSubId() : it.key();
            ingredientViews.add(new IngredientView(it.value(), ReferenceItemManager.getInstance().getReferenceItem(itemId)));
        }
        return ingredientViews;
    }
    
    public CraftRecipe getCraftRecipe() {
        return this.m_craftRecipe;
    }
    
    public AbstractReferenceItem getEffectiveItem() {
        final RecipeResultItem effectiveItem = this.m_craftRecipe.getEffectiveItem(this.m_craftView.getLevel());
        if (effectiveItem == RecipeResultItem.NULL_ITEM) {
            return null;
        }
        final MetaItem metaItem = MetaItemManager.INSTANCE.get(effectiveItem.getItemId());
        return (metaItem == null) ? ReferenceItemManager.getInstance().getReferenceItem(effectiveItem.getItemId()) : metaItem;
    }
    
    public short getEffectiveItemQuantity() {
        return this.m_craftRecipe.getEffectiveItem(this.m_craftView.getLevel()).getQuantity();
    }
    
    public String getMachines() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final int[] machineIds = this.m_craftRecipe.getMachineIds();
        for (int i = 0, size = machineIds.length; i < size; ++i) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(WakfuTranslator.getInstance().getString(59, machineIds[i], new Object[0]));
        }
        return sb.finishAndToString();
    }
    
    static {
        FIELDS = new String[] { "name", "level", "quantityText", "ingredients", "productionPercentage", "hasCriterion", "isSecretRecipe", "obsolete", "canAfford" };
        VERY_HARD_RECIPE_COLOR = new Color(0.8f, 0.0f, 0.0f, 1.0f);
        HARD_RECIPE_COLOR = new Color(0.8f, 0.4f, 0.0f, 1.0f);
        MEDIUM_RECIPE_COLOR = new Color(0.8f, 0.8f, 0.0f, 1.0f);
    }
}
