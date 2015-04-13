package com.ankamagames.xulor2.core.netEnabled;

import java.util.*;
import com.ankamagames.xulor2.component.*;

public class NetEnabledGroup
{
    private final String m_netGroupId;
    private final ArrayList<Widget> m_widgets;
    private boolean m_enabled;
    
    public NetEnabledGroup(final String netGroupId) {
        super();
        this.m_widgets = new ArrayList<Widget>();
        this.m_enabled = true;
        this.m_netGroupId = netGroupId;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.m_enabled == enabled) {
            return;
        }
        this.m_enabled = enabled;
        for (int i = 0, size = this.m_widgets.size(); i < size; ++i) {
            final Widget w = this.m_widgets.get(i);
            w.setNetEnabled(enabled);
        }
    }
    
    public void addWidget(final Widget w) {
        this.m_widgets.add(w);
        w.setNetEnabled(this.m_enabled);
    }
    
    public void removeWidget(final Widget w) {
        this.m_widgets.remove(w);
    }
}
