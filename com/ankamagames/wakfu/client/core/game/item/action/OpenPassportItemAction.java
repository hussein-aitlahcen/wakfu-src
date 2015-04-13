package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class OpenPassportItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public OpenPassportItemAction(final int id) {
        super(id);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            OpenPassportItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        if (!WakfuGameEntity.getInstance().hasFrame(UIPassportFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIPassportFrame.getInstance());
        }
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.OPEN_PASSPORT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)OpenPassportItemAction.class);
    }
}
