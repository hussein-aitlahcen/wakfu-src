package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import com.ankamagames.wakfu.common.game.xp.modifications.*;
import gnu.trove.*;

public final class AptitudeBonusInventoryController
{
    private final AptitudeBonusInventory m_bonusInventory;
    
    public AptitudeBonusInventoryController(final AptitudeBonusInventory bonusInventory) {
        super();
        this.m_bonusInventory = bonusInventory;
    }
    
    public TIntShortHashMap applyModifications(final TIntShortHashMap aptitudeModifications) {
        final TIntShortHashMap results = new TIntShortHashMap();
        aptitudeModifications.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int bonusId, final short levelDiff) {
                if (!AptitudeBonusInventoryController.this.m_bonusInventory.hasPointsAvailableFor(bonusId, levelDiff)) {
                    return true;
                }
                if (levelDiff < 0) {
                    return true;
                }
                final AptitudeBonusModel bonus = AptitudeBonusModelManager.INSTANCE.get(bonusId);
                final short currentLevel = AptitudeBonusInventoryController.this.m_bonusInventory.getLevel(bonusId);
                if (bonus.getMax() != 0 && currentLevel + levelDiff > bonus.getMax()) {
                    return true;
                }
                AptitudeBonusInventoryController.this.m_bonusInventory.addLevel(bonusId, levelDiff);
                AptitudeBonusInventoryController.this.m_bonusInventory.removePointsFor(bonusId, levelDiff);
                results.put(bonusId, levelDiff);
                return true;
            }
        });
        return results;
    }
    
    public void givePointsForLevelUp(final XpModification xpModification, final short newLevel) {
        if (!xpModification.doesLevelUp()) {
            return;
        }
        final short levelDifference = xpModification.getLevelDifference();
        final int previousLevel = newLevel - levelDifference;
        int i;
        for (int firstNewLevel = i = previousLevel + 1; i <= newLevel; ++i) {
            final TIntHashSet activatedCategories = AptitudeCategoryModelManager.INSTANCE.getActivatedCategoriesAtLevel((short)i);
            activatedCategories.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int categoryId) {
                    AptitudeBonusInventoryController.this.m_bonusInventory.incPointFor(categoryId);
                    return true;
                }
            });
        }
    }
    
    public void resetInventoryAndGivePointsBackIfNecessary(final short level) {
        if (this.m_bonusInventory.getTotalPoints() > 0) {
            return;
        }
        this.resetInventoryAndGivePointsBack(level);
    }
    
    public void resetInventoryAndGivePointsBack(final short level) {
        this.m_bonusInventory.clear();
        this.givePointsToLevel(level);
    }
    
    public void givePointsToLevel(final short level) {
        AptitudeCategoryModelManager.INSTANCE.forEachCategory(new TObjectProcedure<AptitudeCategoryModel>() {
            @Override
            public boolean execute(final AptitudeCategoryModel categoryModel) {
                final int indexOfActivationLevel = categoryModel.getIndexOfActivationLevel(level);
                AptitudeBonusInventoryController.this.m_bonusInventory.addPointsFor(categoryModel.getId(), (short)indexOfActivationLevel);
                return true;
            }
        });
    }
    
    @Override
    public String toString() {
        return "AptitudeBonusInventoryController{m_bonusInventory=" + this.m_bonusInventory + '}';
    }
}
