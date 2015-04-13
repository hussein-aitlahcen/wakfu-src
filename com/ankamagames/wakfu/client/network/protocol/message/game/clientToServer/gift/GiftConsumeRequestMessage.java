package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GiftConsumeRequestMessage extends OutputOnlyProxyMessage
{
    private String m_giftGuid;
    
    public void setGiftGuid(final String giftGuid) {
        this.m_giftGuid = giftGuid;
    }
    
    @Override
    public byte[] encode() {
        final byte[] serialized_guid = StringUtils.toUTF8(this.m_giftGuid);
        final ByteBuffer buffer = ByteBuffer.allocate(2 + serialized_guid.length);
        buffer.putShort((short)serialized_guid.length);
        buffer.put(serialized_guid);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 13003;
    }
}
