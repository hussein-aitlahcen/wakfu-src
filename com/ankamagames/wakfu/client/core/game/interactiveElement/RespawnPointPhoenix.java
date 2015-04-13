package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RespawnPointPhoenix extends WakfuClientMapInteractiveElement
{
    static final Logger m_logger;
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUInteractifMachine action = MRUActions.RESPAWN_POINT_ACTION.getMRUAction();
        action.setGfxId(MRUGfxConstants.HEART.m_id);
        action.setTextKey("desc.mru.attachPhoenix");
        final AbstractMRUAction[] mRUActions = { action };
        return mRUActions;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("desc.phoenix");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case ACTIVATE: {
                this.runScript(action);
                WakfuGameEntity.getInstance().getLocalPlayer().getRespawnPointHandler().selectPhoenix((int)this.getId());
                this.sendActionMessage(action);
                playAPS((CharacterInfo)user);
                sendChatMessage();
                WakfuSoundManager.getInstance().playGUISound(600064L);
                return true;
            }
            default: {
                RespawnPointPhoenix.m_logger.error((Object)("Impossible d'effectuer une action " + action + " sur un phoenix"), (Throwable)new IllegalArgumentException());
                return false;
            }
        }
    }
    
    private static void playAPS(final CharacterInfo player) {
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800110);
        if (particle != null) {
            particle.setTarget(player.getActor());
            IsoParticleSystemManager.getInstance().addParticleSystem(particle);
        }
    }
    
    private static void sendChatMessage() {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("soulLinked"));
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    static {
        m_logger = Logger.getLogger((Class)RespawnPointPhoenix.class);
    }
    
    public static class RespawnPointPhoenixFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            RespawnPointPhoenix respawnPointPhoenix;
            try {
                respawnPointPhoenix = (RespawnPointPhoenix)RespawnPointPhoenixFactory.POOL.borrowObject();
                respawnPointPhoenix.setPool(RespawnPointPhoenixFactory.POOL);
            }
            catch (Exception e) {
                RespawnPointPhoenix.m_logger.error((Object)"Erreur lors de l'extraction d'un Phenix du pool", (Throwable)e);
                respawnPointPhoenix = new RespawnPointPhoenix();
            }
            return respawnPointPhoenix;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<RespawnPointPhoenix>() {
                @Override
                public RespawnPointPhoenix makeObject() {
                    return new RespawnPointPhoenix();
                }
            });
        }
    }
}
