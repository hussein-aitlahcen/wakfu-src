package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import java.nio.*;

public class PlayerXpModificationMessage extends InputOnlyProxyMessage
{
    private PlayerXpModificationCollection m_playerXpModificationCollection;
    private boolean m_fightXp;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_fightXp = (bb.get() != 0);
        this.m_playerXpModificationCollection = PlayerXpModificationCollection.deserialize(bb);
        if (bb.hasRemaining()) {
            PlayerXpModificationMessage.m_logger.error((Object)("Il reste " + bb.remaining() + " bytes inutilis\u00e9s"));
            return false;
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4214;
    }
    
    public int size() {
        return this.m_playerXpModificationCollection.size();
    }
    
    public PlayerXpModificationCollection getXpModifications() {
        return this.m_playerXpModificationCollection;
    }
    
    public boolean isFightXp() {
        return this.m_fightXp;
    }
}
