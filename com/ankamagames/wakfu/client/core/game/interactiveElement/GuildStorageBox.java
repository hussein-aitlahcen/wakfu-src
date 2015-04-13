package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GuildStorageBox extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case START_BROWSING: {
                this.runScript(action);
                final BasicOccupation occupation = new BrowseGuildStorageBoxOccupation(this);
                if (!occupation.isAllowed()) {
                    GuildStorageBox.m_logger.error((Object)"[STORAGE_BOX] Impossible de d\u00e9marrer le browsing");
                    return false;
                }
                occupation.begin();
                this.sendActionMessage(action);
                WakfuGameEntity.getInstance().pushFrame(UIStorageBoxFrame.getInstance());
                return true;
            }
            case STOP_BROWSING: {
                this.runScript(action);
                if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
                }
                this.sendActionMessage(action);
                WakfuGameEntity.getInstance().getLocalPlayer().finishCurrentOccupation();
                return true;
            }
            default: {
                GuildStorageBox.m_logger.error((Object)("Impossible d'effectuer l'action " + action), (Throwable)new IllegalArgumentException());
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.setUseSpecificAnimTransition(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
        this.setOverHeadable(false);
        this.setUseSpecificAnimTransition(false);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUGuildStorageBoxAction action = MRUActions.GUILD_STORAGE_BOX_ACTION.getMRUAction();
        action.setGfxId(MRUGfxConstants.HAND.m_id);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("guild.storageBox");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildStorageBox.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            GuildStorageBox ie;
            try {
                ie = (GuildStorageBox)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                GuildStorageBox.m_logger.error((Object)"Erreur lors de l'extraction du pool", (Throwable)e);
                ie = new GuildStorageBox();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new PoolFactory());
        }
    }
    
    private static class PoolFactory extends ObjectFactory<GuildStorageBox>
    {
        @Override
        public GuildStorageBox makeObject() {
            return new GuildStorageBox();
        }
    }
}
