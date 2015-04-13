package com.ankamagames.baseImpl.graphics.network;

import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.core.*;
import com.ankamagames.framework.kernel.events.*;

public class GameNetworkEntity extends NetworkEntity
{
    private GameEntity m_gameEntity;
    
    public GameNetworkEntity(final GameEntity gameEntity) {
        super();
        this.m_gameEntity = gameEntity;
    }
    
    @Override
    public void onConnect() {
        this.m_gameEntity.setNetworkEntity(this);
        this.pushFrame(new NetForwardToGameEntityFrame(this.m_gameEntity));
    }
}
