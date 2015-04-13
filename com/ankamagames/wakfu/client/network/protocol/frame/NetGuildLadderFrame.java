package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildLadder.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class NetGuildLadderFrame extends MessageRunnerFrame
{
    protected static final Logger m_logger;
    public static final NetGuildLadderFrame INSTANCE;
    
    private NetGuildLadderFrame() {
        super(new MessageRunner[] { new GuildLadderResultMessageRunner() });
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetGuildLadderFrame.class);
        INSTANCE = new NetGuildLadderFrame();
    }
    
    private static class GuildLadderResultMessageRunner implements MessageRunner<GuildLadderConsultResultMessage>
    {
        @Override
        public boolean run(final GuildLadderConsultResultMessage msg) {
            GuildLadderView ladderView = UIGuildLadderFrame.getInstance().getGuildLadderView();
            if (ladderView == null) {
                ladderView = new GuildLadderView();
                UIGuildLadderFrame.getInstance().loadGuildLadder(ladderView);
            }
            ladderView.setGuildTotalSize(msg.getTotalGuildSize());
            ladderView.setLadderInfo(msg.getLadderInfo());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 20086;
        }
    }
}
