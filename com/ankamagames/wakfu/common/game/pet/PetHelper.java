package com.ankamagames.wakfu.common.game.pet;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class PetHelper
{
    public static boolean isHealMeal(final Pet pet, final int mealRefId) {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(mealRefId);
        final AbstractItemAction itemAction = referenceItem.getItemAction();
        return (itemAction != null && itemAction.getType() == ItemActionConstants.PET_HP) || pet.getDefinition().containsHealItem(mealRefId);
    }
    
    public static boolean isValidMeal(final Pet pet, final int mealRefId) {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(mealRefId);
        final AbstractItemAction itemAction = referenceItem.getItemAction();
        return (itemAction != null && itemAction.getType() == ItemActionConstants.PET_XP) || pet.getDefinition().containsMeal(mealRefId);
    }
    
    public static boolean isFullLife(final Pet pet) {
        return pet.getDefinition().getHealth() <= pet.getHealth();
    }
    
    public static boolean isPetMaxLevel(final Pet pet) {
        return pet.getMaxLevel() == pet.getLevel();
    }
    
    public static boolean isPetFedTooEarly(final Pet pet, final GameDateConst now) {
        final GameDate normalizedLastMeal = new GameDate(pet.getLastMealDate());
        normalizedLastMeal.trimToDay();
        final GameInterval mealInterval = normalizedLastMeal.timeTo(now);
        if (pet.isSleeping()) {
            final GameInterval sleepInterval = pet.getSleepDate().timeTo(now);
            mealInterval.substract(sleepInterval);
        }
        return !mealInterval.greaterThan(pet.getDefinition().getMealMinInterval());
    }
}
