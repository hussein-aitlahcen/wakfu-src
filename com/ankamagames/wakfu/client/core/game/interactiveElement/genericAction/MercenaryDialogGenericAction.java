package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MercenaryDialogGenericAction extends AbstractClientGenericAction
{
    MercenaryDialogGenericAction(final int id) {
        super(id);
    }
    
    @Override
    protected void runAction() {
        if (!WakfuGameEntity.getInstance().hasFrame(UIMercenaryFrame.INSTANCE)) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final int achievementCategory = (int)((NumericalValue)this.getParam(0)).getLongValue(localPlayer, null, null, localPlayer.getAppropriateContext());
            UIMercenaryFrame.INSTANCE.initFrame(achievementCategory);
            WakfuGameEntity.getInstance().pushFrame(UIMercenaryFrame.INSTANCE);
        }
    }
    
    @Override
    public boolean isRunnable(final CharacterInfo concernedPlayer) {
        return true;
    }
    
    @Override
    public boolean isEnabled(final CharacterInfo concernedPlayer) {
        return true;
    }
}
