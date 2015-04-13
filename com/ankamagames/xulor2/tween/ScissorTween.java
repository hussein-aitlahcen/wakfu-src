package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.*;
import java.awt.*;

public class ScissorTween extends AbstractWidgetTween<Rectangle>
{
    private final boolean m_clearAtEnd;
    
    public ScissorTween(final Rectangle a, final Rectangle b, final Widget w, final boolean clearAtEnd, final int delay, final int duration, final TweenFunction function) {
        super(a, b, w, delay, duration, function);
        if (a == null) {
            this.setA(new Rectangle(new Point(0, 0), w.getSize()));
        }
        this.m_clearAtEnd = clearAtEnd;
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function == null) {
            return true;
        }
        final Rectangle a = (Rectangle)this.m_a;
        final Rectangle b = (Rectangle)this.m_b;
        final int x = (int)this.m_function.compute(a.x, b.x, this.m_elapsedTime, this.m_duration);
        final int y = (int)this.m_function.compute(a.y, b.y, this.m_elapsedTime, this.m_duration);
        final int width = (int)this.m_function.compute(a.width, b.width, this.m_elapsedTime, this.m_duration);
        final int height = (int)this.m_function.compute(a.height, b.height, this.m_elapsedTime, this.m_duration);
        this.getWidget().setScissor(new Rectangle(x, y, width, height));
        return true;
    }
    
    @Override
    public void onEnd() {
        if (this.m_clearAtEnd) {
            this.getWidget().setScissor(null);
        }
        else {
            this.getWidget().setScissor((Rectangle)this.m_b);
        }
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.getClass().getSimpleName()).append("]").append(this.m_a).append(" -> ").append(this.m_b);
        return sb.toString();
    }
}
