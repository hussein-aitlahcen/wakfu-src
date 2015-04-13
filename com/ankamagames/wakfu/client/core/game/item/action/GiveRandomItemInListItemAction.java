package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class GiveRandomItemInListItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_lootListId;
    
    public GiveRandomItemInListItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_lootListId = Integer.parseInt(params[0]);
    }
    
    @Override
    public boolean run(final Item item) {
        final WakfuGameEntity wakfuGameEntity = WakfuGameEntity.getInstance();
        if (wakfuGameEntity.hasFrame(UIBlindBoxFrame.getInstance())) {
            GiveRandomItemInListItemAction.m_logger.error((Object)"Ouille on tente de lancer deux blind box en m\u00eame temps !!!");
            return false;
        }
        UIBlindBoxFrame.getInstance().initialize(this, item);
        wakfuGameEntity.pushFrame(UIBlindBoxFrame.getInstance());
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    public void delayedRequest(final long itemId) {
        this.sendRequest(itemId);
    }
    
    public int getLootListId() {
        return this.m_lootListId;
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.GIVE_RANDOM_ITEM_IN_LIST;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GiveRandomItemInListItemAction.class);
    }
}
