package com.ankamagames.wakfu.common.game.effect.targeting;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

final class TargetComparatorProvider
{
    public static Comparator<EffectUser> getComparator(final WakfuEffect effect, final int x, final int y, final short z) {
        if (effect.hasProperty(RunningEffectPropertyType.SORT_TARGET_BY_PERCENT_HEALTH_ASC)) {
            return new PercentHealthAscComparator();
        }
        if (effect.hasProperty(RunningEffectPropertyType.SORT_TARGET_BY_PERCENT_HEALTH_DESC)) {
            return new PercentHealthDescComparator();
        }
        if (effect.hasProperty(RunningEffectPropertyType.SORT_TARGET_BY_DISTANCE_ASC)) {
            return new DistanceAscComparator(x, y);
        }
        if (effect.hasProperty(RunningEffectPropertyType.SORT_TARGET_BY_DISTANCE_DESC)) {
            return new DistanceDescComparator(x, y);
        }
        final AreaOfEffect areaOfEffect = effect.getAreaOfEffect();
        if (areaOfEffect == null) {
            return null;
        }
        final AreaOfEffectEnum type = areaOfEffect.getType();
        switch (type) {
            case CROSS: {
                return new DistanceAscComparator(x, y);
            }
            default: {
                return null;
            }
        }
    }
}
