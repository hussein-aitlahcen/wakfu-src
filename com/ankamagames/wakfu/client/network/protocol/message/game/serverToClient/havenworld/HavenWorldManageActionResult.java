package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;

public class HavenWorldManageActionResult extends InputOnlyProxyMessage
{
    private HavenWorldAction m_action;
    private HavenWorldError m_error;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final HavenWorldActionType type = HavenWorldActionType.valueOf(bb.get());
        (this.m_action = type.createNew()).unSerialize(bb);
        this.m_error = HavenWorldError.valueOf(bb.getInt());
        return true;
    }
    
    public HavenWorldAction getAction() {
        return this.m_action;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
    }
    
    @Override
    public int getId() {
        return 15652;
    }
    
    @Override
    public String toString() {
        return "HavenWorldManageActionResult{m_error=" + this.m_error + '}';
    }
}
