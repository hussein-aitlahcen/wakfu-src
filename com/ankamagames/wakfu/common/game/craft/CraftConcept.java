package com.ankamagames.wakfu.common.game.craft;

import com.ankamagames.wakfu.common.datas.*;

public class CraftConcept extends Craft
{
    public CraftConcept(final int refId) {
        super(refId);
    }
    
    @Override
    public boolean addKnownRecipe(final int recipeId) {
        return false;
    }
    
    @Override
    boolean isKnownRecipe(final int recipeId) {
        return false;
    }
    
    @Override
    CharacterSerializedCraft.RawCraft toRaw() {
        return null;
    }
    
    @Override
    long getXp() {
        return 0L;
    }
    
    @Override
    long addQuickXp(final long toAdd) {
        return 0L;
    }
    
    @Override
    public void release() {
    }
}
