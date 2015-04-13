package com.ankamagames.wakfu.client.core.game.skill;

import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public final class ActionVisualHelper
{
    public static boolean isAnimationEmpty(final ActionVisual actionVisual) {
        return actionVisual == null || actionVisual.getAnimLink().length() == 0;
    }
    
    public static void applyActionVisual(final PathMobile mobile, final ActionVisual actionVisual) {
        applyActionVisual(mobile, actionVisual, false);
    }
    
    public static void applyActionVisual(final PathMobile mobile, final ActionVisual actionVisual, final boolean end) {
        final String animLink = end ? (getAnimationName(actionVisual) + "-Fin") : actionVisual.getAnimLink();
        if (animLink.length() == 0) {
            return;
        }
        mobile.setAnimation(animLink);
        mobile.setIncrementZOrder(actionVisual.isMultiCell());
    }
    
    public static String getAnimationName(final ActionVisual actionVisual) {
        final String animLink = actionVisual.getAnimLink();
        final int index = animLink.lastIndexOf("-Debut");
        return (index == -1) ? animLink : animLink.substring(0, index);
    }
}
