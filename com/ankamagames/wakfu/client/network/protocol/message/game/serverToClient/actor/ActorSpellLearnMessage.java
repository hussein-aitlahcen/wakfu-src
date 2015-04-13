package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class ActorSpellLearnMessage extends InputOnlyProxyMessage
{
    private ArrayList<ObjectTriplet<Integer, Long, Short>> m_learnedSpells;
    
    public ActorSpellLearnMessage() {
        super();
        this.m_learnedSpells = new ArrayList<ObjectTriplet<Integer, Long, Short>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            this.m_learnedSpells.add(new ObjectTriplet<Integer, Long, Short>(buffer.getInt(), buffer.getLong(), buffer.getShort()));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8402;
    }
    
    public ArrayList<ObjectTriplet<Integer, Long, Short>> getLearnedSpells() {
        return this.m_learnedSpells;
    }
}
