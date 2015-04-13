package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class SecretQuestionMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private String m_question;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        final byte[] bytes = new byte[bb.getShort()];
        bb.get(bytes);
        this.m_question = StringUtils.fromUTF8(bytes);
        return true;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public String getQuestion() {
        return this.m_question;
    }
    
    @Override
    public int getId() {
        return 2074;
    }
}
