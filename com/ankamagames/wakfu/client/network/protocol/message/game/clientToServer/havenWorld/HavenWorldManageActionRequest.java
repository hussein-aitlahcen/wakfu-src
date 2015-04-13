package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class HavenWorldManageActionRequest extends OutputOnlyProxyMessage
{
    private final ArrayList<HavenWorldAction> m_actions;
    
    public HavenWorldManageActionRequest() {
        super();
        this.m_actions = new ArrayList<HavenWorldAction>();
    }
    
    public boolean addAction(final HavenWorldAction action) {
        return !this.m_actions.contains(action) && this.m_actions.add(action);
    }
    
    @Override
    public byte[] encode() {
        final ByteArray bb = new ByteArray();
        for (int i = 0; i < this.m_actions.size(); ++i) {
            final HavenWorldAction action = this.m_actions.get(i);
            bb.put(action.getActionType().id);
            bb.put(action.serialize());
        }
        return this.addClientHeader((byte)3, bb.toArray());
    }
    
    @Override
    public int getId() {
        return 15651;
    }
    
    @Override
    public String toString() {
        return "HavenWorldManageActionRequest{m_actions=" + this.m_actions.size() + '}';
    }
}
