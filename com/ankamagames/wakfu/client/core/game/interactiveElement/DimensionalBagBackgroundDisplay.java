package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagBackgroundDisplay extends AbstractItemizableInteractiveElement
{
    private static final Logger m_logger;
    public static final short STATE_NORMAL = 1;
    private int m_linkedItemReferenceId;
    protected IEDimensionalBagBackgroundDisplayParameter m_backgroundParameters;
    
    @Override
    protected void unserializeSpecificSharedData(final ByteBuffer buffer) {
        this.m_linkedItemReferenceId = buffer.getInt();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setVisible(true);
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(true);
        this.m_linkedItemReferenceId = -1;
        assert this.m_backgroundParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_backgroundParameters = null;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        final GemType[] array;
        if (this.m_backgroundParameters == null) {
            array = new GemType[] { GemType.GEM_ID_DECORATION };
        }
        else {
            this.m_backgroundParameters.getAllowedGemTypes();
        }
        return array;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.ACTIVATE) {
            final UIBackgroundDisplayFrame displayFrame = UIBackgroundDisplayFrame.getInstance();
            displayFrame.loadBackgroundDisplay(this.m_backgroundParameters.getBackgroundDisplayId());
            WakfuGameEntity.getInstance().pushFrame(displayFrame);
            return true;
        }
        return super.onAction(action, user);
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            DimensionalBagBackgroundDisplay.m_logger.error((Object)("[LD] L'IE de DimensionalBagBackgroundDisplay " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        final IEDimensionalBagBackgroundDisplayParameter param = (IEDimensionalBagBackgroundDisplayParameter)IEParametersManager.INSTANCE.getParam(IETypes.DIMENSIONAL_BAG_BACKGROUND_DISPLAY, Integer.valueOf(params[0]));
        if (param == null) {
            DimensionalBagBackgroundDisplay.m_logger.error((Object)("[LD] L'IE de DimensionalBagBackgroundDisplay " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_backgroundParameters = param;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    protected InteractiveElementAction[] getAdditionalUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    protected AbstractMRUAction[] getAdditionalMRUActions() {
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(MRUGfxConstants.BOOK.m_id);
        action.setTextKey("desc.mru.lookAt");
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public boolean isOverHeadable() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_linkedItemReferenceId);
        return referenceItem != null;
    }
    
    @Override
    public String getName() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_linkedItemReferenceId);
        if (referenceItem != null) {
            return referenceItem.getName();
        }
        return "<UNKNOWN>";
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.BACKGROUND_DISPLAY;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagBackgroundDisplay.class);
    }
    
    public static class BackgroundDisplayFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagBackgroundDisplay element;
            try {
                element = (DimensionalBagBackgroundDisplay)BackgroundDisplayFactory.m_pool.borrowObject();
                element.setPool(BackgroundDisplayFactory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagBackgroundDisplay.m_logger.error((Object)("Erreur lors de l'extraction d'un " + DimensionalBagBackgroundDisplay.class.getName() + " du pool"), (Throwable)e);
                element = new DimensionalBagBackgroundDisplay();
            }
            return element;
        }
        
        static {
            BackgroundDisplayFactory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagBackgroundDisplay>() {
                @Override
                public DimensionalBagBackgroundDisplay makeObject() {
                    return new DimensionalBagBackgroundDisplay();
                }
            });
        }
    }
}
