package com.ankamagames.wakfu.client.network.protocol.frame.fight;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.spectator.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.framework.kernel.*;

public final class NetSpectatorFightFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static NetSpectatorFightFrame INSTANCE;
    final FightManagementFrame<Fight> m_messageFrame;
    private Fight m_associatedFight;
    
    private NetSpectatorFightFrame() {
        super();
        this.m_messageFrame = new FightManagementFrame<Fight>();
        this.addSpectatorHandlers();
        this.addCommonHandlers();
    }
    
    public static NetSpectatorFightFrame getInstance() {
        return NetSpectatorFightFrame.INSTANCE;
    }
    
    private void addSpectatorHandlers() {
        SpectatorModeHandlersFactory.createHandlerForThisFrame(8104, this.m_messageFrame);
        SpectatorModeHandlersFactory.createHandlerForThisFrame(8100, this.m_messageFrame);
        SpectatorModeHandlersFactory.createHandlerForThisFrame(8300, this.m_messageFrame);
    }
    
    private void addCommonHandlers() {
        CommonHandlersFactory.createHandlerForThisFrame(202, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8010, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8304, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8106, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8108, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8014, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8302, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4300, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8028, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8002, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8110, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8116, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4122, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8200, this.m_messageFrame);
    }
    
    public void associateFight(final Fight fight) {
        this.m_associatedFight = fight;
        this.m_messageFrame.associateFight(fight);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (this.m_associatedFight == null) {
            NetSpectatorFightFrame.m_logger.error((Object)("[FIGHT] La NetSpectatorFightFrame re\u00e7oit un message a traiter alors qu'aucun combat ne lui est associ\u00e9 : " + message.getClass().getSimpleName()));
            return true;
        }
        return (message instanceof AbstractFightMessage && ((AbstractFightMessage)message).getFightId() != this.m_associatedFight.getId()) || this.m_messageFrame.onMessage(message);
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
        m_logger = Logger.getLogger((Class)NetSpectatorFightFrame.class);
        NetSpectatorFightFrame.INSTANCE = new NetSpectatorFightFrame();
    }
}
