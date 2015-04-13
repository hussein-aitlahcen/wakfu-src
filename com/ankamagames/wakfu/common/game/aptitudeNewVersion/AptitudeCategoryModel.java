package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import gnu.trove.*;
import java.util.*;

public final class AptitudeCategoryModel
{
    private final int m_id;
    private final Set<AptitudeBonusModel> m_bonusSet;
    private final TIntArrayList m_activationLevels;
    
    public AptitudeCategoryModel(final int id, final TIntArrayList activationLevels) {
        super();
        this.m_bonusSet = new HashSet<AptitudeBonusModel>();
        this.m_id = id;
        (this.m_activationLevels = activationLevels).sort();
    }
    
    public void addBonus(final AptitudeBonusModel bonus) {
        this.m_bonusSet.add(bonus);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public <T extends AptitudeBonusModel> Set<T> getBonusSet() {
        return new HashSet<T>((Collection<? extends T>)this.m_bonusSet);
    }
    
    public boolean hasActivationLevel(final short level) {
        return this.m_activationLevels.contains(level);
    }
    
    public int getIndexOfActivationLevel(final short level) {
        final int size = this.m_activationLevels.size();
        for (int i = 0; i < size; ++i) {
            if (level < this.m_activationLevels.get(i)) {
                return i;
            }
        }
        return size;
    }
    
    public int getNextActivationLevel(final short currentLevel) {
        for (final int activationLevel : this.m_activationLevels.toNativeArray()) {
            if (activationLevel > currentLevel) {
                return activationLevel;
            }
        }
        return -1;
    }
    
    public int getFirstActivationLevel() {
        if (this.m_activationLevels.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        return this.m_activationLevels.get(0);
    }
    
    @Override
    public String toString() {
        return "AptitudeCategoryModel{m_id=" + this.m_id + ", m_bonusSet=" + this.m_bonusSet + ", m_activationLevels=" + this.m_activationLevels + '}';
    }
}
