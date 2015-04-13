package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public final class SecretAnswerMessage extends OutputOnlyProxyMessage
{
    private final long m_characterId;
    private final String m_answer;
    
    public SecretAnswerMessage(final long characterId, final String answer) {
        super();
        this.m_characterId = characterId;
        this.m_answer = answer;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_characterId);
        final byte[] bytes = StringUtils.toUTF8(this.m_answer);
        ba.putShort((short)bytes.length);
        ba.put(bytes);
        return this.addClientHeader((byte)2, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 2075;
    }
}
