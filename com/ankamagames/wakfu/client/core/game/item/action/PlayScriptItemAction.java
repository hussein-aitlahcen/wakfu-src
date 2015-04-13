package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class PlayScriptItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public PlayScriptItemAction(final int id) {
        super(id);
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            PlayScriptItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public void clear() {
        this.setCastPosition(null);
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.PLAY_SCRIPT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayScriptItemAction.class);
    }
}
