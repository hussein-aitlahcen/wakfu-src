package com.ankamagames.xulor2.component.clock;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.*;

public class TimedWidgetData implements MessageHandler
{
    private Widget m_widget;
    private long m_clockId;
    
    public TimedWidgetData(final Widget w, final int duration) {
        super();
        this.m_widget = w;
        this.m_clockId = MessageScheduler.getInstance().addClock(this, duration, 0, 1);
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void stop() {
        MessageScheduler.getInstance().removeClock(this.m_clockId);
        this.m_clockId = 0L;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final ClockMessage msg = (ClockMessage)message;
            if (msg.getClockId() == this.m_clockId) {
                if (this.m_widget.isElementMapRoot() && this.m_widget.getElementMap() != null) {
                    Xulor.getInstance().unload(this.m_widget.getElementMap().getId());
                }
                else {
                    this.m_widget.destroySelfFromParent();
                }
            }
        }
        return false;
    }
}
