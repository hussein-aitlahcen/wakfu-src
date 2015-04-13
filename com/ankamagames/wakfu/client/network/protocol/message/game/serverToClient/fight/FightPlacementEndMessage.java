package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class FightPlacementEndMessage extends AbstractFightMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(buffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 8028;
    }
}
