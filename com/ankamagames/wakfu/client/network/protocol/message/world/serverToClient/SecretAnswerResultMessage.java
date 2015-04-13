package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class SecretAnswerResultMessage extends InputOnlyProxyMessage
{
    private boolean m_answerIsRight;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_answerIsRight = (bb.get() == 1);
        return true;
    }
    
    public boolean isAnswerIsRight() {
        return this.m_answerIsRight;
    }
    
    @Override
    public int getId() {
        return 2076;
    }
}
