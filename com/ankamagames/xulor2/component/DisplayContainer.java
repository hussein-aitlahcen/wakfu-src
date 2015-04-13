package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.component.clock.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class DisplayContainer extends Container
{
    public static final String TAG = "DisplayContainer";
    private int m_duration;
    private int m_contentSize;
    private final HashMap<Widget, TimedWidgetData> m_timedWidgetDataList;
    public static final int CONTENT_SIZE_HASH;
    public static final int DURATION_HASH;
    
    public DisplayContainer() {
        super();
        this.m_duration = 30;
        this.m_contentSize = 10;
        this.m_timedWidgetDataList = new HashMap<Widget, TimedWidgetData>();
    }
    
    @Override
    public boolean addWidget(final Widget widget, final int index) {
        final boolean added = super.addWidget(widget, index);
        if (added) {
            if (this.m_widgetChildren.size() > this.m_contentSize) {
                this.m_widgetChildren.get(0).destroySelfFromParent();
            }
            this.m_timedWidgetDataList.put(widget, new TimedWidgetData(widget, this.m_duration * 1000));
            final DisplayContainerChangedEvent e = new DisplayContainerChangedEvent(this, this.m_widgetChildren.size() == this.m_contentSize);
            e.onCheckOut();
            this.dispatchEvent(e);
        }
        return added;
    }
    
    @Override
    public void removeWidget(final Widget widget) {
        final TimedWidgetData twd = this.m_timedWidgetDataList.remove(widget);
        if (twd != null) {
            twd.stop();
        }
        super.removeWidget(widget);
        final DisplayContainerChangedEvent e = new DisplayContainerChangedEvent(this, this.m_widgetChildren.size() == this.m_contentSize);
        e.onCheckOut();
        this.dispatchEvent(e);
    }
    
    @Override
    public String getTag() {
        return "DisplayContainer";
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    public int getContentSize() {
        return this.m_contentSize;
    }
    
    public void setContentSize(final int contentSize) {
        this.m_contentSize = contentSize;
    }
    
    public boolean isFull() {
        return this.m_contentSize == this.m_widgetChildren.size();
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final DisplayContainer dc = (DisplayContainer)c;
        dc.setDuration(this.m_duration);
        dc.setContentSize(this.m_contentSize);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        for (final TimedWidgetData data : this.m_timedWidgetDataList.values()) {
            data.stop();
        }
        this.m_timedWidgetDataList.clear();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == DisplayContainer.CONTENT_SIZE_HASH) {
            this.setContentSize(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != DisplayContainer.DURATION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == DisplayContainer.CONTENT_SIZE_HASH) {
            this.setContentSize(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != DisplayContainer.DURATION_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setDuration(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        CONTENT_SIZE_HASH = "contentSize".hashCode();
        DURATION_HASH = "duration".hashCode();
    }
}
