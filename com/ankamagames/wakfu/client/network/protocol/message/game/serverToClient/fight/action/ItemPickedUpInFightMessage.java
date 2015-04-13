package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public class ItemPickedUpInFightMessage extends AbstractFightActionMessage
{
    private Long m_itemId;
    private long m_inventoryId;
    private boolean m_success;
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.ITEM_PICKED_UP;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_itemId = bb.getLong();
        this.m_inventoryId = bb.getLong();
        this.m_success = (bb.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 8034;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public long getInventoryId() {
        return this.m_inventoryId;
    }
    
    public boolean isSuccess() {
        return this.m_success;
    }
}
