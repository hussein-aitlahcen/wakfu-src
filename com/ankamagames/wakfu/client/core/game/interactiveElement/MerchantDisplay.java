package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MerchantDisplay extends AbstractItemizableInteractiveElement
{
    private static final Logger m_logger;
    public static final short STATE_NORMAL = 1;
    private int m_displayRefId;
    private long m_merchantInventoryUid;
    private short m_itemCount;
    private boolean m_marketregistered;
    
    @Override
    protected void unserializeSpecificSharedData(final ByteBuffer buffer) {
        this.m_merchantInventoryUid = buffer.getLong();
        this.m_displayRefId = buffer.getInt();
        this.m_itemCount = buffer.getShort();
        this.m_marketregistered = (buffer.get() == 1);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setState((short)1);
        this.setVisible(true);
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(true);
        this.m_displayRefId = 0;
        this.m_merchantInventoryUid = 0L;
        this.m_itemCount = 0;
        this.m_marketregistered = false;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean canBeRepacked() {
        return this.isEmpty();
    }
    
    public boolean isEmpty() {
        return this.m_itemCount == 0;
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return new GemType[] { GemType.GEM_ID_MERCHANT };
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        switch (action) {
            case ASK_INFORMATIONS:
            case START_MANAGE:
            case STOP_MANAGE: {
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return super.onAction(action, user);
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    protected InteractiveElementAction[] getAdditionalUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_MANAGE, InteractiveElementAction.STOP_MANAGE };
    }
    
    @Override
    protected AbstractMRUAction[] getAdditionalMRUActions() {
        return new AbstractMRUAction[] { MRUActions.MANAGE_FLEA_ACTION.getMRUAction(), MRUActions.BROWSE_FLEA_ACTION.getMRUAction() };
    }
    
    @Override
    public String getName() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_displayRefId);
        return (referenceItem != null) ? referenceItem.getName() : "<UNKNOWN>";
    }
    
    public long getMerchantInventoryUid() {
        return this.m_merchantInventoryUid;
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
        if (specificData.getVirtualId() == 1) {
            final RawPersistantDataMerchantDisplay data = (RawPersistantDataMerchantDisplay)specificData;
            this.m_merchantInventoryUid = data.content.uid;
        }
    }
    
    public boolean isMarketregistered() {
        return this.m_marketregistered;
    }
    
    public int getGfxId() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_displayRefId);
        return (referenceItem != null) ? referenceItem.getGfxId() : -1;
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.MERCHANT_DISPLAY;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MerchantDisplay.class);
    }
    
    public static class MerchantDisplayFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            MerchantDisplay element;
            try {
                element = (MerchantDisplay)MerchantDisplayFactory.m_pool.borrowObject();
                element.setPool(MerchantDisplayFactory.m_pool);
            }
            catch (Exception e) {
                MerchantDisplay.m_logger.error((Object)("Erreur lors de l'extraction d'un " + MerchantDisplay.class.getName() + " du pool"), (Throwable)e);
                element = new MerchantDisplay();
            }
            return element;
        }
        
        static {
            MerchantDisplayFactory.m_pool = new MonitoredPool(new ObjectFactory<MerchantDisplay>() {
                @Override
                public MerchantDisplay makeObject() {
                    return new MerchantDisplay();
                }
            });
        }
    }
}
