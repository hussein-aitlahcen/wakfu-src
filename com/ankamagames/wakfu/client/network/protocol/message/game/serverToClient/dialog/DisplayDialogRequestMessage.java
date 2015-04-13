package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dialog;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.dialog.*;

public class DisplayDialogRequestMessage extends InputOnlyProxyMessage
{
    private int m_dialogId;
    private byte m_sourceTypeId;
    private long m_sourceId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_dialogId = bb.getInt();
        this.m_sourceTypeId = bb.get();
        this.m_sourceId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15704;
    }
    
    public int getDialogId() {
        return this.m_dialogId;
    }
    
    public DialogSourceType getSourceType() {
        return DialogSourceType.getFromId(this.m_sourceTypeId);
    }
    
    public long getSourceId() {
        return this.m_sourceId;
    }
}
