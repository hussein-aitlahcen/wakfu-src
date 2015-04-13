package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class AddItemToCompanionEquipmentErrorMessage extends InputOnlyProxyMessage
{
    private int m_errorCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.getInt();
        return false;
    }
    
    public int getErrorCode() {
        return this.m_errorCode;
    }
    
    @Override
    public int getId() {
        return 5563;
    }
}
