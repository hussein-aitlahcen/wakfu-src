package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class FightCompanionPlacementRequestMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    private Point3 m_position;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_characterId);
        ba.putInt(this.m_position.getX());
        ba.putInt(this.m_position.getY());
        ba.putShort(this.m_position.getZ());
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setPosition(final Point3 position) {
        this.m_position = position;
    }
    
    @Override
    public int getId() {
        return 8161;
    }
    
    @Override
    public String toString() {
        return "FightCompanionPlacementRequestMessage{m_characterId=" + this.m_characterId + ", m_position=" + this.m_position + '}';
    }
}
