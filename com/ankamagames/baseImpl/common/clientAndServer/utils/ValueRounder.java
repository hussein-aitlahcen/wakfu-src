package com.ankamagames.baseImpl.common.clientAndServer.utils;

import com.ankamagames.framework.kernel.core.maths.*;

public class ValueRounder
{
    public static int randomRound(final float value) {
        double integerPart = Math.floor(value);
        final double decimalPart = value - integerPart;
        if (MersenneTwister.getInstance().nextBoolean(decimalPart)) {
            ++integerPart;
        }
        return (int)integerPart;
    }
}
