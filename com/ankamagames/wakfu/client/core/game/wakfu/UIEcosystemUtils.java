package com.ankamagames.wakfu.client.core.game.wakfu;

import com.ankamagames.framework.kernel.core.maths.*;

public class UIEcosystemUtils
{
    public static int getRoundedEcosystemValue(final int value) {
        return (int)(MathHelper.round(value / 10.0f, 0) * 10.0f);
    }
}
