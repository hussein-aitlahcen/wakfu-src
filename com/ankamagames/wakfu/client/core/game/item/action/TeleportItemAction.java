package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class TeleportItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public TeleportItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            TeleportItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.sendRequest(item.getUniqueId());
        final PlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player.getCitizenComportment().getPvpState().isActive()) {
            PvpInteractionManager.INSTANCE.startInteraction(new PvpInteractionHandler() {
                @Override
                public void onFinish() {
                    TeleportItemAction.this.sendRequest(item.getUniqueId());
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
        return ItemActionConstants.TELEPORT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TeleportItemAction.class);
    }
}
