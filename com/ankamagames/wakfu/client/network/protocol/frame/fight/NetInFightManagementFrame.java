package com.ankamagames.wakfu.client.network.protocol.frame.fight;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;

public final class NetInFightManagementFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static NetInFightManagementFrame INSTANCE;
    final FightManagementFrame<Fight> m_messageFrame;
    private Fight m_associatedFight;
    
    public static NetInFightManagementFrame getInstance() {
        return NetInFightManagementFrame.INSTANCE;
    }
    
    private NetInFightManagementFrame() {
        super();
        this.m_messageFrame = new FightManagementFrame<Fight>();
        this.createCommonHandlers();
        this.createInFightHandlers();
    }
    
    private void createCommonHandlers() {
        CommonHandlersFactory.createHandlerForThisFrame(8200, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8010, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(202, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4300, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8014, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8002, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8028, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8304, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8410, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8106, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4122, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4123, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8120, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8124, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8122, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(6200, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8110, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8116, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8108, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4506, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4524, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4528, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4520, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(6204, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(4522, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8302, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8034, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8033, this.m_messageFrame);
        CommonHandlersFactory.createHandlerForThisFrame(8412, this.m_messageFrame);
    }
    
    private void createInFightHandlers() {
        InFightHandlersFactory.createHandlerForThisFrame(8202, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8308, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4127, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8158, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8156, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4508, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8040, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8104, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8100, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4214, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8300, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8114, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8030, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8026, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8150, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8012, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8000, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4126, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4124, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(4170, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(5240, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8016, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8310, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8042, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8043, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8044, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8046, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8045, this.m_messageFrame);
        InFightHandlersFactory.createHandlerForThisFrame(8415, this.m_messageFrame);
    }
    
    public void associateFight(final Fight fight) {
        this.m_associatedFight = fight;
        this.m_messageFrame.associateFight(fight);
    }
    
    public Fight getAssociatedFight() {
        return this.m_associatedFight;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        NetInFightManagementFrame.m_logger.info((Object)"NetInFight Added");
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        NetInFightManagementFrame.m_logger.info((Object)"NetInFight Removed");
        this.associateFight(null);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (this.m_associatedFight == null) {
            NetInFightManagementFrame.m_logger.error((Object)("[FIGHT] La NetInFightManagementFrame re\u00e7oit un message a traiter alors qu'aucun combat ne lui est associ\u00e9 : " + message.getClass().getSimpleName()));
            return true;
        }
        return (message instanceof AbstractFightMessage && ((AbstractFightMessage)message).getFightId() != this.m_associatedFight.getId()) || this.m_messageFrame.onMessage(message);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetInFightManagementFrame.class);
        NetInFightManagementFrame.INSTANCE = new NetInFightManagementFrame();
    }
}
