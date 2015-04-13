package com.ankamagames.wakfu.common.game.craft.reference;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class CraftRecipe
{
    private static final Logger m_logger;
    private static final double SUCCESS_RATE_DELTA_LEVEL_MAX = 10.0;
    public static final byte REGULAR_RECIPE = 0;
    private final int m_id;
    private final int m_craftId;
    private final short m_requiredLevel;
    private final long m_craftDuration;
    private final int m_xpRatio;
    private final SimpleCriterion m_criterion;
    private final SimpleCriterion m_visibilityCriterion;
    private final EnumSet<CraftRecipeProperty> m_properties;
    protected final TIntShortHashMap m_ingredients;
    private final ArrayList<RecipeResultItem> m_resultItems;
    private final int[] m_machineIds;
    
    public CraftRecipe(final int id, final int craftId, final short requiredLevel, final long craftDuration, final int xpRatio, final SimpleCriterion criterion, final SimpleCriterion visibilityCriterion, final EnumSet<CraftRecipeProperty> properties, final int[] machineIds) {
        super();
        this.m_ingredients = new TIntShortHashMap();
        this.m_resultItems = new ArrayList<RecipeResultItem>();
        this.m_id = id;
        this.m_craftId = craftId;
        this.m_requiredLevel = requiredLevel;
        this.m_craftDuration = craftDuration;
        this.m_xpRatio = xpRatio;
        this.m_criterion = criterion;
        this.m_visibilityCriterion = visibilityCriterion;
        this.m_properties = ((properties != null) ? properties : EnumSet.noneOf(CraftRecipeProperty.class));
        this.m_machineIds = machineIds;
    }
    
    public boolean isValid(final BasicCharacterInfo user) {
        return this.m_criterion == null || this.m_criterion.isValid(user, user, this, user.getAppropriateContext());
    }
    
    public boolean isVisible(final BasicCharacterInfo user) {
        return this.m_visibilityCriterion == null || this.m_visibilityCriterion.isValid(user, user, this, user.getAppropriateContext());
    }
    
    public byte getType() {
        return 0;
    }
    
    public void addProperty(final CraftRecipeProperty prop) {
        this.m_properties.add(prop);
    }
    
    public void addProperties(final Collection<CraftRecipeProperty> props) {
        this.m_properties.addAll((Collection<?>)props);
    }
    
    public void removeProperty(final CraftRecipeProperty prop) {
        this.m_properties.remove(prop);
    }
    
    public void removeProperties(final Collection<CraftRecipeProperty> props) {
        this.m_properties.removeAll(props);
    }
    
    public boolean hasProperty(final CraftRecipeProperty prop) {
        return this.m_properties.contains(prop);
    }
    
    public Iterator<CraftRecipeProperty> getProperties() {
        return this.m_properties.iterator();
    }
    
    public int[] getMachineIds() {
        return this.m_machineIds;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public short getRequiredLevel() {
        return this.m_requiredLevel;
    }
    
    public int getNbResultItems() {
        return this.m_resultItems.size();
    }
    
    public long getCraftDuration() {
        return this.m_craftDuration;
    }
    
    public int getXpRatio() {
        return this.m_xpRatio;
    }
    
    public long getCraftRealDuration(final BasicCharacterInfo owner) {
        final int quicknessPercent = owner.getSkillCharacteristics().getCraftCharacteristicEfficiency(CraftSkillType.CRAFT_QUICKNESS, this.m_craftId) + owner.getCharacteristicValue(FighterCharacteristicType.OCCUPATION_CRAFT_QUICKNESS);
        final double speed = 1.0 / this.m_craftDuration;
        final double factor = 1.0 + quicknessPercent / 100.0;
        final double newSpeed = speed * factor;
        return Math.round(1.0 / newSpeed);
    }
    
    public RecipeResultItem getEffectiveItem(final short playerLevel) {
        if (this.m_resultItems.isEmpty()) {
            return RecipeResultItem.NULL_ITEM;
        }
        final double bestDropRate = CraftDistribution.getMean(playerLevel, this);
        return this.m_resultItems.get((int)Math.round(bestDropRate));
    }
    
    public double getSuccessRate(final short playerLevel) {
        final double deltaLevel = MathHelper.clamp(this.m_requiredLevel - playerLevel, 0.0, 10.0);
        final double percent = deltaLevel / 10.0;
        return 1.0 - percent;
    }
    
    public long getXPGain(final short playerLevel, final WakfuAccountInformationHolder wakfuAccountInformationHolder) {
        return CraftXPUtil.getXPGain(playerLevel, this.m_requiredLevel, CraftManager.INSTANCE.getCraft(this.m_craftId), this.m_xpRatio, wakfuAccountInformationHolder);
    }
    
    public RecipeResultItem[] getResultItems() {
        return this.m_resultItems.toArray(new RecipeResultItem[this.m_resultItems.size()]);
    }
    
    public RecipeResultItem getResultItem(final short playerLevel) {
        final Distribution distribution = new CraftDistribution(playerLevel, this);
        return this.m_resultItems.get((int)Math.round(distribution.nextDouble()));
    }
    
    public TIntShortIterator ingredientsIterator() {
        return this.m_ingredients.iterator();
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public boolean containsIngredient(final int refItemId) {
        return this.m_ingredients.containsKey(refItemId);
    }
    
    public void putIngredient(final int itemId, final short quantity) {
        this.m_ingredients.put(itemId, quantity);
    }
    
    @Override
    public String toString() {
        return "CraftRecipe{m_id=" + this.m_id + ", m_craftId=" + this.m_craftId + ", m_requiredLevel=" + this.m_requiredLevel + ", m_craftDuration=" + this.m_craftDuration + '}';
    }
    
    public boolean containsResultItem(final int itemId) {
        for (int i = 0, size = this.m_resultItems.size(); i < size; ++i) {
            if (this.m_resultItems.get(i).getItemId() == itemId) {
                return true;
            }
        }
        return false;
    }
    
    public void addResultItem(final int itemId, final short quantity) {
        final RecipeResultItem resultItem = new RecipeResultItem(itemId, quantity);
        if (this.m_resultItems.contains(resultItem)) {
            CraftRecipe.m_logger.warn((Object)("Duplication d'item r\u00e9sultant " + itemId + " sur la recette " + this.getId()));
            return;
        }
        this.m_resultItems.add(resultItem);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CraftRecipe.class);
    }
}
