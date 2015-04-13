package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public final class NetGuildFrame extends MessageRunnerFrame
{
    protected static final Logger m_logger;
    public static final NetGuildFrame INSTANCE;
    
    private NetGuildFrame() {
        super(new MessageRunner[] { new GuildChangeMessageRunner(), new GuildErrorMessageRunner(), new GuildInvitationMessageRunner(), new GuildBonusLearnedMessageRunner(), new HavenWorldMessageRunner(), new HavenWorldGuildNotifyBuildingAddedMessageRunner(), new HavenWorldGuildNotifyBuildingRemovedMessageRunner(), new HavenWorldGuildNotifyBuildingEvolvedMessageRunner(), new GuildObtainHavenWorldMessageRunner() });
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
        m_logger = Logger.getLogger((Class)NetGuildFrame.class);
        INSTANCE = new NetGuildFrame();
    }
}
