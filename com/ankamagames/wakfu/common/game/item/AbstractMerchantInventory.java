package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.moderation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public abstract class AbstractMerchantInventory extends ContiguousArrayInventory<AbstractMerchantInventoryItem, RawMerchantItem> implements RawConvertible<RawMerchantItemInventory>, LoggableEntity
{
    private static final Logger m_logger;
    protected final long m_uid;
    protected final MerchantItemType m_requiredItemType;
    protected final byte m_maximumPack;
    protected String m_shortAd;
    private final ArrayList<MerchantInventoryEventListener> m_merchantEventListeners;
    
    public AbstractMerchantInventory(final long uid, final InventoryContentProvider<AbstractMerchantInventoryItem, RawMerchantItem> contentProvider, final MerchantItemType requiredItemType, final short maximumSize, final byte maximumPack) {
        super(contentProvider, MerchantInventoryChecker.getInstance(), maximumSize, true);
        this.m_uid = uid;
        this.m_requiredItemType = requiredItemType;
        this.m_maximumPack = maximumPack;
        this.m_merchantEventListeners = new ArrayList<MerchantInventoryEventListener>();
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public final byte getMaximumPack() {
        return this.m_maximumPack;
    }
    
    public final MerchantItemType getRequiredItemType() {
        return this.m_requiredItemType;
    }
    
    @Nullable
    public final AbstractMerchantInventoryItem add(final Item item) {
        final AbstractMerchantInventoryItem merchantItem = this.makeMerchantInventoryItem();
        merchantItem.setItem(item);
        boolean bOk = false;
        try {
            bOk = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).add(merchantItem);
        }
        catch (InventoryException e) {
            AbstractMerchantInventory.m_logger.error((Object)"Item can not be added to the merchant bag", (Throwable)e);
        }
        catch (Exception e2) {
            AbstractMerchantInventory.m_logger.error((Object)"Item can not be added to the merchant bag", (Throwable)e2);
        }
        if (bOk) {
            return merchantItem;
        }
        merchantItem.setItem(null);
        merchantItem.release();
        return null;
    }
    
    @Nullable
    public final AbstractMerchantInventoryItem insertAt(final Item item, final short pos, final short packSize, final int price) {
        final AbstractMerchantInventoryItem merchantItem = this.makeMerchantInventoryItem();
        merchantItem.setItem(item);
        merchantItem.setPackType(PackType.fromQuantity(packSize));
        merchantItem.setPrice(price);
        boolean bOk = false;
        try {
            bOk = ((ContiguousArrayInventory<AbstractMerchantInventoryItem, R>)this).insertAt(merchantItem, pos);
        }
        catch (InventoryException e) {
            AbstractMerchantInventory.m_logger.error((Object)"Erreur lors de l'ajout de L'item au sac marchant!", (Throwable)e);
        }
        catch (Exception e2) {
            AbstractMerchantInventory.m_logger.error((Object)"Erreur lors de l'ajout de L'item au sac marchant!", (Throwable)e2);
        }
        if (bOk) {
            return merchantItem;
        }
        merchantItem.setItem(null);
        merchantItem.release();
        return null;
    }
    
    public abstract AbstractMerchantInventoryItem makeMerchantInventoryItem();
    
    public boolean canBuyItem(final AbstractMerchantInventoryItem item) {
        return item != null;
    }
    
    public void onItemSold(final MerchantClient client, final AbstractMerchantInventoryItem merchantItem) {
        this.fireMerchantItemSold(client, merchantItem);
    }
    
    public ClientMerchantTransaction<Item> buy(final MerchantClient<Item> buyer, final long itemUid, final short quantity, final Wallet sellerWallet, final long ownerId) {
        final ClientMerchantTransaction<Item> transaction = new ClientMerchantTransaction<Item>();
        transaction.setError((byte)1);
        final AbstractMerchantInventoryItem merchantItem = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).getWithUniqueId(itemUid);
        if (merchantItem == null) {
            AbstractMerchantInventory.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'item " + itemUid));
            return transaction;
        }
        if (this.isLocked()) {
            AbstractMerchantInventory.m_logger.error((Object)"Requ\u00eate d'achat sur un inventaire marchand verouill\u00e9");
            transaction.setError((byte)3);
            return transaction;
        }
        if (!this.canBuyItem(merchantItem) || quantity <= 0) {
            return transaction;
        }
        final Wallet clientWallet = buyer.getWallet();
        final short currentQuantity = merchantItem.getQuantity();
        final long price = merchantItem.getPrice() * quantity;
        if (price > 2147483647L || price < 0L) {
            AbstractMerchantInventory.m_logger.error((Object)("[BROCANTE] Tentative d'achat d'un objet en brocante invalide : prix total incoh\u00e9rent (d\u00e9passement de capacit\u00e9 ou n\u00e9gatif) [price=" + price + ']'));
            return transaction;
        }
        final int playerCash = clientWallet.getAmountOfCash();
        final short packQuantity = merchantItem.getPackType().qty;
        if (currentQuantity < quantity * packQuantity || playerCash < price || packQuantity < 0) {
            AbstractMerchantInventory.m_logger.error((Object)("[BROCANTE] Quantit\u00e9 insuffisante(" + quantity + '/' + currentQuantity + ") " + "ou le joueur n'a pas les moyens(" + playerCash + '/' + price + ") " + "ou les constantes sont invalides packSize=" + packQuantity));
            return transaction;
        }
        final Item obtainedItem = merchantItem.getItem().getCopy(Item.getItemComposer().getUidGenerator().getNextUID(), false);
        obtainedItem.setQuantity((short)(quantity * packQuantity));
        if (!buyer.canStockItem(obtainedItem)) {
            AbstractMerchantInventory.m_logger.warn((Object)("[BROCANTE] Impossible d'acheter : Les inventaires du joueur ne peuvent acceuillir l'objet de type " + obtainedItem.getReferenceId()));
            obtainedItem.release();
            transaction.setError((byte)2);
            return transaction;
        }
        this.onItemSold(buyer, merchantItem);
        if (merchantItem.getQuantity() == quantity * packQuantity) {
            this.removeWithUniqueId(itemUid);
            merchantItem.release();
        }
        else {
            this.updateQuantity(merchantItem.getUniqueId(), (short)(-(quantity * packQuantity)));
        }
        final AbstractBag container = buyer.stockItem(obtainedItem);
        if (buyer instanceof BasicCharacterInfo) {
            final BasicCharacterInfo buyerCharacterInfo = (BasicCharacterInfo)buyer;
            ItemTracker.log(Level.INFO, ownerId, -1L, buyerCharacterInfo.getOwnerId(), buyerCharacterInfo.getId(), "FromMerchantInventoryToInventory", -1L, buyerCharacterInfo.getInstanceId(), obtainedItem.getReferenceId(), obtainedItem.getUniqueId(), obtainedItem.getUniqueId(), obtainedItem.getQuantity());
        }
        clientWallet.setAmountOfCash(playerCash - (int)price);
        if (sellerWallet != null) {
            sellerWallet.addAmount((int)price);
        }
        if (buyer instanceof BasicCharacterInfo) {
            final BasicCharacterInfo buyerCharacterInfo = (BasicCharacterInfo)buyer;
            ItemTracker.log(Level.INFO, buyerCharacterInfo.getOwnerId(), buyerCharacterInfo.getId(), ownerId, -1L, "BuyInMerchantInventory", this.getUid(), buyerCharacterInfo.getInstanceId(), -1, -1L, -1L, -price);
        }
        transaction.setError((byte)0);
        transaction.setTargetContainer(container);
        transaction.setObtainedItem(obtainedItem);
        transaction.setPrice((int)price);
        transaction.setQuantity(quantity * packQuantity);
        return transaction;
    }
    
    public String getShortAd() {
        return this.m_shortAd;
    }
    
    public void setShortAd(final String shortAd) {
        this.m_shortAd = shortAd;
    }
    
    public void setPrice(final AbstractMerchantInventoryItem item, final int price) {
        final AbstractMerchantInventoryItem myItem = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).getWithUniqueId(item.getUniqueId());
        if (myItem == item) {
            myItem.setPrice(price);
            this.notifyObservers(InventoryItemModifiedEvent.checkOutPriceEvent(this, item, this.getPosition(item.getUniqueId())));
        }
        else {
            AbstractMerchantInventory.m_logger.error((Object)("Impossible de d\u00e9finir le prix sur un objet qui ne fait pas partie de l'inventaire: " + item.getUniqueId()));
        }
    }
    
    public void setPackType(final AbstractMerchantInventoryItem item, final PackType packType) {
        final AbstractMerchantInventoryItem myItem = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).getWithUniqueId(item.getUniqueId());
        if (myItem == item) {
            myItem.setPackType(packType);
            this.notifyObservers(InventoryItemModifiedEvent.checkOutPackSizeEvent(this, item, this.getPosition(item.getUniqueId())));
        }
        else {
            AbstractMerchantInventory.m_logger.error((Object)("Impossible de d\u00e9finir le prix sur un objet qui ne fait pas partie de l'inventaire: " + item.getUniqueId()));
        }
    }
    
    @Override
    public boolean fromRaw(final RawMerchantItemInventory raw) {
        if (this.m_uid != raw.uid) {
            AbstractMerchantInventory.m_logger.warn((Object)("Mauvais uid \u00e0 la d\u00e9s\u00e9rialisation: attendu=" + this.m_uid + ", trouv\u00e9=" + raw.uid));
        }
        if (this.m_requiredItemType.ordinal() != (raw.requiredItemType & 0xFF)) {
            AbstractMerchantInventory.m_logger.warn((Object)("Mauvais type d'item requis \u00e0 la d\u00e9s\u00e9rialisation: attendu=" + this.m_requiredItemType + ", trouv\u00e9=" + raw.requiredItemType));
        }
        if (this.getMaximumSize() != raw.nSlots) {
            AbstractMerchantInventory.m_logger.warn((Object)("Mauvais nombre de slots la d\u00e9s\u00e9rialisation: attendu=" + this.getMaximumSize() + ", trouv\u00e9=" + raw.nSlots));
        }
        if (this.m_maximumPack != raw.maxPackSize) {
            AbstractMerchantInventory.m_logger.warn((Object)("Mauvaise packMax \u00e0 la d\u00e9s\u00e9rialisation: attendu=" + this.m_maximumPack + ", trouv\u00e9=" + raw.maxPackSize));
        }
        this.m_shortAd = raw.shortAd;
        this.setLocked(raw.locked);
        this.destroyAll();
        boolean ok = true;
        for (final RawMerchantItemInventory.Content rawContent : raw.contents) {
            final AbstractMerchantInventoryItem item = (AbstractMerchantInventoryItem)this.m_contentProvider.unSerializeContent((R)rawContent.merchantItem);
            if (item != null) {
                try {
                    if (!((ArrayInventory<AbstractMerchantInventoryItem, R>)this).addAt(item, rawContent.position)) {
                        ok = false;
                        AbstractMerchantInventory.m_logger.error((Object)("L'item (" + item + ")ne peut \u00eatre ajout\u00e9 \u00e0 l'inventaire, slot : " + rawContent.position));
                    }
                }
                catch (InventoryCapacityReachedException e) {
                    ok = false;
                    AbstractMerchantInventory.m_logger.error((Object)e);
                }
                catch (ContentAlreadyPresentException e2) {
                    ok = false;
                    AbstractMerchantInventory.m_logger.error((Object)e2);
                }
                catch (PositionAlreadyUsedException e3) {
                    ok = false;
                    AbstractMerchantInventory.m_logger.error((Object)e3);
                }
                if (ok) {
                    continue;
                }
                item.setItem(null);
                item.release();
            }
            else {
                AbstractMerchantInventory.m_logger.error((Object)("D\u00e9s\u00e9rialisation d'un MerchantItem null \u00e0 la position " + rawContent.position));
                ok = false;
            }
        }
        return ok;
    }
    
    @Override
    public boolean toRaw(final RawMerchantItemInventory raw) {
        this.toRawInventoryOnly(raw);
        final TLongShortIterator it = this.m_idxByUniqueId.iterator();
        while (it.hasNext()) {
            it.advance();
            final short pos = it.value();
            final AbstractMerchantInventoryItem item = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).getFromPosition(pos);
            if (item == null) {
                AbstractMerchantInventory.m_logger.error((Object)("Incoh\u00e9rence d'Inventory, l'item $" + it.key() + " est r\u00e9f\u00e9renc\u00e9 mais n'est pas pr\u00e9sent dans le tableau"), (Throwable)new Exception());
            }
            else {
                if (!item.shouldBeSerialized()) {
                    continue;
                }
                final RawMerchantItemInventory.Content content = new RawMerchantItemInventory.Content();
                content.position = pos;
                final boolean ok = item.toRaw(content.merchantItem);
                if (!ok) {
                    AbstractMerchantInventory.m_logger.error((Object)("Impossible de convertir l'item \u00e0 la position " + pos + " sous forme d\u00e9s\u00e9rialis\u00e9e brute"));
                    return false;
                }
                raw.contents.add(content);
            }
        }
        return true;
    }
    
    public void toRawInventoryOnly(final RawMerchantItemInventory rawInventory) {
        rawInventory.clear();
        rawInventory.uid = this.m_uid;
        rawInventory.requiredItemType = (byte)this.m_requiredItemType.ordinal();
        rawInventory.nSlots = this.getMaximumSize();
        rawInventory.maxPackSize = this.m_maximumPack;
        rawInventory.shortAd = this.m_shortAd;
        rawInventory.locked = this.isLocked();
    }
    
    public void addMerchantEventListener(final MerchantInventoryEventListener<? extends MerchantClient> listener) {
        if (this.m_merchantEventListeners.contains(listener)) {
            AbstractMerchantInventory.m_logger.error((Object)"Tentative d'ajout multiple du m\u00eame listener d'evenement sur un MerchantInventory");
            return;
        }
        this.m_merchantEventListeners.add(listener);
    }
    
    public void removeMerchantEventListener(final MerchantInventoryEventListener listener) {
        this.m_merchantEventListeners.remove(listener);
    }
    
    protected final void fireMerchantItemSold(final MerchantClient client, final AbstractMerchantInventoryItem merchantItem) {
        for (int i = 0, size = this.m_merchantEventListeners.size(); i < size; ++i) {
            this.m_merchantEventListeners.get(i).onMerchantItemSold(client, merchantItem);
        }
    }
    
    @Override
    public String getLogRepresentation() {
        return "merchant";
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMerchantInventory.class);
    }
}
