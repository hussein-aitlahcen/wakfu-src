package com.ankamagames.wakfu.common.game.pet.definition;

import com.ankamagames.wakfu.common.game.mount.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.graphics.image.*;
import gnu.trove.*;

public class PetDefinition
{
    private final int m_id;
    private final String m_gfxId;
    private final MountType m_mountType;
    private final int m_health;
    private final TObjectIntHashMap<HealthPenaltyType> m_healthPenalties;
    private final IntIntLightWeightMap m_healItemRefIds;
    private final IntObjectLightWeightMap<GameIntervalConst> m_sleepItemRefIds;
    private final PetDefinitionMeal m_petDefinitionMeal;
    private final TIntHashSet m_equipmentItemRefIds;
    private final IntObjectLightWeightMap<PetDefinitionColor> m_colorItemRefIds;
    private final int m_baseColorItemRefId;
    private final int m_baseReskinItemRefId;
    private final TIntObjectHashMap<String> m_reskinItems;
    private final short m_xpPerLevel;
    private final short m_maxLevel;
    
    public PetDefinition(final int id, final String gfxId, final MountType mountType, final int baseColorItemRefId, final int baseReskinItemRefId, final int health, final long mealMinInterval, final long mealMaxInterval, final byte xpPerMeal, final short xpPerLevel, final short maxLevel) {
        super();
        this.m_healthPenalties = new TObjectIntHashMap<HealthPenaltyType>();
        this.m_healItemRefIds = new IntIntLightWeightMap();
        this.m_sleepItemRefIds = new IntObjectLightWeightMap<GameIntervalConst>();
        this.m_equipmentItemRefIds = new TIntHashSet();
        this.m_colorItemRefIds = new IntObjectLightWeightMap<PetDefinitionColor>();
        this.m_reskinItems = new TIntObjectHashMap<String>();
        this.m_id = id;
        this.m_gfxId = gfxId;
        this.m_mountType = mountType;
        this.m_baseColorItemRefId = baseColorItemRefId;
        this.m_baseReskinItemRefId = baseReskinItemRefId;
        this.m_health = health;
        this.m_petDefinitionMeal = new PetDefinitionMeal(mealMinInterval, mealMaxInterval, xpPerMeal);
        this.m_xpPerLevel = xpPerLevel;
        this.m_maxLevel = maxLevel;
    }
    
    public void addReskinItem(final int itemRefId, final String gfxId) {
        this.m_reskinItems.put(itemRefId, gfxId);
    }
    
    public boolean containsReskinItem(final int itemId) {
        return this.m_reskinItems.containsKey(itemId);
    }
    
    public String getReskinItemGfxId(final int itemId) {
        return this.m_reskinItems.get(itemId);
    }
    
    public int addHealthPenalty(final HealthPenaltyType type, final int value) {
        return this.m_healthPenalties.put(type, value);
    }
    
    public int getHealthPenalty(final HealthPenaltyType type) {
        return this.m_healthPenalties.get(type);
    }
    
    public void addHealItem(final int itemRefId, final int value) {
        this.m_healItemRefIds.put(itemRefId, value);
    }
    
    public boolean containsHealItem(final int itemRefId) {
        return this.m_healItemRefIds.contains(itemRefId);
    }
    
    public int getHealItemBonus(final int itemRefId) {
        return this.m_healItemRefIds.get(itemRefId);
    }
    
    public int[] getHealItemsRefIds() {
        return this.m_healItemRefIds.keys();
    }
    
    public void addSleepItem(final int itemRefId, final GameIntervalConst value) {
        this.m_sleepItemRefIds.put(itemRefId, value);
    }
    
    public boolean containsSleepItem(final int itemRefId) {
        return this.m_sleepItemRefIds.contains(itemRefId);
    }
    
    public GameIntervalConst getSleepItemInterval(final int itemRefId) {
        return this.m_sleepItemRefIds.get(itemRefId);
    }
    
    public void addMeal(final int itemRefId, final boolean visible) {
        this.m_petDefinitionMeal.addMeal(itemRefId, visible);
    }
    
    public boolean addEquipment(final int itemRefId) {
        return this.m_equipmentItemRefIds.add(itemRefId);
    }
    
    public void addColor(final int itemRefId, final int partIdx, final int aBGRColor) {
        PetDefinitionColor colorDef = this.m_colorItemRefIds.get(itemRefId);
        if (colorDef == null) {
            this.m_colorItemRefIds.put(itemRefId, colorDef = new PetDefinitionColor());
        }
        colorDef.add(partIdx, new Color(aBGRColor));
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getGfxId() {
        return this.m_gfxId;
    }
    
    public MountType getMountType() {
        return this.m_mountType;
    }
    
    public boolean hasMount() {
        return this.m_mountType != MountType.NONE;
    }
    
    public int getBaseColorItemRefId() {
        return this.m_baseColorItemRefId;
    }
    
    public int getBaseReskinItemRefId() {
        return this.m_baseReskinItemRefId;
    }
    
    public PetDefinitionColor getColorDefinition(final int colorItemRefId) {
        return this.m_colorItemRefIds.get(colorItemRefId);
    }
    
    public int getHealth() {
        return this.m_health;
    }
    
    public GameIntervalConst getMealMinInterval() {
        return this.m_petDefinitionMeal.getMealMinInterval();
    }
    
    public GameIntervalConst getMealMaxInterval() {
        return this.m_petDefinitionMeal.getMealMaxInterval();
    }
    
    public byte getXpPerMeal() {
        return this.m_petDefinitionMeal.getXpPerMeal();
    }
    
    public boolean containsMeal(final int itemRefId) {
        return this.m_petDefinitionMeal.containsMeal(itemRefId);
    }
    
    public boolean foreachVisibleMeal(final TIntProcedure procedure) {
        return this.m_petDefinitionMeal.foreachVisibleMeal(procedure);
    }
    
    public boolean containsEquipment(final int itemRefId) {
        return this.m_equipmentItemRefIds.contains(itemRefId);
    }
    
    public boolean containsColorItem(final int itemRefId) {
        return this.m_colorItemRefIds.contains(itemRefId);
    }
    
    public short getXpPerLevel() {
        return this.m_xpPerLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public int getRequiredXpForLevel(final short level) {
        return Math.min(this.m_maxLevel, level) * this.m_xpPerLevel;
    }
    
    @Override
    public String toString() {
        return "PetDefinition{m_id=" + this.m_id + '}';
    }
    
    int[] getEquipmentItemIds() {
        final TIntHashSet set = new TIntHashSet();
        for (int i = 0, size = this.m_colorItemRefIds.size(); i < size; ++i) {
            set.add(this.m_colorItemRefIds.getQuickKey(i));
        }
        set.addAll(this.m_equipmentItemRefIds.toArray());
        return set.toArray();
    }
}
