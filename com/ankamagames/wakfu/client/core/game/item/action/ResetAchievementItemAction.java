package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class ResetAchievementItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_achievementId;
    
    public ResetAchievementItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_achievementId = Integer.parseInt(params[0]);
    }
    
    @Override
    public boolean isRunnable(final Item item) {
        if (!super.isRunnable(item)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientAchievementsContext achievementsContext = localPlayer.getAchievementsContext();
        return !achievementsContext.isAchievementActive(this.m_achievementId) || achievementsContext.isAchievementComplete(this.m_achievementId);
    }
    
    @Override
    public String getAdditionalRequirementDescription() {
        return WakfuTranslator.getInstance().getString("item.action.resetAchievement", WakfuTranslator.getInstance().getString(62, this.m_achievementId, new Object[0]));
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
        return ItemActionConstants.RESET_ACHIEVEMENT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ResetAchievementItemAction.class);
    }
}
