package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class AbstractInteractiveElementManager<T extends AnimatedInteractiveElement> extends AbstractAnimatedElementManager<T>
{
    public int selectElementUnderPoint(final float x, final float y) {
        int count = 0;
        final ArrayList<T> elements = this.m_displayedElements;
        for (int i = 0, size = elements.size(); i < size; ++i) {
            final T element = elements.get(i);
            if (element.hitTest(x, y)) {
                element.setSelected(true);
                ++count;
            }
            else {
                element.setSelected(false);
            }
        }
        return count;
    }
    
    protected void updateScreenPosition(final T element, final AleaWorldScene scene) {
        if (!element.hasWatchers()) {
            return;
        }
        final Point2 p = IsoCameraFunc.getScreenPositionFromCenter(scene, element);
        final int newScreenX = MathHelper.fastRound(p.m_x);
        final int newScreenY = MathHelper.fastRound(p.m_y);
        final int newScreenHeight = IsoCameraFunc.getScreenHeight(scene, element.getVisualHeight());
        if (newScreenX != element.getScreenX() || newScreenY != element.getScreenY() || newScreenHeight != element.getScreenTargetHeight()) {
            element.setScreenX(newScreenX);
            element.setScreenY(newScreenY);
            element.setScreenTargetHeight(newScreenHeight);
            element.fireScreenPositionChanged();
        }
    }
}
