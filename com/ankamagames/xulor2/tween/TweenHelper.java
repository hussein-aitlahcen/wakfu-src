package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class TweenHelper
{
    public static void fadeHighlight(final Widget w) {
        final DecoratorAppearance appearance = w.getAppearance();
        final ModulationColorTween tween = new ModulationColorTween(Color.WHITE, Color.WHITE_ALPHA, appearance, 0, 300, 10, true, TweenFunction.PROGRESSIVE);
        appearance.addTween(tween);
    }
}
