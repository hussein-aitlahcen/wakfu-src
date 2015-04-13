package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.*;

public class PositionTween extends AbstractWidgetTween
{
    private int m_xA;
    private int m_yA;
    private int m_xB;
    private int m_yB;
    
    public PositionTween(final int xa, final int ya, final int xb, final int yb, final Widget w, final int delay, final int duration, final TweenFunction function) {
        super(null, null, w, delay, duration, function);
        this.m_xA = xa;
        this.m_yA = ya;
        this.m_xB = xb;
        this.m_yB = yb;
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function != null) {
            final int x = (int)this.m_function.compute(this.m_xA, this.m_xB, this.m_elapsedTime, this.m_duration);
            final int y = (int)this.m_function.compute(this.m_yA, this.m_yB, this.m_elapsedTime, this.m_duration);
            this.getWidget().setPosition(x, y, true);
        }
        return true;
    }
    
    @Override
    public void onEnd() {
        this.getWidget().setPosition(this.m_xB, this.m_yB, true);
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[PositionTween] (").append(this.m_xA).append(", ").append(this.m_yA).append(") -> (").append(this.m_xB).append(", ").append(this.m_yB).append(")");
        return sb.toString();
    }
}
