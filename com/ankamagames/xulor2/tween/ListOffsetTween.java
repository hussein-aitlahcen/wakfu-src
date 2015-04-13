package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.*;

public class ListOffsetTween extends AbstractWidgetTween
{
    private float m_offseta;
    private float m_offsetb;
    
    public ListOffsetTween(final float offseta, final float offsetb, final List list, final int delay, final int duration, final TweenFunction function) {
        super(null, null, list, delay, duration, function);
        this.m_offseta = offseta;
        this.m_offsetb = offsetb;
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function != null) {
            final float offset = this.m_function.compute(this.m_offseta, this.m_offsetb, this.m_elapsedTime, this.m_duration);
            ((List)this.getWidget()).setListOffset(offset);
        }
        return true;
    }
    
    @Override
    public void onEnd() {
        ((List)this.getWidget()).setListOffset(this.m_offsetb);
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ListOffsetTween] (").append(this.m_offseta).append(") -> (").append(this.m_offsetb).append(")");
        return sb.toString();
    }
}
