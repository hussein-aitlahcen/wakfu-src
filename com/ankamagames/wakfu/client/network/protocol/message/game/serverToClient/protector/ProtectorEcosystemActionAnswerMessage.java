package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.protector.*;
import java.nio.*;

public final class ProtectorEcosystemActionAnswerMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private boolean m_actionResult;
    private ProtectorEcosystemAction m_action;
    private int m_familyId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = bb.getInt();
        this.m_actionResult = (bb.get() == 1);
        this.m_action = ProtectorEcosystemAction.getActionById(bb.get());
        this.m_familyId = bb.getInt();
        return true;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public boolean isActionResult() {
        return this.m_actionResult;
    }
    
    public ProtectorEcosystemAction getAction() {
        return this.m_action;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    @Override
    public int getId() {
        return 15330;
    }
}
