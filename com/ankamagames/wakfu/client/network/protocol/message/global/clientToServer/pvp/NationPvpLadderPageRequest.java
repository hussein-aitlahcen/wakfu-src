package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.pvp.filter.*;

public class NationPvpLadderPageRequest extends OutputOnlyProxyMessage
{
    private final int m_nationId;
    private final NationPvpLadderFilterParam m_params;
    
    public NationPvpLadderPageRequest(final int nationId, final NationPvpLadderFilterParam params) {
        super();
        this.m_nationId = nationId;
        this.m_params = params;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.m_nationId);
        NationPvpLadderFilterFactory.serialize(this.m_params, ba);
        return this.addClientHeader((byte)6, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 20403;
    }
}
