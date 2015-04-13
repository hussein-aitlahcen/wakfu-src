package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class EquipableDummy extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    public static final int NO_ITEM = -1;
    private IEEquipableDummyParameter m_params;
    private final BinarSerialPart SYNCHRONIZATION_PART;
    private Bag m_bag;
    private Item m_item;
    private int m_itemAttachedRefId;
    private StatueView m_view;
    
    public EquipableDummy() {
        super();
        this.SYNCHRONIZATION_PART = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final RawEquipableDummy data = new RawEquipableDummy();
                data.unserialize(buffer);
                final int previousPackRefId = EquipableDummy.this.m_itemAttachedRefId;
                EquipableDummy.this.m_itemAttachedRefId = data.setPackId;
                EquipableDummy.this.createItem(previousPackRefId, EquipableDummy.this.m_itemAttachedRefId, data.item);
            }
        };
        this.m_itemAttachedRefId = -1;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return new AbstractMRUAction[] { MRUActions.MANAGE_EQUIPABLE_DUMMY.getMRUAction() };
    }
    
    @Override
    protected InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_item = null;
        this.m_view = null;
        WakfuGameEntity.getInstance().removeFrame(UIEquipableDummyFrame.getInstance());
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingMovements(true);
        this.m_itemAttachedRefId = -1;
        this.m_bag = new Bag(0L, 0, NullContentChecker.INSTANCE, (short)1, null);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("equipable.dummy");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SYNCHRONIZATION_PART;
    }
    
    @Override
    public void initializeWithParameter() throws IllegalArgumentException {
        if (!StringUtils.isEmptyOrNull(this.m_parameter)) {
            this.m_params = (IEEquipableDummyParameter)IEParametersManager.INSTANCE.getParam(IETypes.EQUIPABLE_DUMMY, Integer.parseInt(this.m_parameter));
        }
        if (this.m_params == null) {
            throw new IllegalArgumentException("Impossible de trouver le param\u00e8tre " + this.m_parameter + " pour l'\u00e9l\u00e9ment interactif d'id=" + this.m_id);
        }
    }
    
    public void addInventoryObserver(final InventoryObserver obs) {
        this.m_bag.addObserver(obs);
    }
    
    public void createItem(final int previousPackId, final int newPackId, final RawInventoryItem rawItem) {
        if (previousPackId == newPackId) {
            return;
        }
        if (previousPackId != -1 && this.m_item != null) {
            final Item previousItem = this.m_item;
            this.m_item = null;
            this.m_bag.remove(previousItem);
        }
        if (newPackId != -1) {
            if (rawItem.refId > 0) {
                (this.m_item = new Item()).fromRaw(rawItem);
            }
            else {
                (this.m_item = new Item(GUIDGenerator.getGUID())).initializeWithReferenceItem(ReferenceItemManager.getInstance().getReferenceItem(this.m_itemAttachedRefId));
                this.m_item.setQuantity((short)1);
            }
            try {
                this.m_bag.add(this.m_item);
            }
            catch (InventoryCapacityReachedException e) {
                EquipableDummy.m_logger.warn((Object)"Ne devrait pas arriver, on a nettoy\u00e9 l'inventaire avant");
            }
            catch (ContentAlreadyPresentException e2) {
                EquipableDummy.m_logger.warn((Object)"Ne devrait pas arriver, on a nettoy\u00e9 l'inventaire avant");
            }
        }
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public String getAnimName() {
        return this.m_params.getAnimName();
    }
    
    public byte getSex() {
        return this.m_params.getSex();
    }
    
    public int getItemAttachedRefId() {
        return this.m_itemAttachedRefId;
    }
    
    public void setItemAttachedRefId(final int itemAttachedRefId) {
        this.m_itemAttachedRefId = itemAttachedRefId;
    }
    
    public void setView(final StatueView view) {
        this.m_view = view;
    }
    
    public StatueView getView() {
        return this.m_view;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.REPACK) {
            WakfuGameEntity.getInstance().removeFrame(UIEquipableDummyFrame.getInstance());
        }
        this.runScript(action);
        return true;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new EquipableDummyItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EquipableDummy.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            EquipableDummy element;
            try {
                element = (EquipableDummy)Factory.m_pool.borrowObject();
                element.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                EquipableDummy.m_logger.error((Object)("Erreur lors de l'extraction d'un " + EquipableDummy.class.getName() + " du pool"), (Throwable)e);
                element = new EquipableDummy();
            }
            return element;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<EquipableDummy>() {
                @Override
                public EquipableDummy makeObject() {
                    return new EquipableDummy();
                }
            });
        }
    }
}
