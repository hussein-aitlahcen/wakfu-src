package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public class ItemPickedUpByOtherMessage extends AbstractFightActionMessage
{
    private Long m_itemId;
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.ITEM_PICKED_UP_BY_OTHER;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(bb);
        this.m_itemId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 8033;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
}
