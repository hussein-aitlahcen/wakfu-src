package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ClientDispatchAuthenticationCardMessage extends OutputOnlyProxyMessage
{
    private byte[] m_encryptedLoginAndPassword;
    private byte[] m_encryptedCardQuestionAnswer;
    
    public ClientDispatchAuthenticationCardMessage() {
        super();
        this.m_encryptedLoginAndPassword = null;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.m_encryptedLoginAndPassword.length);
        ba.put(this.m_encryptedLoginAndPassword);
        ba.putInt(this.m_encryptedCardQuestionAnswer.length);
        ba.put(this.m_encryptedCardQuestionAnswer);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1038;
    }
    
    public void setEncryptedLoginAndPassword(final byte[] encryptedLoginAndPassword) {
        this.m_encryptedLoginAndPassword = encryptedLoginAndPassword.clone();
    }
    
    public void setEncryptedCardQuestionAnswer(final byte[] encryptedCardQuestionAnswer) {
        this.m_encryptedCardQuestionAnswer = encryptedCardQuestionAnswer.clone();
    }
}
