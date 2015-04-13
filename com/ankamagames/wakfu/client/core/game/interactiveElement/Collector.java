package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.collector.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.limited.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.free.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Collector extends WakfuClientMapInteractiveElement implements CollectorOccupationProvider
{
    private static final Logger m_logger;
    private IECollectorParameter m_info;
    private ClientCollectorInventory m_inventory;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        switch (action) {
            case START_BROWSING: {
                try {
                    final BrowseCollectorOccupation occupation = new BrowseCollectorOccupation(this, new CollectorEventHandler() {
                        @Override
                        public void onContentMessageReceived() {
                            UICollectMachineFrame.getInstance().initialize(Collector.this);
                        }
                    });
                    if (!occupation.isAllowed()) {
                        Collector.m_logger.error((Object)"[IE] Impossible de d\u00e9marrer le browsing");
                        return false;
                    }
                    occupation.begin();
                    this.sendActionMessage(action);
                }
                catch (UnsupportedOperationException e) {
                    Collector.m_logger.error((Object)"[IE] Impossible de d\u00e9marrer le browsing", (Throwable)e);
                    return false;
                }
                return true;
            }
            case STOP_BROWSING: {
                WakfuGameEntity.getInstance().removeFrame(NetCollectorMessageFrame.INSTANCE);
                this.sendActionMessage(action);
                WakfuGameEntity.getInstance().getLocalPlayer().finishCurrentOccupation();
                return true;
            }
            default: {
                Collector.m_logger.error((Object)("Action non trait\u00e9e sur une Collector : " + action));
                return false;
            }
        }
    }
    
    @Override
    public CollectorInventory getInventory() {
        return (CollectorInventory)this.m_inventory;
    }
    
    @Override
    public IECollectorParameter getInfo() {
        return this.m_info;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(86, this.m_info.getId(), new Object[0]);
    }
    
    @Override
    public void unSerializeInventory(final byte[] content) {
        this.m_inventory.unSerialize(content);
    }
    
    @Override
    public WakfuClientMapInteractiveElement getInteractiveElement() {
        return this;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_info.getVisualId());
        final AbstractMRUAction[] mRUActions = { null };
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        action.setTextKey("desc.mru." + visual.getMruLabelKey());
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public void initializeWithParameter() {
        this.m_info = (IECollectorParameter)IEParametersManager.INSTANCE.getParam(IETypes.COLLECT_MACHINE, Integer.valueOf(this.m_parameter));
        this.m_inventory = (this.m_info.hasExpected() ? new ClientCollectorInventoryLimited(this.m_info) : new ClientCollectorInventoryFree(this.m_info));
        this.setOverHeadable(true);
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(true);
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_info = null;
        this.m_inventory = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        assert this.m_info == null;
        assert this.m_inventory == null;
    }
    
    @Override
    public String toString() {
        return "Collector : \r\nm_info=" + this.m_info + "\r\n" + "m_inventory=" + this.m_inventory;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Collector.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public Collector makeObject() {
            Collector machine;
            try {
                machine = (Collector)Factory.m_pool.borrowObject();
                machine.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                Collector.m_logger.error((Object)"Erreur lors de l'extraction d'une Collector du pool", (Throwable)e);
                machine = new Collector();
            }
            return machine;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<Collector>() {
                @Override
                public Collector makeObject() {
                    return new Collector();
                }
            });
        }
    }
}
