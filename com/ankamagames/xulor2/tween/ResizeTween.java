package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;

public class ResizeTween extends AbstractWidgetTween<Dimension>
{
    public ResizeTween(final Dimension a, final Dimension b, final Widget w, final int delay, final int duration, final TweenFunction function) {
        super(a, b, w, delay, duration, function);
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function != null) {
            final Dimension a = (Dimension)this.m_a;
            final Dimension b = (Dimension)this.m_b;
            final int width = (int)this.m_function.compute(a.width, b.width, this.m_elapsedTime, this.m_duration);
            final int height = (int)this.m_function.compute(a.height, b.height, this.m_elapsedTime, this.m_duration);
            this.getWidget().setSize(width, height, true);
        }
        return true;
    }
    
    @Override
    public void onEnd() {
        final Dimension d = (Dimension)this.m_b;
        this.getWidget().setSize(d.width, d.height, true);
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ResizeTween] ").append(this.m_a).append(" -> ").append(this.m_b);
        return sb.toString();
    }
}
