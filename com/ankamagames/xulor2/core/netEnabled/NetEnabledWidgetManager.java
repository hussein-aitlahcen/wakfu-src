package com.ankamagames.xulor2.core.netEnabled;

import java.util.*;
import com.ankamagames.xulor2.component.*;

public class NetEnabledWidgetManager
{
    public static final NetEnabledWidgetManager INSTANCE;
    private final HashMap<String, NetEnabledGroup> m_groups;
    
    private NetEnabledWidgetManager() {
        super();
        this.m_groups = new HashMap<String, NetEnabledGroup>();
    }
    
    public void createGroup(final String netGroupId) {
        this.m_groups.put(netGroupId, new NetEnabledGroup(netGroupId));
    }
    
    public void destroyGroup(final String netGroupId) {
        this.m_groups.remove(netGroupId);
    }
    
    public void addToGroup(final String netGroupId, final Widget w) {
        final NetEnabledGroup group = this.m_groups.get(netGroupId);
        if (group != null) {
            group.addWidget(w);
        }
    }
    
    public void removeFromGroup(final String netGroupId, final Widget w) {
        final NetEnabledGroup group = this.m_groups.get(netGroupId);
        if (group != null) {
            group.removeWidget(w);
        }
    }
    
    public void setGroupEnabled(final String netGroupId, final boolean enabled) {
        final NetEnabledGroup group = this.m_groups.get(netGroupId);
        if (group != null) {
            group.setEnabled(enabled);
        }
    }
    
    static {
        INSTANCE = new NetEnabledWidgetManager();
    }
}
