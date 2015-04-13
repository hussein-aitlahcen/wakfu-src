package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class MRUFollowPlayerAction extends AbstractMRUAction
{
    private static final Logger m_logger;
    
    @Override
    public MRUActions tag() {
        return MRUActions.FOLLOW_PLAYER;
    }
    
    @Override
    public String getLabel() {
        if (!(this.m_source instanceof CharacterInfo)) {
            return null;
        }
        final CharacterInfo character = (CharacterInfo)this.m_source;
        final TextWidgetFormater sb = new TextWidgetFormater().b().append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey(), character.getName()))._b();
        return sb.finishAndToString();
    }
    
    @Override
    public String getTranslatorKey() {
        return "followPlayer";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.FOLLOW.m_id;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUFollowPlayerAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final PlayerCharacter player = (PlayerCharacter)this.m_source;
        if (player.getActor().isVisible()) {
            WakfuGameEntity.getInstance().pushFrame(new FollowPlayerFrame(player));
        }
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
    
    @Override
    public boolean isRunnable() {
        if (this.m_source == null) {
            return false;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isWaitingForResult()) {
            return false;
        }
        if (localPlayer.isOnFight()) {
            return false;
        }
        if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
            return false;
        }
        if (localPlayer.isActiveProperty(WorldPropertyType.FOLLOW_PLAYER_DISABLED)) {
            return false;
        }
        final PlayerCharacter source = (PlayerCharacter)this.m_source;
        return !source.isOnFight() && !source.isActiveProperty(WorldPropertyType.FOLLOW_PLAYER_DISABLED);
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUFollowPlayerAction();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MRUFollowPlayerAction.class);
    }
    
    private static class FollowPlayerFrame implements MessageFrame
    {
        private final PlayerCharacter m_target;
        private TargetPositionListener<PathMobile> m_targetMovedListener;
        private CharacterFightListener m_fightListener;
        
        FollowPlayerFrame(final PlayerCharacter player) {
            super();
            this.m_target = player;
        }
        
        @Override
        public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_targetMovedListener = new TargetPositionListener<PathMobile>() {
                @Override
                public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
                    if (FollowPlayerFrame.this.m_target.isOnFight()) {
                        WakfuGameEntity.getInstance().removeFrame(FollowPlayerFrame.this);
                        return;
                    }
                    if (localPlayer.getPositionConst().getDistance(worldX, worldY, altitude) > 1) {
                        localPlayer.moveNearTarget(FollowPlayerFrame.this.m_target, true, true);
                    }
                }
            };
            this.m_fightListener = new CharacterFightListener() {
                @Override
                public void onJoinFight(final BasicCharacterInfo character) {
                    WakfuGameEntity.getInstance().removeFrame(FollowPlayerFrame.this);
                }
                
                @Override
                public void onLeaveFight(final BasicCharacterInfo character) {
                }
                
                @Override
                public void onWonFight(final BasicCharacterInfo character) {
                }
                
                @Override
                public void onLoseFight(final BasicCharacterInfo character) {
                }
            };
            this.m_target.getActor().addPositionListener(this.m_targetMovedListener);
            localPlayer.addCharacterFightListener(this.m_fightListener);
        }
        
        @Override
        public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
            this.m_target.getActor().removePositionListener(this.m_targetMovedListener);
            WakfuGameEntity.getInstance().getLocalPlayer().removeCharacterFightListener(this.m_fightListener);
            this.m_targetMovedListener = null;
            this.m_fightListener = null;
        }
        
        @Override
        public boolean onMessage(final Message message) {
            switch (message.getId()) {
                case 19992:
                case 19995: {
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
        
        @Override
        public long getId() {
            return 0L;
        }
        
        @Override
        public void setId(final long id) {
        }
    }
}
