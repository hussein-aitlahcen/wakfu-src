package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.krosmoz;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class KrosmozFigureDeleteRequestMessage extends OutputOnlyProxyMessage
{
    private final String m_guid;
    
    public KrosmozFigureDeleteRequestMessage(final String guid) {
        super();
        this.m_guid = guid;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray array = new ByteArray();
        final byte[] encodedString = StringUtils.toUTF8(this.m_guid);
        array.putShort((short)encodedString.length);
        array.put(encodedString);
        return this.addClientHeader((byte)3, array.toArray());
    }
    
    @Override
    public int getId() {
        return 14014;
    }
}
