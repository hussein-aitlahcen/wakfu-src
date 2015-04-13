package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import java.nio.*;

public class NationGovernmentDetailResultMessage extends InputOnlyProxyMessage
{
    private GovernmentInfo m_government;
    
    public GovernmentInfo getGovernment() {
        return this.m_government;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_government = GovernmentInfo.fromBuild(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 20028;
    }
}
