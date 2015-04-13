package com.ankamagames.wakfu.common.game.resource;

import com.ankamagames.wakfu.common.game.groundType.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;

public final class ResourceSproutHelper
{
    public static boolean rollSprout(final ResourcePlanter sower, final AbstractReferenceResource resource, final GroundType groundType, final float precipitation) {
        return rollSprout(sower, resource, groundType, precipitation, MathHelper.getRandomGenerator());
    }
    
    public static boolean rollSprout(final ResourcePlanter sower, final AbstractReferenceResource resource, final GroundType groundType, final float precipitation, final Random random) {
        final short fertility = groundType.getFertilityByReferenceResource(resource.getId(), resource.getResourceType());
        return fertility > 0 && random.nextDouble() < getChancesToGrow(sower, resource, fertility, precipitation);
    }
    
    public static double getChancesToGrow(final ResourcePlanter sower, final AbstractReferenceResource resource, final GroundType groundType, final float precipitation) {
        final short fertility = groundType.getFertilityByReferenceResource(resource.getId(), resource.getResourceType());
        return getChancesToGrow(sower, resource, fertility, precipitation);
    }
    
    private static double getChancesToGrow(final ResourcePlanter sower, final AbstractReferenceResource resource, final int groundFertility, final float precipitation) {
        final double fertility = fertilityFactor(groundFertility);
        final double weather = weatherFactor(resource, precipitation);
        final double characteristic = characteristicBonus(sower, resource);
        return MathHelper.clamp(fertility * weather * characteristic, 0.0, 1.0);
    }
    
    private static double fertilityFactor(final int fertility) {
        return fertility / 100.0;
    }
    
    private static double weatherFactor(final AbstractReferenceResource resource, final float precipitations) {
        if (precipitations == -1.0f) {
            return 1.0;
        }
        return (precipitations >= resource.getIdealRainMin() && precipitations <= resource.getIdealRainMax()) ? 1.25 : 1.0;
    }
    
    private static double characteristicBonus(final ResourcePlanter character, final AbstractReferenceResource resource) {
        return 1.0 + character.getPlantationBonus(ResourceType.getByAgtIdOrHWCategory(resource.getResourceType()));
    }
}
