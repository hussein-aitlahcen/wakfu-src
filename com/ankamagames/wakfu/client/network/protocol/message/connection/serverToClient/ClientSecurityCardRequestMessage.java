package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ClientSecurityCardRequestMessage extends InputOnlyProxyMessage
{
    private String m_question;
    private long m_salt;
    private byte[] m_publicKey;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] utfQuestion = new byte[bb.getInt()];
        bb.get(utfQuestion);
        this.m_question = StringUtils.fromUTF8(utfQuestion);
        this.m_salt = bb.getLong();
        bb.get(this.m_publicKey = new byte[bb.getInt()]);
        return true;
    }
    
    public String getQuestion() {
        return this.m_question;
    }
    
    public long getSalt() {
        return this.m_salt;
    }
    
    public byte[] getPublicKey() {
        return this.m_publicKey.clone();
    }
    
    @Override
    public int getId() {
        return 1037;
    }
}
