package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.gift;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class GiftInventoryResultMessage extends InputOnlyProxyMessage
{
    private ArrayList<RawGiftPackage> m_giftTokenAccountInventory;
    
    public GiftInventoryResultMessage() {
        super();
        this.m_giftTokenAccountInventory = new ArrayList<RawGiftPackage>();
    }
    
    public ArrayList<RawGiftPackage> getGiftTokenAccountInventory() {
        return this.m_giftTokenAccountInventory;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int count = buffer.getShort() & 0xFFFF, i = 0; i < count; ++i) {
            final RawGiftPackage item = new RawGiftPackage();
            item.unserialize(buffer);
            this.m_giftTokenAccountInventory.add(item);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 13000;
    }
}
