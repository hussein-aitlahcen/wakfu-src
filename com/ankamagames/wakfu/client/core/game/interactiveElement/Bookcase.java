package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class Bookcase extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEBookcaseParameter m_params;
    private int[] m_booksRefIds;
    private Bag m_bag;
    private final BinarSerialPart SYNCHRONIZATION_PART;
    
    public Bookcase() {
        super();
        this.SYNCHRONIZATION_PART = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final RawBookcase data = new RawBookcase();
                data.unserialize(buffer);
                final ArrayList<RawBookcase.Content> bookRefIds = data.bookRefIds;
                Bookcase.this.m_booksRefIds = new int[bookRefIds.size()];
                for (int i = 0, n = bookRefIds.size(); i < n; ++i) {
                    Bookcase.this.m_booksRefIds[i] = bookRefIds.get(i).bookRefId;
                }
                Bookcase.this.updateBag();
            }
        };
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.REPACK) {
            WakfuGameEntity.getInstance().removeFrame(UIBookcaseFrame.getInstance());
        }
        return false;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("bookcase");
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_MANAGE, InteractiveElementAction.STOP_MANAGE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.getItemizableInfo().getOwnerId() == WakfuGameEntity.getInstance().getLocalPlayer().getId() || MRUActionUtils.canManageInHavenWorld(this.getItemizableInfo().getOwnerId())) {
            return new AbstractMRUAction[] { MRUActions.MANAGE_BOOKCASE.getMRUAction() };
        }
        return new AbstractMRUAction[] { MRUActions.BROWSE_BOOKCASE.getMRUAction() };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingMovements(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_bag = null;
        WakfuGameEntity.getInstance().removeFrame(UIBookcaseFrame.getInstance());
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SYNCHRONIZATION_PART;
    }
    
    @Override
    public void initializeWithParameter() throws IllegalArgumentException {
        if (!StringUtils.isEmptyOrNull(this.m_parameter)) {
            this.m_params = (IEBookcaseParameter)IEParametersManager.INSTANCE.getParam(IETypes.BOOKCASE, Integer.parseInt(this.m_parameter));
        }
        if (this.m_params == null) {
            throw new IllegalArgumentException("Impossible de trouver le param\u00e8tre " + this.m_parameter + " pour l'\u00e9l\u00e9ment interactif d'id=" + this.m_id);
        }
        this.m_bag = new Bag(0L, 0, BagInventoryContentChecker.INSTANCE, this.m_params.getSize(), null);
    }
    
    public void updateBag() {
        if (this.m_booksRefIds.length != this.m_bag.getMaximumSize()) {
            throw new IllegalArgumentException("La taille des donn\u00e9es dynamiques ne correspond pas \u00e0 la taille des donn\u00e9es statiques. Bookcase id=" + this.m_id);
        }
        for (short index = 0, size = this.m_bag.getMaximumSize(); index < size; ++index) {
            final Item book = this.m_bag.getFromPosition(index);
            final int previousRefId = (book == null) ? 0 : book.getReferenceId();
            final int newRefId = this.m_booksRefIds[index];
            if (previousRefId != newRefId) {
                if (book != null) {
                    this.m_bag.removeAt(index);
                }
                if (newRefId != 0) {
                    final Item newItem = new Item(GUIDGenerator.getGUID());
                    newItem.initializeWithReferenceItem(ReferenceItemManager.getInstance().getReferenceItem(newRefId));
                    newItem.setQuantity((short)1);
                    try {
                        this.m_bag.addAt(newItem, index);
                    }
                    catch (InventoryCapacityReachedException e) {
                        Bookcase.m_logger.warn((Object)"Ne devrait pas arriver, on a nettoy\u00e9 l'inventaire avant");
                    }
                    catch (ContentAlreadyPresentException e2) {
                        Bookcase.m_logger.warn((Object)"Ne devrait pas arriver, on a nettoy\u00e9 l'inventaire avant");
                    }
                    catch (PositionAlreadyUsedException e3) {
                        Bookcase.m_logger.warn((Object)"Ne devrait pas arriver, on a nettoy\u00e9 l'inventaire avant");
                    }
                }
            }
        }
    }
    
    public void addInventoryObserver(final InventoryObserver obs) {
        this.m_bag.addObserver(obs);
    }
    
    public Bag getBag() {
        return this.m_bag;
    }
    
    public int[] getBooksRefIds() {
        return this.m_booksRefIds;
    }
    
    public void setBooksRefIds(final int[] booksRefIds) {
        this.m_booksRefIds = booksRefIds;
    }
    
    public int fillSize() {
        if (this.m_booksRefIds == null) {
            return 0;
        }
        int res = 0;
        for (int i = 0; i < this.m_booksRefIds.length; ++i) {
            final int book = this.m_booksRefIds[i];
            if (book > 0) {
                ++res;
            }
        }
        return res;
    }
    
    @Override
    protected InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new BookcaseItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Bookcase.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Bookcase element;
            try {
                element = (Bookcase)Factory.m_pool.borrowObject();
                element.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                Bookcase.m_logger.error((Object)("Erreur lors de l'extraction d'un " + Bookcase.class.getName() + " du pool"), (Throwable)e);
                element = new Bookcase();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<Bookcase>() {
                @Override
                public Bookcase makeObject() {
                    return new Bookcase();
                }
            });
        }
    }
}
