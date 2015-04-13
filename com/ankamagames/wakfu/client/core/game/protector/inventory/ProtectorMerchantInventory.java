package com.ankamagames.wakfu.client.core.game.protector.inventory;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ProtectorMerchantInventory extends AbstractMerchantInventory
{
    private static final Logger m_logger;
    private boolean m_canOnlyBuyOneAtOnce;
    private final ProtectorWalletContext m_walletContext;
    
    public ProtectorMerchantInventory(final long uid, final InventoryContentProvider<AbstractMerchantInventoryItem, RawMerchantItem> contentProvider, final MerchantItemType requiredItemType, final short maximumSize, final byte maximumPack, final boolean canOnlyBuyOneAtOnce, final ProtectorWalletContext walletContext) {
        super(uid, contentProvider, requiredItemType, maximumSize, maximumPack);
        this.m_canOnlyBuyOneAtOnce = canOnlyBuyOneAtOnce;
        this.m_walletContext = walletContext;
    }
    
    @Override
    public boolean canBuyItem(final AbstractMerchantInventoryItem item) {
        if (item != null) {
            final ProtectorMerchantInventoryItem pItem = (ProtectorMerchantInventoryItem)item;
            if (pItem.isActivated()) {
                return false;
            }
        }
        return super.canBuyItem(item);
    }
    
    @Override
    public void onItemSold(final MerchantClient client, final AbstractMerchantInventoryItem merchantItem) {
        if (this.m_canOnlyBuyOneAtOnce) {
            final Iterator<AbstractMerchantInventoryItem> it = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).iterator();
            while (it.hasNext()) {
                it.next().setStartDate(0L);
            }
        }
        final ProtectorMerchantInventoryItem item = (ProtectorMerchantInventoryItem)merchantItem;
        item.setStartDate(WakfuGameCalendar.getInstance().getInternalTimeInMs());
        super.onItemSold(client, merchantItem);
    }
    
    public ProtectorWalletContext getWalletContext() {
        return this.m_walletContext;
    }
    
    @Override
    public boolean toRaw(final RawMerchantItemInventory raw) {
        throw new UnsupportedOperationException("Le client ne doit pas s\u00e9rialiser des ProtectorMerchantInventory");
    }
    
    public boolean fromRaw(final RawProtectorMerchantInventory rawInventory) {
        if (this.m_uid != rawInventory.uid) {
            ProtectorMerchantInventory.m_logger.warn((Object)("Mauvais uid \u00e0 la d\u00e9s\u00e9rialisation: attendu=" + this.m_uid + ", trouv\u00e9=" + rawInventory.uid));
        }
        this.destroyAll();
        boolean ok = true;
        for (final RawProtectorMerchantInventory.Content rawContent : rawInventory.contents) {
            final ProtectorMerchantInventoryItem item = (ProtectorMerchantInventoryItem)this.m_contentProvider.unSerializeContent((R)rawContent.merchantItem);
            if (item != null) {
                try {
                    if (((ArrayInventory<ProtectorMerchantInventoryItem, R>)this).addAt(item, rawContent.position)) {
                        item.setType(ProtectorMerchantItemType.getById(rawContent.type));
                        item.setFeatureReferenceId(rawContent.featureReferenceId);
                        item.setDuration(rawContent.duration);
                        item.setStartDate(rawContent.startDate);
                    }
                    else {
                        ok = false;
                    }
                }
                catch (InventoryCapacityReachedException e) {
                    ok = false;
                    ProtectorMerchantInventory.m_logger.error((Object)e);
                }
                catch (ContentAlreadyPresentException e2) {
                    ok = false;
                    ProtectorMerchantInventory.m_logger.error((Object)e2);
                }
                catch (PositionAlreadyUsedException e3) {
                    ok = false;
                    ProtectorMerchantInventory.m_logger.error((Object)e3);
                }
            }
            else {
                ProtectorMerchantInventory.m_logger.error((Object)("D\u00e9s\u00e9rialisation d'un ProtectorMerchantItem null \u00e0 la position " + rawContent.position));
                ok = false;
            }
        }
        return ok;
    }
    
    @Nullable
    public ProtectorMerchantInventoryItem getFromFeatureId(final int id) {
        for (final ProtectorMerchantInventoryItem item : this) {
            if (item.getFeatureReferenceId() == id) {
                return item;
            }
        }
        return null;
    }
    
    @Override
    public AbstractMerchantInventoryItem makeMerchantInventoryItem() {
        return new ProtectorMerchantInventoryItem();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorMerchantInventory.class);
    }
}
