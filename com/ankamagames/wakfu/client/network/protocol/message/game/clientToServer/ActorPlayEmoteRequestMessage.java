package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ActorPlayEmoteRequestMessage extends OutputOnlyProxyMessage
{
    private int m_emoteId;
    private boolean m_emoteOccupation;
    private long m_targetId;
    
    public ActorPlayEmoteRequestMessage() {
        super();
        this.m_targetId = -1L;
    }
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 13;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putInt(this.m_emoteId);
        buffer.put((byte)(this.m_emoteOccupation ? 1 : 0));
        buffer.putLong(this.m_targetId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15401;
    }
    
    public void setEmoteId(final int emoteId) {
        this.m_emoteId = emoteId;
    }
    
    public void setEmoteOccupation(final boolean emoteOccupation) {
        this.m_emoteOccupation = emoteOccupation;
    }
    
    public void setTargetId(final long targetId) {
        this.m_targetId = targetId;
    }
}
