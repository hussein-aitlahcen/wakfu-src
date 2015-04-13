package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dungeon.*;
import com.ankamagames.wakfu.common.game.dungeon.challenge.*;

public final class NetArcadeDungeonFrame extends MessageRunnerFrame
{
    static final Logger m_logger;
    public static final NetArcadeDungeonFrame INSTANCE;
    
    private NetArcadeDungeonFrame() {
        super(new MessageRunner[] { new DungeonRoundMessageManager(), new DungeonWaveMessageManager(), new DungeonUserMessageManager(), new DungeonEventWonMessageManager(), new DungeonChallengeMessageManager() });
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
        m_logger = Logger.getLogger((Class)NetArcadeDungeonFrame.class);
        INSTANCE = new NetArcadeDungeonFrame();
    }
    
    private static class DungeonRoundMessageManager implements MessageRunner<DungeonRoundMessage>
    {
        @Override
        public boolean run(final DungeonRoundMessage msg) {
            UIArcadeDungeonFrame.getInstance().onRoundBegin();
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15952;
        }
    }
    
    private static class DungeonWaveMessageManager implements MessageRunner<DungeonWaveMessage>
    {
        @Override
        public boolean run(final DungeonWaveMessage msg) {
            UIArcadeDungeonFrame.getInstance().onNewWave();
            if (msg.getBonusId() != -1) {
                final String title = WakfuTranslator.getInstance().getString("notification.arcadeDungeonBonusTitle");
                final String text = WakfuTranslator.getInstance().getString("notification.arcadeDungeonBonusText", ArcadeDungeonLoader.INSTANCE.getBonusName(msg.getBonusId()));
                Worker.getInstance().pushMessage(new UINotificationMessage(title, text, NotificationMessageType.DUNGEON_LADDER));
            }
            final int eventId = msg.getEventId();
            if (eventId != -1) {
                final String title2 = WakfuTranslator.getInstance().getString("notification.arcadeDungeonEventTitle");
                final String text2 = WakfuTranslator.getInstance().getString("notification.arcadeDungeonEventText", ArcadeDungeonLoader.INSTANCE.getEventName(eventId));
                Worker.getInstance().pushMessage(new UINotificationMessage(title2, text2, NotificationMessageType.DUNGEON_LADDER));
                UIArcadeDungeonFrame.getInstance().displaySplashText(ArcadeDungeonLoader.INSTANCE.getEventDesc(eventId));
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15954;
        }
    }
    
    private static class DungeonUserMessageManager implements MessageRunner<DungeonUserMessage>
    {
        @Override
        public boolean run(final DungeonUserMessage msg) {
            UIArcadeDungeonFrame.getInstance().loadDungeon(msg.getDungeonDefinitionId());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15956;
        }
    }
    
    private static class DungeonEventWonMessageManager implements MessageRunner<DungeonEventWonMessage>
    {
        @Override
        public boolean run(final DungeonEventWonMessage msg) {
            UIArcadeDungeonFrame.getInstance().onEventSucceeded(msg.getEventId());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15958;
        }
    }
    
    private static class DungeonChallengeMessageManager implements MessageRunner<DungeonChallengeMessage>
    {
        @Override
        public boolean run(final DungeonChallengeMessage msg) {
            final int challengeId = msg.getChallengeId();
            switch (msg.getStatus()) {
                case FAIL: {
                    UIArcadeDungeonFrame.getInstance().onChallengeFailed(challengeId);
                    break;
                }
                case SUCCESS: {
                    UIArcadeDungeonFrame.getInstance().onChallengeSucceeded(challengeId);
                    break;
                }
                case PENDING: {
                    UIArcadeDungeonFrame.getInstance().onChallengePending(challengeId);
                    break;
                }
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 15960;
        }
    }
}
