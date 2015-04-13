package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ClientAuthenticationResultsMessage extends InputOnlyProxyMessage
{
    private byte m_resultCode;
    private int m_banDuration;
    private byte[] m_serializedAccountInformations;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_resultCode = bb.get();
        if (this.m_resultCode == 5) {
            if (!this.checkMessageSize(rawDatas.length, 5, false)) {
                return false;
            }
            this.m_banDuration = bb.getInt();
        }
        if (this.isSuccessfull()) {
            if (!this.checkMessageSize(rawDatas.length, 3, false)) {
                return false;
            }
            final int serializedAccountInformationSize = bb.getShort() & 0xFFFF;
            if (!this.checkMessageSize(rawDatas.length, serializedAccountInformationSize + 3, true)) {
                return false;
            }
            bb.get(this.m_serializedAccountInformations = new byte[serializedAccountInformationSize]);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 1025;
    }
    
    public boolean isSuccessfull() {
        return this.m_resultCode == 0;
    }
    
    public byte getErrorCode() {
        return this.m_resultCode;
    }
    
    public byte[] getSerializedAccountInformations() {
        return this.m_serializedAccountInformations;
    }
    
    public int getBanDuration() {
        return this.m_banDuration;
    }
}
