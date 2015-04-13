package com.ankamagames.wakfu.common.game.craft.util;

import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class CraftDistribution extends Distribution
{
    private static final double DELTA_LEVEL_MAX = 30.0;
    
    public CraftDistribution(final short playerLevel, final CraftRecipe recipe) {
        super(getMean(playerLevel, recipe), recipe.getNbResultItems() / 8.0);
    }
    
    public static double getMean(final short playerLevel, final CraftRecipe recipe) {
        final double deltaLevel = MathHelper.clamp(recipe.getRequiredLevel() - playerLevel, 0.0, 30.0);
        return deltaLevel * (recipe.getNbResultItems() - 1) / 30.0;
    }
}
