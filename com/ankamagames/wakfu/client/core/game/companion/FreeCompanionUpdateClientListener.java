package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

public final class FreeCompanionUpdateClientListener implements FreeCompanionManagerListener
{
    public static final FreeCompanionUpdateClientListener INSTANCE;
    
    @Override
    public void onFreeCompanionBreedIdChanged() {
        if (!WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
            return;
        }
        UICompanionsManagementFrame.INSTANCE.loadCompanionsList();
        UICompanionsManagementFrame.INSTANCE.reflowCompanionsList();
    }
    
    static {
        INSTANCE = new FreeCompanionUpdateClientListener();
    }
}
