package com.ankamagames.wakfu.client.core.game.collector.inventory.limited;

import com.ankamagames.wakfu.common.game.collector.limited.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import java.nio.*;

public class ClientCollectorInventoryLimited extends CollectorInventoryLimited implements ClientCollectorInventory
{
    public ClientCollectorInventoryLimited(final IECollectorParameter param) {
        super(param);
    }
    
    @Override
    public void unSerialize(final byte[] datas) {
        this.m_content.clear();
        final ByteBuffer bb = ByteBuffer.wrap(datas);
        this.m_wallet.setAmountOfCash(bb.getInt());
        final short size = bb.getShort();
        for (int i = 0; i < size; ++i) {
            this.m_content.put(bb.getInt(), bb.getInt());
        }
    }
}
