package com.ankamagames.wakfu.common.game.aptitude;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;

public final class ReferenceAptitude implements Comparable<ReferenceAptitude>
{
    private static final Logger m_logger;
    private final short m_referenceId;
    private final int m_uiId;
    private final THashSet<Breed> m_breed;
    private final FighterCharacteristicType m_characteristic;
    private final int m_linkedSpellId;
    private final int m_aptitudeGfxId;
    private final int m_spellGfxId;
    private final ArrayList<WakfuEffect> m_effects;
    private final short m_maxLevel;
    private final int m_constantCost;
    private final TIntArrayList m_variableCost;
    private final TIntArrayList m_variableCostLevel;
    private final AptitudeType m_type;
    private final TIntArrayList m_levelUnlock;
    private final TIntArrayList m_levelUnlockLevel;
    
    public ReferenceAptitude(final short referenceId, final int uiId, final int[] breedIds, final FighterCharacteristicType characteristicType, final int linkedSpellId, final int aptitudeGfxId, final int spellGfxId, final short maxLevel, final int constantCost, final int[] variableCosts, final int[] levelUnlock, final AptitudeType type) {
        super();
        this.m_breed = new THashSet<Breed>();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_referenceId = referenceId;
        this.m_uiId = uiId;
        this.m_linkedSpellId = linkedSpellId;
        this.m_aptitudeGfxId = aptitudeGfxId;
        this.m_spellGfxId = spellGfxId;
        this.m_maxLevel = maxLevel;
        this.m_constantCost = constantCost;
        for (final int breedId : breedIds) {
            final AvatarBreed breed = AvatarBreed.getBreedFromId(breedId);
            if (breed != null) {
                this.m_breed.add(breed);
            }
            else {
                ReferenceAptitude.m_logger.warn((Object)("Breed Id inconnue : " + breedId));
            }
        }
        this.m_characteristic = characteristicType;
        this.m_variableCost = new TIntArrayList();
        this.m_variableCostLevel = new TIntArrayList();
        this.initVariableCosts(variableCosts);
        this.m_levelUnlock = new TIntArrayList();
        this.m_levelUnlockLevel = new TIntArrayList();
        this.initLevelUnlocks(levelUnlock);
        this.m_type = type;
    }
    
    private void initVariableCosts(final int[] costs) {
        if (costs.length % 2 != 0) {
            return;
        }
        for (int i = 0, size = costs.length / 2; i < size; ++i) {
            this.m_variableCostLevel.add(costs[i * 2]);
            this.m_variableCost.add(costs[i * 2 + 1]);
        }
    }
    
    private void initLevelUnlocks(final int[] unlocks) {
        if (unlocks.length % 2 != 0) {
            return;
        }
        for (int i = 0, size = unlocks.length / 2; i < size; ++i) {
            this.m_levelUnlockLevel.add(unlocks[i * 2]);
            this.m_levelUnlock.add(unlocks[i * 2 + 1]);
        }
    }
    
    public short getReferenceId() {
        return this.m_referenceId;
    }
    
    public int getUiId() {
        return this.m_uiId;
    }
    
    public boolean hasBreed(final Breed breed) {
        return this.m_breed.contains(AvatarBreed.COMMON) || this.m_breed.contains(breed);
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    public List<WakfuEffect> getEffects() {
        return this.m_effects;
    }
    
    public int getPointsForLevel(final short level) {
        if (level < 0 || level > this.m_maxLevel) {
            return -1;
        }
        if (level == 0) {
            return 0;
        }
        return this.m_constantCost + this.getVariableCostForLevel(level);
    }
    
    private int getVariableCostForLevel(final short level) {
        if (this.m_variableCost.isEmpty()) {
            return 0;
        }
        int value = 0;
        for (int i = 0, size = this.m_variableCostLevel.size(); i < size && this.m_variableCostLevel.get(i) <= level; ++i) {
            value = this.m_variableCost.get(i);
        }
        return value;
    }
    
    public int getLevelUnlock(final short level) {
        if (level < 0 || level > this.m_maxLevel) {
            return -1;
        }
        int value = 0;
        for (int i = 0, size = this.m_levelUnlockLevel.size(); i < size && this.m_levelUnlockLevel.get(i) <= level; ++i) {
            value = this.m_levelUnlock.get(i);
        }
        return value;
    }
    
    public int getAptitudeGfxId() {
        return this.m_aptitudeGfxId;
    }
    
    public int getSpellGfxId() {
        return this.m_spellGfxId;
    }
    
    public FighterCharacteristicType getCharacteristic() {
        return this.m_characteristic;
    }
    
    public int getLinkedSpellId() {
        return this.m_linkedSpellId;
    }
    
    public AptitudeType getType() {
        return this.m_type;
    }
    
    @Override
    public int compareTo(final ReferenceAptitude o) {
        return this.m_uiId - o.m_uiId;
    }
    
    @Override
    public String toString() {
        return "ReferenceAptitude{m_referenceId=" + this.m_referenceId + ", m_breed=" + this.m_breed + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceAptitude.class);
    }
}
