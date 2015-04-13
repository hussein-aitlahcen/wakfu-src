package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import gnu.trove.*;
import java.nio.*;

public final class AwaitingFightersNotificationMessage extends AbstractFightMessage
{
    private TLongHashSet m_awaitedFighters;
    
    public AwaitingFightersNotificationMessage() {
        super();
        this.m_awaitedFighters = new TLongHashSet();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        final byte size = bb.get();
        for (int i = 0; i < size; ++i) {
            this.m_awaitedFighters.add(bb.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8016;
    }
    
    public TLongHashSet getAwaitedFighters() {
        return this.m_awaitedFighters;
    }
}
