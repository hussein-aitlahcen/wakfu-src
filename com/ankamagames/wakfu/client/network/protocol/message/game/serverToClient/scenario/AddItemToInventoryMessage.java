package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AddItemToInventoryMessage extends InputOnlyProxyMessage
{
    private long m_uid;
    private int m_referenceId;
    private short m_quantity;
    private boolean m_completeFeedBack;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_uid = buff.getLong();
        this.m_referenceId = buff.getInt();
        this.m_quantity = buff.getShort();
        this.m_completeFeedBack = (buff.get() != 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 11102;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public boolean isCompleteFeedBack() {
        return this.m_completeFeedBack;
    }
}
