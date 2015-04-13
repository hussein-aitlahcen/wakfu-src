package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.*;

public class ResetAchievementGenericAction extends AbstractClientGenericAction
{
    public ResetAchievementGenericAction(final int id) {
        super(id);
    }
    
    @Override
    protected void runAction() {
    }
    
    @Override
    public boolean isRunnable(final CharacterInfo concernedPlayer) {
        final int achievmeentId = (int)((NumericalValue)this.getParam(0)).getLongValue(concernedPlayer, null, null, concernedPlayer.getAppropriateContext());
        return WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().canResetAchievement(achievmeentId);
    }
    
    @Override
    public boolean isEnabled(final CharacterInfo concernedPlayer) {
        return true;
    }
}
