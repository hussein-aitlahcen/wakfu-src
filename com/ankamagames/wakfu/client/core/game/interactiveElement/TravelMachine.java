package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.client.core.game.travel.provider.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TravelMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    protected TravelType m_travelType;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        final TravelProvider provider = TravelHelper.getProvider(this.m_travelType);
        this.runScript(action);
        switch (action) {
            case ACTIVATE: {
                provider.activate(this, (BasicCharacterInfo)user);
                return true;
            }
            case REGISTER: {
                provider.discover(this, (BasicCharacterInfo)user);
                this.playAPS((PlayerCharacter)user);
                this.sendChatMessage();
                return true;
            }
            default: {
                TravelMachine.m_logger.error((Object)("Impossible d'effectuer une action " + action + " sur une machine de transport de type " + this.m_travelType), (Throwable)new IllegalArgumentException());
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        if (this.m_travelType == TravelType.BOAT) {
            return InteractiveElementAction.ACTIVATE;
        }
        return WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler().isTravelDiscovered(this.m_travelType, (int)this.m_id) ? InteractiveElementAction.ACTIVATE : InteractiveElementAction.REGISTER;
    }
    
    @Override
    public byte getHeight() {
        return 8;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (this.m_travelType == TravelType.BOAT) {
            return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
        }
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE, InteractiveElementAction.REGISTER };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        assert this.m_travelType == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_travelType = null;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final TravelInfo info = TravelInfoManager.INSTANCE.getInfo(this.m_travelType, this.m_id);
        final ActionVisual visual = ActionVisualManager.getInstance().get(info.getVisualId());
        final TravelHandler handler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        MRUInteractifMachine action;
        if (this.m_travelType == TravelType.BOAT || handler.isTravelDiscovered(this.m_travelType, (int)this.m_id)) {
            action = MRUActions.ZAAP_ACTION.getMRUAction();
            action.setTextKey("desc.mru." + visual.getMruLabelKey());
        }
        else {
            action = MRUActions.INTERACTIF_ACTION.getMRUAction();
            action.setTextKey("zaap.discover");
        }
        action.setGfxId(visual.getMruGfx());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        final ClientTravelProvider provider = TravelHelper.getProvider(this.m_travelType);
        return provider.getOverHeadInfo(this);
    }
    
    public TravelType getTravelType() {
        return this.m_travelType;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    private void playAPS(final PlayerCharacter player) {
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800110);
        if (particle != null) {
            particle.setTarget(player.getActor());
            IsoParticleSystemManager.getInstance().addParticleSystem(particle);
        }
    }
    
    private void sendChatMessage() {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.travel.discovered"));
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    @Nullable
    public String getTravelCostLabel() {
        final ClientTravelProvider provider = TravelHelper.getProvider(this.m_travelType);
        return provider.getTravelCostLabel(this);
    }
    
    public boolean canUse(final LocalPlayerCharacter player) {
        final ClientTravelProvider provider = TravelHelper.getProvider(this.m_travelType);
        return provider.canUse(player, this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TravelMachine.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        private final TravelType m_travelType;
        
        public Factory(final TravelType type) {
            super();
            this.m_travelType = type;
        }
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            TravelMachine travelMachine;
            try {
                travelMachine = (TravelMachine)Factory.m_pool.borrowObject();
                travelMachine.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                TravelMachine.m_logger.error((Object)"Erreur lors de l'extraction d'une DistributionMachine du pool", (Throwable)e);
                travelMachine = new TravelMachine();
            }
            travelMachine.m_travelType = this.m_travelType;
            return travelMachine;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<TravelMachine>() {
                @Override
                public TravelMachine makeObject() {
                    return new TravelMachine();
                }
            });
        }
    }
}
