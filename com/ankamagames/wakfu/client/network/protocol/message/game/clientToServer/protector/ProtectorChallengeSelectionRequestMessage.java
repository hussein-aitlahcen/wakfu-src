package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorChallengeSelectionRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    private int m_challengeId;
    private boolean m_selection;
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public void setChallengeId(final int challengeId) {
        this.m_challengeId = challengeId;
    }
    
    public void setSelection(final boolean selection) {
        this.m_selection = selection;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putInt(this.m_protectorId);
        buffer.putInt(this.m_challengeId);
        buffer.put((byte)(this.m_selection ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15323;
    }
}
