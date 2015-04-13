package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon.*;
import com.ankamagames.wakfu.client.core.dungeon.*;
import java.nio.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.dungeon.*;

public class NetDungeonFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static NetDungeonFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 15950: {
                final DungeonLadderInformationMessage ladderMessage = (DungeonLadderInformationMessage)message;
                this.onLadderInformationMessage(ladderMessage);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onLadderInformationMessage(final DungeonLadderInformationMessage message) {
        final short instanceId = message.getInstanceId();
        final byte[] serializedResults = message.getSerializedLadderResults();
        final DungeonLadderDefinition ladderDefinition = DungeonLadderDefinitionManager.INSTANCE.get(instanceId);
        final DungeonLadder ladder = new DungeonLadder(ladderDefinition);
        ladder.unserializeResults(ByteBuffer.wrap(serializedResults));
        DungeonLadderManager.INSTANCE.registerLadder(ladder);
        NetDungeonFrame.m_logger.info((Object)("Reception des infos de ladder d'un donjon : " + ladder));
        UIDungeonLadderFrame.getInstance().loadDungeonLadder(ladder);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetDungeonFrame.class);
        NetDungeonFrame.INSTANCE = new NetDungeonFrame();
    }
}
