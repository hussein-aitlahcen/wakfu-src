package com.ankamagames.wakfu.client.core.game.chaos;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.collector.inventory.limited.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.collector.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class ChaosInteractiveElementHandler extends AbstractChaosInteractiveElementHandler implements CollectorOccupationProvider
{
    private static final Logger m_logger;
    private final IECollectorParameter m_collectorParam;
    private final ClientCollectorInventoryLimited m_collectorInventory;
    
    public ChaosInteractiveElementHandler(final ChaosInteractiveElement element) {
        super(element);
        final int collectorParamId = element.getChaosIEParameter().getChaosCollectorParamId();
        this.m_collectorParam = (IECollectorParameter)IEParametersManager.INSTANCE.getParam(IETypes.COLLECT_MACHINE, collectorParamId);
        this.m_collectorInventory = new ClientCollectorInventoryLimited(this.m_collectorParam);
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case START_BROWSING: {
                try {
                    final BasicOccupation occupation = new BrowseCollectorOccupation(this, new CollectorEventHandler() {
                        @Override
                        public void onContentMessageReceived() {
                            UICollectMachineFrame.getInstance().initialize(ChaosInteractiveElementHandler.this);
                        }
                    });
                    if (!occupation.isAllowed()) {
                        ChaosInteractiveElementHandler.m_logger.error((Object)"[IE] Impossible de d\u00e9marrer le browsing");
                        return false;
                    }
                    occupation.begin();
                    this.sendActionMessage(action);
                }
                catch (UnsupportedOperationException e) {
                    ChaosInteractiveElementHandler.m_logger.error((Object)"[IE] Impossible de d\u00e9marrer le browsing", (Throwable)e);
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
                ChaosInteractiveElementHandler.m_logger.error((Object)("Action non trait\u00e9e sur une Collector : " + action));
                return false;
            }
        }
    }
    
    @Override
    public InteractiveElementAction getDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public IECollectorParameter getInfo() {
        return this.m_collectorParam;
    }
    
    @Override
    public WakfuClientMapInteractiveElement getInteractiveElement() {
        return (WakfuClientMapInteractiveElement)this.m_element;
    }
    
    @Override
    public CollectorInventory getInventory() {
        return this.m_collectorInventory;
    }
    
    @Override
    public int getSerializedInventorySize() {
        throw new UnsupportedOperationException("Aucune serialisation dans le client");
    }
    
    @Override
    public byte[] serializeInventory() {
        throw new UnsupportedOperationException("Aucune serialisation dans le client");
    }
    
    @Override
    public void unSerializeInventory(final byte[] serializedInventory) {
        this.m_collectorInventory.unSerialize(serializedInventory);
    }
    
    @Override
    public InteractiveElementAction[] getUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    public AbstractMRUAction[] getMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_collectorParam.getVisualId());
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        action.setTextKey("desc.mru." + visual.getMruLabelKey());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public void changeState(final short state) {
        super.changeState(state);
    }
    
    @Override
    public void sendActionMessage(final InteractiveElementAction action) {
        ((InteractiveElementActionRunner)this.m_element).sendActionMessage(action);
    }
    
    @Override
    public String toString() {
        return "ChaosInteractiveElementHandler{m_collectorParam=" + this.m_collectorParam + ", m_collectorInventory=" + this.m_collectorInventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChaosInteractiveElementHandler.class);
    }
}
