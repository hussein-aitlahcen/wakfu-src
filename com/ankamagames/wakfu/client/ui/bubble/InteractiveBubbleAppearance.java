package com.ankamagames.wakfu.client.ui.bubble;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;

public class InteractiveBubbleAppearance extends DecoratorAppearance
{
    public static final String TAG = "InteractiveBubbleAppearance";
    private BubbleBorder m_bubbleBorder;
    
    public InteractiveBubbleAppearance() {
        super();
        this.m_bubbleBorder = null;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof BubbleBorder) {
            this.m_bubbleBorder = (BubbleBorder)e;
        }
    }
    
    @Override
    public String getTag() {
        return "InteractiveBubbleAppearance";
    }
    
    public BubbleBorder getBubbleBorder() {
        return this.m_bubbleBorder;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_bubbleBorder = null;
    }
}
