package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class ActorDespawnMessage extends InputOnlyProxyMessage
{
    private final ArrayList<ObjectPair<Byte, Long>> m_actorsIds;
    private boolean m_applyApsOnDespawn;
    private boolean m_fightDespawn;
    
    public ActorDespawnMessage() {
        super();
        this.m_actorsIds = new ArrayList<ObjectPair<Byte, Long>>();
        this.m_applyApsOnDespawn = false;
        this.m_fightDespawn = false;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 3, false)) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_applyApsOnDespawn = (buffer.get() == 1);
        this.m_fightDespawn = (buffer.get() == 1);
        final int actorsCount = buffer.get() & 0xFF;
        if (!this.checkMessageSize(buffer.remaining(), actorsCount * 9, true)) {
            return false;
        }
        for (int i = 0; i < actorsCount; ++i) {
            final byte type = buffer.get();
            final long id = buffer.getLong();
            this.m_actorsIds.add(new ObjectPair<Byte, Long>(type, id));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4104;
    }
    
    public ArrayList<ObjectPair<Byte, Long>> getActorsIds() {
        return this.m_actorsIds;
    }
    
    public boolean isApplyApsOnDespawn() {
        return this.m_applyApsOnDespawn;
    }
    
    public boolean isFightDespawn() {
        return this.m_fightDespawn;
    }
}
