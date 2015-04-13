package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.action.*;

final class TpToRespawnPointItemAction extends AbstractClientItemAction
{
    TpToRespawnPointItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        this.sendRequest(item.getUniqueId());
        final PlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player.getCitizenComportment().getPvpState().isActive()) {
            PvpInteractionManager.INSTANCE.startInteraction(new PvpInteractionHandler() {
                @Override
                public void onFinish() {
                    TpToRespawnPointItemAction.this.sendRequest(item.getUniqueId());
                }
                
                @Override
                public void onCancel() {
                }
            });
        }
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.TP_TO_RESPAWN_POINT;
    }
}
