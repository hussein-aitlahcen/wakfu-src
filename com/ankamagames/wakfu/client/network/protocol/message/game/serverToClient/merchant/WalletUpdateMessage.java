package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class WalletUpdateMessage extends InputOnlyProxyMessage
{
    private int m_amountOfCash;
    
    public int getAmountOfCash() {
        return this.m_amountOfCash;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_amountOfCash = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 5240;
    }
}
