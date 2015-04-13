package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import gnu.trove.*;
import java.nio.*;

public class ItemPickedUpMessage extends InputOnlyProxyMessage
{
    private final ArrayList<Long> m_itemIds;
    private final TByteArrayList m_canPickUpList;
    private final ArrayList<Long> m_destinationIds;
    
    public ItemPickedUpMessage() {
        super();
        this.m_itemIds = new ArrayList<Long>();
        this.m_canPickUpList = new TByteArrayList();
        this.m_destinationIds = new ArrayList<Long>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final short itemCount = buffer.getShort();
        for (int i = 0; i < itemCount; ++i) {
            this.m_itemIds.add(buffer.getLong());
            this.m_canPickUpList.add(buffer.get());
            this.m_destinationIds.add(buffer.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5208;
    }
    
    public ArrayList<Long> getItemIds() {
        return this.m_itemIds;
    }
    
    public TByteArrayList getCanPickUpList() {
        return this.m_canPickUpList;
    }
    
    public ArrayList<Long> getDestinationIds() {
        return this.m_destinationIds;
    }
}
