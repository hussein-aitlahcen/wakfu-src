package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class CharacterRenameResultMessage extends InputOnlyProxyMessage
{
    private byte m_renameErrorCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_renameErrorCode = bb.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 2072;
    }
    
    public byte getErrorCode() {
        return this.m_renameErrorCode;
    }
}
