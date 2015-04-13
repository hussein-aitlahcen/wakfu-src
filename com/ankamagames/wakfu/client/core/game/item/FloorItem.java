package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;

public class FloorItem extends DroppedItem
{
    private long m_id;
    private FloorItemInteractiveElement m_floorItemInteractiveElement;
    private Item m_item;
    private boolean m_visible;
    private long m_instigatorId;
    
    public FloorItem() {
        super();
        this.m_item = new Item();
        this.m_visible = true;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final long id) {
        this.m_id = id;
    }
    
    @Override
    public Item getItem() {
        return this.m_item;
    }
    
    public void unserialize(final byte[] datas) {
        final ByteBuffer buf = ByteBuffer.wrap(datas);
        this.m_id = buf.getLong();
        this.setCurrentFightId(buf.getInt());
        this.setInstigatorId(buf.getLong());
        final RawInventoryItem rawItem = new RawInventoryItem();
        rawItem.unserialize(buf);
        this.m_item.fromRaw(rawItem);
        final byte size = buf.get();
        for (int i = 0; i < size; ++i) {
            final byte inSize = buf.get();
            final ArrayList<Long> locks = new ArrayList<Long>(inSize);
            for (int j = 0; j < inSize; ++j) {
                locks.add(buf.getLong());
            }
            this.getLocks().add(locks);
        }
        this.setPhase(buf.get());
        this.setRemainingTicksInPhase(buf.getLong());
        final boolean hasSpecialPhase = buf.get() == 1;
        if (hasSpecialPhase) {
            final short[] temp = { buf.getShort(), buf.getShort(), buf.getShort() };
            this.setItemPhaseSpan(temp);
        }
        this.setItemSpan(buf.getShort());
    }
    
    public void unspawn() {
        this.m_floorItemInteractiveElement.remove(this);
        if (this.m_floorItemInteractiveElement.getFloorItems().size() <= 0) {
            LocalPartitionManager.getInstance().removeInteractiveElement(this.m_floorItemInteractiveElement);
        }
    }
    
    public FloorItemInteractiveElement getFloorItemInteractiveElement() {
        return this.m_floorItemInteractiveElement;
    }
    
    public void setFloorItemInteractiveElement(final FloorItemInteractiveElement floorItemInteractiveElement) {
        this.m_floorItemInteractiveElement = floorItemInteractiveElement;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public long getInstigatorId() {
        return this.m_instigatorId;
    }
    
    public void setInstigatorId(final long instigatorId) {
        this.m_instigatorId = instigatorId;
    }
    
    public int getFloorGfxId() {
        return this.m_item.getFloorGfxId();
    }
}
