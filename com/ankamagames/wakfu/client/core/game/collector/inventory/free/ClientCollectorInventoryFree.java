package com.ankamagames.wakfu.client.core.game.collector.inventory.free;

import com.ankamagames.wakfu.common.game.collector.free.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ClientCollectorInventoryFree extends CollectorInventoryFree implements ClientCollectorInventory
{
    public ClientCollectorInventoryFree(final IECollectorParameter param) {
        super(param);
    }
    
    @Override
    public void unSerialize(final byte[] datas) {
        this.m_content.cleanup();
        final ByteBuffer bb = ByteBuffer.wrap(datas);
        final InventoryContentProvider<Item, RawInventoryItem> provider = this.m_content.getContentProvider();
        final RawInventoryItem rawItem = new RawInventoryItem();
        this.m_wallet.setAmountOfCash(bb.getInt());
        while (bb.hasRemaining()) {
            rawItem.clear();
            rawItem.unserialize(bb);
            this.add(provider.unSerializeContent(rawItem));
        }
    }
    
    public Inventory<Item> getContent() {
        return (Inventory<Item>)this.m_content;
    }
}
