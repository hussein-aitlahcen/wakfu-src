package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.character.action;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import java.nio.*;

public class MonsterActionRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_npcId;
    private final AbstractClientMonsterAction m_action;
    
    public MonsterActionRequestMessage(final long npcId, final AbstractClientMonsterAction action) {
        super();
        this.m_npcId = npcId;
        this.m_action = action;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16 + this.m_action.serializedSize());
        bb.putLong(this.m_npcId);
        bb.putLong(this.m_action.getId());
        this.m_action.serialize(bb);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 4529;
    }
}
