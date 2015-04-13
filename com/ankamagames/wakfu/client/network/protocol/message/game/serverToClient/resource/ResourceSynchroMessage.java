package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class ResourceSynchroMessage extends InputOnlyProxyMessage
{
    private byte m_flag;
    private long m_estimatedTime;
    private byte m_craftId;
    private int m_x;
    private int m_y;
    private byte m_remainingSlots;
    private short m_remainingLevel;
    private ArrayList<ObjectPair<Long, Item>> m_items;
    
    public ResourceSynchroMessage() {
        super();
        this.m_flag = 0;
        this.m_estimatedTime = 0L;
        this.m_craftId = 0;
        this.m_items = new ArrayList<ObjectPair<Long, Item>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_estimatedTime = buffer.getInt();
        this.m_flag = buffer.get();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_craftId = buffer.get();
        this.m_remainingSlots = buffer.get();
        this.m_remainingLevel = buffer.getShort();
        for (int itemCount = buffer.getShort(), i = 0; i < itemCount; ++i) {
            final long guId = buffer.getLong();
            final int referenceId = buffer.getInt();
            final byte quantity = buffer.get();
            final long containerId = buffer.getLong();
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(referenceId);
            if (refItem != null) {
                final Item item = new Item(guId);
                item.initializeWithReferenceItem(refItem);
                item.setQuantity(quantity);
                this.m_items.add(new ObjectPair<Long, Item>(containerId, item));
            }
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4204;
    }
    
    public byte getFlag() {
        return this.m_flag;
    }
    
    public long getEstimatedTime() {
        return this.m_estimatedTime;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public byte getCraftId() {
        return this.m_craftId;
    }
    
    public ArrayList<ObjectPair<Long, Item>> getItems() {
        return this.m_items;
    }
    
    public byte getRemainingSlots() {
        return this.m_remainingSlots;
    }
    
    public short getRemainingLevel() {
        return this.m_remainingLevel;
    }
    
    public byte getUsedSlots() {
        return this.m_remainingSlots;
    }
}
