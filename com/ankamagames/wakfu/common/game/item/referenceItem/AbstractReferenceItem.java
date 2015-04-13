package com.ankamagames.wakfu.common.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.xp.*;

public abstract class AbstractReferenceItem<T extends AbstractItemType<T>> implements BasicReferenceItem
{
    private int m_id;
    private short m_setId;
    private int m_gfxId;
    private int m_femaleGfxId;
    private int m_floorGfxId;
    private final GrowingArray<WakfuEffect> m_effects;
    private short m_level;
    private final EnumMap<ActionsOnItem, SimpleCriterion> m_criteria;
    private AbstractItemType<T> m_itemType;
    protected short m_stackMaximumHeight;
    private boolean m_usableInFight;
    private boolean m_usableInWorld;
    private byte m_useCostAP;
    private byte m_useCostMP;
    private byte m_useCostWP;
    private int m_useRangeMin;
    private int m_useRangeMax;
    private boolean m_useTestFreeCell;
    private boolean m_useTestNotBorderCell;
    private boolean m_useTestLOS;
    private boolean m_useOnlyInLine;
    private ItemRarity m_rarity;
    private ItemType m_type;
    private ItemBindType m_bindType;
    private final EnumSet<ItemProperty> m_itemProperties;
    private boolean m_canBeCritical;
    protected AbstractItemAction m_itemAction;
    private GemType m_gemType;
    private GemElementType m_gemElementType;
    private byte m_gemsNum;
    private int m_metaId;
    
    protected AbstractReferenceItem() {
        super();
        this.m_criteria = new EnumMap<ActionsOnItem, SimpleCriterion>(ActionsOnItem.class);
        this.m_itemProperties = EnumSet.noneOf(ItemProperty.class);
        this.m_effects = new GrowingArray<WakfuEffect>();
    }
    
    void setId(final int id) {
        this.m_id = id;
    }
    
    void setSetId(final short setId) {
        this.m_setId = setId;
    }
    
    void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    void setFemaleGfxId(final int gfxId) {
        this.m_femaleGfxId = gfxId;
    }
    
    void setFloorGfxId(final int floorGfxId) {
        this.m_floorGfxId = floorGfxId;
    }
    
    void setLevel(final short level) {
        this.m_level = level;
    }
    
    void setItemType(final AbstractItemType itemType) {
        this.checkItemType(itemType);
        this.m_itemType = (AbstractItemType<T>)itemType;
    }
    
    void setCriteria(final Map<ActionsOnItem, SimpleCriterion> criteria) {
        this.m_criteria.putAll(criteria);
    }
    
    void setStackMaximumHeight(final short stackMaximumHeight) {
        this.m_stackMaximumHeight = (short)Math.max(1, stackMaximumHeight);
    }
    
    void setUseCostAP(final byte useCostAP) {
        this.m_useCostAP = useCostAP;
    }
    
    void setUseCostMP(final byte useCostMP) {
        this.m_useCostMP = useCostMP;
    }
    
    void setUseCostWP(final byte useCostWP) {
        this.m_useCostWP = useCostWP;
    }
    
    void setUseRangeMin(final int useRangeMin) {
        this.m_useRangeMin = useRangeMin;
    }
    
    void setUseRangeMax(final int useRangeMax) {
        this.m_useRangeMax = useRangeMax;
    }
    
    void setUseTestFreeCell(final boolean useTestFreeCell) {
        this.m_useTestFreeCell = useTestFreeCell;
    }
    
    void setUseTestNotBorderCell(final boolean useTestNotBorderCell) {
        this.m_useTestNotBorderCell = useTestNotBorderCell;
    }
    
    void setUseTestLOS(final boolean useTestLOS) {
        this.m_useTestLOS = useTestLOS;
    }
    
    void setUseOnlyInLine(final boolean useOnlyInLine) {
        this.m_useOnlyInLine = useOnlyInLine;
    }
    
    void setRarity(final ItemRarity rarity) {
        this.m_rarity = rarity;
    }
    
    void setBindType(final ItemBindType type) {
        this.m_bindType = type;
    }
    
    void setType(final ItemType type) {
        this.m_type = type;
    }
    
    void setGemType(final GemType type) {
        this.m_gemType = type;
    }
    
    void setGemsNum(final byte gemsNum) {
        this.m_gemsNum = gemsNum;
    }
    
    void setGemElementType(final GemElementType gemElementType) {
        this.m_gemElementType = gemElementType;
    }
    
    public final void checkItemType(final AbstractItemType<T> itemType) throws IllegalArgumentException {
        if (itemType == null) {
            throw new IllegalArgumentException("m_itemType ne peut \u00eatre null pour l'objet " + this.m_id);
        }
    }
    
    public GrowingArray<WakfuEffect> getEffects() {
        return this.m_effects;
    }
    
    public Iterator<Map.Entry<ActionsOnItem, SimpleCriterion>> criteriaIterator() {
        return this.m_criteria.entrySet().iterator();
    }
    
    public abstract ItemMetaType getMetaType();
    
    public void addItemProperty(final ItemProperty prop) {
        this.m_itemProperties.add(prop);
    }
    
    public boolean hasItemProperty(final ItemProperty prop) {
        return this.m_itemProperties.contains(prop);
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
        if (effect.isUsableInFight()) {
            this.m_usableInFight = true;
        }
        if (effect.isUsableInWorld()) {
            this.m_usableInWorld = true;
        }
        if (effect.checkFlags(1L)) {
            this.m_canBeCritical = true;
        }
    }
    
    public boolean canBeCritical() {
        return this.m_canBeCritical;
    }
    
    public byte getActionPoints() {
        return this.m_useCostAP;
    }
    
    public SimpleCriterion getCriterion(final ActionsOnItem actionToPerform) {
        return this.m_criteria.get(actionToPerform);
    }
    
    public boolean isCriterionValid(final ActionsOnItem action, final BasicCharacterInfo user, final CraftRecipe recipe) {
        final SimpleCriterion crit = this.getCriterion(action);
        return crit == null || crit.isValid(user, user, recipe, user.getAppropriateContext());
    }
    
    public String getDescription() {
        throw new UnsupportedOperationException("M\u00e9thode non cod\u00e9e dans le refactor");
    }
    
    @Override
    public String getName() {
        throw new UnsupportedOperationException("M\u00e9thode non cod\u00e9e dans le refactor");
    }
    
    public Iterator<WakfuEffect> getEffectsIterator() {
        return this.m_effects.iterator();
    }
    
    public byte getWakfuPoints() {
        return this.m_useCostWP;
    }
    
    public byte getMovementPoints() {
        return this.m_useCostMP;
    }
    
    public short getSetId() {
        return this.m_setId;
    }
    
    public boolean hasToTestFreeCell() {
        return this.m_useTestFreeCell;
    }
    
    public boolean hasToTestLOS() {
        return this.m_useTestLOS;
    }
    
    public boolean hasToTestNotBorderCell() {
        return this.m_useTestNotBorderCell;
    }
    
    public boolean isEquipmentPositionValid(final EquipmentPosition position) {
        return this.m_itemType.isEquipmentPositionValid(position);
    }
    
    public boolean isTwoHandedWeapon() {
        final EquipmentPosition[] blockedPositions = this.m_itemType.getLinkedPositions();
        if (blockedPositions == null) {
            return false;
        }
        final EquipmentPosition[] equipPositions = this.m_itemType.getEquipmentPositions();
        return ArrayUtils.contains(equipPositions, EquipmentPosition.FIRST_WEAPON) && ArrayUtils.contains(blockedPositions, EquipmentPosition.SECOND_WEAPON);
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public int getFemaleGfxId() {
        return this.m_femaleGfxId;
    }
    
    public int getFloorGfxId() {
        return this.m_floorGfxId;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public AbstractItemType<T> getItemType() {
        return this.m_itemType;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public int getUseRangeMax() {
        return this.m_useRangeMax;
    }
    
    public int getUseRangeMin() {
        return this.m_useRangeMin;
    }
    
    public boolean isUsableInFight() {
        return this.m_usableInFight;
    }
    
    public boolean isUsableInWorld() {
        return this.m_usableInWorld;
    }
    
    public boolean isUseOnlyInLine() {
        return this.m_useOnlyInLine;
    }
    
    @Override
    public short getStackMaximumHeight() {
        return this.m_stackMaximumHeight;
    }
    
    public void setUsableInWorld(final boolean usableInWorld) {
        this.m_usableInWorld = usableInWorld;
    }
    
    public void addAction(final AbstractItemAction action) {
        this.m_itemAction = action;
    }
    
    @Nullable
    public AbstractItemAction getItemAction() {
        return this.m_itemAction;
    }
    
    public ItemRarity getRarity() {
        return this.m_rarity;
    }
    
    public ItemBindType getBindType() {
        return this.m_bindType;
    }
    
    public ItemType getType() {
        return this.m_type;
    }
    
    public boolean isChallengeItem() {
        return this.hasItemProperty(ItemProperty.CHALLENGE);
    }
    
    public boolean isTreasureItem() {
        return this.hasItemProperty(ItemProperty.TREASURE);
    }
    
    public boolean isEquipment() {
        return this.m_itemType.getEquipmentPositions().length > 0 || this.m_itemType.getId() == 218 || this.m_itemType.getId() == 399;
    }
    
    public boolean isCraftEnabled() {
        return false;
    }
    
    public int getItemXpDefinitionId() {
        if (this.hasItemProperty(ItemProperty.ADMIN_XP)) {
            return 2;
        }
        return ItemXpAGT.contains(this.m_id) ? 1 : 0;
    }
    
    public boolean hasItemXp() {
        return ItemXpDefinitionManager.INSTANCE.contains(this.getItemXpDefinitionId());
    }
    
    public boolean hasPet() {
        return PetDefinitionManager.INSTANCE.getFromRefItemId(this.m_id) != null;
    }
    
    public static boolean isVariableEffect(final boolean isMetaItem, final boolean activeParentType, final int effectActionId) {
        return isMetaItem && !activeParentType && effectActionId == RunningEffectConstants.VARIABLE_EFFECT.getId();
    }
    
    public short getMaxLevel() {
        final ItemXpDefinition definition = ItemXpDefinitionManager.INSTANCE.get(this.getItemXpDefinitionId());
        if (definition == null) {
            return this.m_level;
        }
        final ItemXp xp = ItemXpFactory.create(definition);
        return xp.getMaxLevel();
    }
    
    public boolean isGemmable() {
        return this.m_gemsNum > 0;
    }
    
    public GemType getGemType() {
        return this.m_gemType;
    }
    
    public byte getGemsNum() {
        return this.m_gemsNum;
    }
    
    public GemElementType getGemElementType() {
        return this.m_gemElementType;
    }
    
    public int getMetaId() {
        return this.m_metaId;
    }
    
    public void setMetaId(final int metaId) {
        this.m_metaId = metaId;
    }
    
    public boolean hasHealEffect() {
        final Iterator<WakfuEffect> effects = this.getEffectsIterator();
        while (effects.hasNext()) {
            final WakfuEffect effect = effects.next();
            if (effect.getActionId() == RunningEffectConstants.HP_GAIN.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasRandomElementEffect() {
        final Iterator<WakfuEffect> effects = this.getEffectsIterator();
        while (effects.hasNext()) {
            final int actionId = effects.next().getActionId();
            if (actionId == RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId() || actionId == RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public int countRandomElementEffectNeed(final int forActionId) {
        int count = 0;
        final Iterator<WakfuEffect> effects = this.getEffectsIterator();
        while (effects.hasNext()) {
            final WakfuEffect effect = effects.next();
            final int actionId = effect.getActionId();
            if (actionId == forActionId) {
                count += (int)effect.getParam(1);
            }
        }
        return count;
    }
    
    public abstract boolean canBeEquiped();
    
    @Override
    public String toString() {
        return "AbstractReferenceItem{m_id=" + this.m_id + ", m_setId=" + this.m_setId + ", m_level=" + this.m_level + '}';
    }
}
