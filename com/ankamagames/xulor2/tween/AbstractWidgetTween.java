package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.*;

public abstract class AbstractWidgetTween<T> extends AbstractTween<T>
{
    public AbstractWidgetTween() {
        super();
    }
    
    public AbstractWidgetTween(final T a, final T b, final Widget w, final int delay, final int duration, final TweenFunction function) {
        super();
        this.setA(a);
        this.setB(b);
        this.setWidget(w);
        this.setDelay(delay);
        this.setDuration(duration);
        this.setTweenFunction(function);
    }
    
    public void setWidget(final Widget widget) {
        this.m_client = widget;
    }
    
    public Widget getWidget() {
        return (Widget)this.m_client;
    }
}
