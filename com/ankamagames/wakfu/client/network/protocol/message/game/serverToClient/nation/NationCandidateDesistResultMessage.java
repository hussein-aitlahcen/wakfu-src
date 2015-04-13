package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NationCandidateDesistResultMessage extends InputOnlyProxyMessage
{
    private boolean m_success;
    private long m_fromId;
    private String m_fromName;
    private long m_toId;
    private String m_toName;
    private int m_candidateBallotsCount;
    private int m_ballotsGiven;
    
    public boolean isSuccess() {
        return this.m_success;
    }
    
    public long getCandidateFromId() {
        return this.m_fromId;
    }
    
    public String getCandidateFromName() {
        return this.m_fromName;
    }
    
    public long getCandidateToId() {
        return this.m_toId;
    }
    
    public String getCandidateToName() {
        return this.m_toName;
    }
    
    public int getCandidateBallotsCount() {
        return this.m_candidateBallotsCount;
    }
    
    public int getBallotsGiven() {
        return this.m_ballotsGiven;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_success = (buffer.get() != 0);
        if (this.m_success) {
            this.m_ballotsGiven = buffer.getInt();
            this.m_candidateBallotsCount = buffer.getInt();
            this.m_fromId = buffer.getLong();
            final byte fromNameSize = buffer.get();
            byte[] name = new byte[fromNameSize];
            buffer.get(name);
            this.m_fromName = StringUtils.fromUTF8(name);
            this.m_toId = buffer.getLong();
            final byte toNameSize = buffer.get();
            name = new byte[toNameSize];
            buffer.get(name);
            this.m_toName = StringUtils.fromUTF8(name);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20008;
    }
}
