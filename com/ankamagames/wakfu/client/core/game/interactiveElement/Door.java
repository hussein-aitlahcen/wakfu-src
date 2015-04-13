package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;

public class Door extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    public static final short OPENED = 0;
    public static final short CLOSED = 1;
    public static final short LOCKED = 2;
    private AccessType m_accessType;
    private boolean m_displayBehindMobileWhenOpen;
    private boolean m_displayBehindMobileWhenClose;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_useSpecificAnimTransition = true;
        this.setState((short)1);
        this.setOldState((short)1);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.m_accessType = AccessType.ALL;
        this.m_displayBehindMobileWhenOpen = false;
        this.m_displayBehindMobileWhenClose = false;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!this.m_accessType.canUse(this, localPlayer)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        int gfxId = 0;
        String mruText = null;
        switch (this.m_state) {
            case 0: {
                gfxId = MRUGfxConstants.PADLOCK.m_id;
                mruText = "close";
                break;
            }
            case 1:
            case 2: {
                gfxId = MRUGfxConstants.PADLOCK_OPEN.m_id;
                mruText = "containerOpen";
                break;
            }
            default: {
                gfxId = MRUGfxConstants.PADLOCK_OPEN.m_id;
                mruText = "containerOpen";
                break;
            }
        }
        action.setGfxId(gfxId);
        action.setTextKey("desc.mru." + mruText);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        Door.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        Door.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        if (!this.m_usable) {
            return false;
        }
        if (!this.m_accessType.canUse(this, user)) {
            return false;
        }
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    @Override
    public void setUsable(final boolean usable) {
        super.setUsable(usable);
        this.setSelectable(usable);
    }
    
    @Override
    public boolean checkSubscription() {
        return true;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = this.m_parameter.split(";");
        if (params.length < 0 || params.length > 3) {
            Door.m_logger.error((Object)("[LD] La porte " + this.m_id + " doit avoir 0, 1, 2 ou 3 param\u00e8tre(s)"));
            return;
        }
        if (params.length == 0) {
            this.m_accessType = AccessType.ALL;
            this.m_displayBehindMobileWhenOpen = false;
            this.m_displayBehindMobileWhenClose = false;
            return;
        }
        if (params.length == 1) {
            try {
                this.m_accessType = AccessType.valueOf(params[0].toUpperCase());
            }
            catch (IllegalArgumentException e) {
                this.m_accessType = AccessType.ALL;
            }
            this.m_displayBehindMobileWhenOpen = false;
            this.m_displayBehindMobileWhenClose = false;
        }
        if (params.length == 2) {
            this.m_accessType = AccessType.ALL;
            this.m_displayBehindMobileWhenOpen = Boolean.parseBoolean(params[0]);
            this.m_displayBehindMobileWhenClose = Boolean.parseBoolean(params[1]);
        }
        if (params.length == 3) {
            this.m_accessType = AccessType.valueOf(params[0].toUpperCase());
            this.m_displayBehindMobileWhenOpen = Boolean.parseBoolean(params[1]);
            this.m_displayBehindMobileWhenClose = Boolean.parseBoolean(params[2]);
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return (this.m_state == 1 || this.m_state == 2) ? InteractiveElementAction.OPEN : InteractiveElementAction.CLOSE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.OPEN, InteractiveElementAction.CLOSE, InteractiveElementAction.LOCK, InteractiveElementAction.UNLOCK };
    }
    
    @Override
    protected void onStateChanged() {
        super.onStateChanged();
        switch (this.m_state) {
            case 0: {
                this.setBlockingLineOfSight(false);
                this.setBlockingMovements(false);
                this.setViewBehind(this.m_displayBehindMobileWhenOpen);
                break;
            }
            case 1:
            case 2: {
                this.setBlockingLineOfSight(true);
                this.setBlockingMovements(true);
                this.setViewBehind(this.m_displayBehindMobileWhenClose);
                break;
            }
        }
    }
    
    private void setViewBehind(final boolean behind) {
        for (final ClientInteractiveElementView view : this.getViews()) {
            view.setBehindMobile(behind);
        }
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new DoorItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Door.class);
    }
    
    public static class DoorFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Door door;
            try {
                door = (Door)DoorFactory.m_pool.borrowObject();
                door.setPool(DoorFactory.m_pool);
            }
            catch (Exception e) {
                Door.m_logger.error((Object)"Erreur lors de l'extraction d'un Door du pool", (Throwable)e);
                door = new Door();
            }
            return door;
        }
        
        static {
            DoorFactory.m_pool = new MonitoredPool(new ObjectFactory<Door>() {
                @Override
                public Door makeObject() {
                    return new Door();
                }
            });
        }
    }
    
    private enum AccessType
    {
        ALL {
            @Override
            public boolean canUse(final Door door, final InteractiveElementUser user) {
                return true;
            }
        }, 
        GUILD {
            @Override
            public boolean canUse(final Door door, final InteractiveElementUser user) {
                if (!(user instanceof LocalPlayerCharacter)) {
                    return false;
                }
                final LocalPlayerCharacter player = (LocalPlayerCharacter)user;
                final short instanceId = player.getInstanceId();
                final ClientGuildInformationHandler guildHandler = (ClientGuildInformationHandler)((GuildUser)user).getGuildHandler();
                final DimensionalBagView visitingDimentionalBag = player.getVisitingDimentionalBag();
                final boolean isInOwnBag = visitingDimentionalBag != null && visitingDimentionalBag.getOwnerId() == player.getId();
                if (isInOwnBag) {
                    return true;
                }
                final boolean isInGuildMemberBag = visitingDimentionalBag != null && guildHandler.getMember(visitingDimentionalBag.getOwnerId()) != null;
                final boolean isInOwnHavenWorld = isInOwnHavenWorld(instanceId, guildHandler);
                return isInOwnHavenWorld || isInGuildMemberBag;
            }
        };
        
        private static boolean isInOwnHavenWorld(final short instanceId, final ClientGuildInformationHandler guildHandler) {
            final HavenWorldDefinition worldDefinition = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
            return worldDefinition != null && worldDefinition.getId() == guildHandler.getHavenWorldId();
        }
        
        public abstract boolean canUse(final Door p0, final InteractiveElementUser p1);
    }
}
