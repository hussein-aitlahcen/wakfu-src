package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class OfflineFleaTransactionSummaryMessage extends InputOnlyProxyMessage
{
    private long m_kamasEarned;
    private int m_numberOfTransactions;
    
    public long getKamasEarned() {
        return this.m_kamasEarned;
    }
    
    public int getNumberOfTransactions() {
        return this.m_numberOfTransactions;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_kamasEarned = buffer.getInt();
        this.m_numberOfTransactions = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 5242;
    }
}
