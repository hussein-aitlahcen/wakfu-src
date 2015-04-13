package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public class EndFightCreationMessage extends AbstractFightMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(byteBuffer);
        return true;
    }
    
    @Override
    public int getId() {
        return 8202;
    }
}
