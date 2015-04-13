package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class FollowAchievementItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    public FollowAchievementItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.FOLLOW_ACHIEVEMENT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FollowAchievementItemAction.class);
    }
}
