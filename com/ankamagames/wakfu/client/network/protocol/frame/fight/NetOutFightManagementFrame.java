package com.ankamagames.wakfu.client.network.protocol.frame.fight;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public final class NetOutFightManagementFrame implements MessageFrame
{
    private static NetOutFightManagementFrame INSTANCE;
    final FightManagementFrame<ExternalFightInfo> m_messageFrame;
    
    public static NetOutFightManagementFrame getInstance() {
        return NetOutFightManagementFrame.INSTANCE;
    }
    
    private NetOutFightManagementFrame() {
        super();
        this.m_messageFrame = new FightManagementFrame<ExternalFightInfo>();
        this.addCommonHandlers();
        this.addOutFightHandlers();
    }
    
    private void addCommonHandlers() {
        CommonHandlersFactory.createHandlerForThisFrame(4522, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4524, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4506, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8120, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4528, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4520, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8122, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(6204, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4123, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8124, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8200, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8412, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(6200, this.m_messageFrame);
    }
    
    private void addOutFightHandlers() {
        OutFightHandlersFactory.createHandlerForThisFrame(7906, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8150, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8004, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(4126, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(4114, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(4122, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8154, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8202, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8300, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8006, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8000, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8038, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8302, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8108, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(7904, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(7902, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8002, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8028, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8026, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(4300, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(8110, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(7998, this.m_messageFrame);
        OutFightHandlersFactory.createHandlerForThisFrame(4170, this.m_messageFrame);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return this.m_messageFrame.onMessage(message);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        NetOutFightManagementFrame.INSTANCE = new NetOutFightManagementFrame();
    }
}
