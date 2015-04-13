package com.ankamagames.wakfu.client.core.action.fight;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public final class ItemPickedUpByOtherAction extends AbstractFightTimedAction
{
    private long m_itemUid;
    
    public ItemPickedUpByOtherAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final ItemPickedUpByOtherMessage msg) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_itemUid = msg.getItemId();
    }
    
    @Override
    protected long onRun() {
        final FloorItem floorItem = FloorItemManager.getInstance().getFloorItem(this.m_itemUid);
        floorItem.unspawn();
        return 0L;
    }
}
