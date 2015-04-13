package com.ankamagames.wakfu.client.core.game.exchange;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ExchangeUserUpdater implements ExchangerUserListener
{
    private final ItemTrade m_exchanger;
    
    public ExchangeUserUpdater(final ItemTrade exchanger) {
        super();
        this.m_exchanger = exchanger;
    }
    
    @Override
    public void onReadyChanged(final ExchangerUser user) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (user.getId() == localPlayer.getId()) {
            this.m_exchanger.updateLocalReadyProperties();
        }
        else {
            this.m_exchanger.updateRemoteReadyProperties();
        }
    }
}
