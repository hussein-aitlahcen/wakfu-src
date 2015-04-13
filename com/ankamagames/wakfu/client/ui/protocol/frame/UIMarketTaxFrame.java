package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;

public abstract class UIMarketTaxFrame implements MessageFrame
{
    protected Item m_item;
    protected MerchantInventoryItem m_merchantItem;
    protected int m_taxValue;
    protected short m_merchantItemFirstQuantity;
    protected TaxContext m_taxContext;
    
    protected boolean updateTax() {
        if (this.m_taxContext == null) {
            return false;
        }
        this.m_taxValue = this.getMerchantAddItemTax(this.m_merchantItem.getQuantity(), this.m_merchantItem.getPackType(), this.m_merchantItem.getAuctionDuration().taxFactor, this.m_merchantItem.getPrice());
        if (this.m_taxValue == -1) {
            return false;
        }
        PropertiesProvider.getInstance().setPropertyValue("stackTax", this.m_taxValue);
        return true;
    }
    
    private int getMerchantAddItemTax(final short quantity, final PackType packType, final double taxFactor, final int price) {
        final int packNumber = quantity / packType.qty;
        final long totalPrice = Math.round(price * packNumber * taxFactor);
        if (totalPrice < 0L || totalPrice > 2147483647L) {
            return -1;
        }
        return TaxManager.INSTANCE.getTaxesAmount(WakfuGameEntity.getInstance().getLocalPlayer(), this.m_taxContext, (int)totalPrice);
    }
    
    protected boolean localPlayerHasEnoughMoney() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int count = localPlayer.getKamasCount();
        return this.m_taxValue != -1 && count >= this.m_taxValue;
    }
    
    protected void addMerchantItem(final Item copy, final short quantity) {
        this.m_merchantItem.setItem(copy);
        this.m_merchantItemFirstQuantity = quantity;
        this.m_merchantItem.setQuantity(this.m_merchantItemFirstQuantity);
        this.m_merchantItem.setPackType(PackType.ONE);
        this.m_merchantItem.fillPackTypes();
        this.m_merchantItem.setPrice(0);
        this.m_merchantItem.fillDurations();
        this.m_merchantItem.setDuration(AuctionDuration.TWENTY_FOUR_HOURS);
        this.updateTax();
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.m_merchantItem = MerchantInventoryItem.checkout();
        PropertiesProvider.getInstance().setPropertyValue("merchantItem", this.m_merchantItem);
        Xulor.getInstance().putActionClass("wakfu.merchant.tax", MerchantTaxDialogActions.class);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_taxContext = null;
        this.m_merchantItem.setItem(null);
        this.m_merchantItem.release();
        this.m_merchantItem = null;
        PropertiesProvider.getInstance().setPropertyValue("merchantItem", null);
        Xulor.getInstance().removeActionClass("wakfu.merchant.tax");
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17300: {
                if (this.m_merchantItem.getItem() == null) {
                    return false;
                }
                final UIMessage msg = (UIMessage)message;
                int intValue = msg.getIntValue();
                if (this.m_merchantItem.getQuantity() * intValue < 0) {
                    intValue = 0;
                }
                final int price = this.m_merchantItem.getPrice();
                this.m_merchantItem.setPrice(intValue);
                if (!this.updateTax()) {
                    this.m_merchantItem.setPrice(price);
                }
                return false;
            }
            case 17311: {
                if (this.m_merchantItem.getItem() == null) {
                    return false;
                }
                final UIMerchantChangeDurationMessage uiMessage = (UIMerchantChangeDurationMessage)message;
                final AuctionDuration auctionDuration = this.m_merchantItem.getAuctionDuration();
                this.m_merchantItem.setDuration(uiMessage.getDuration());
                if (!this.updateTax()) {
                    this.m_merchantItem.setDuration(auctionDuration);
                }
                return false;
            }
            case 17302: {
                if (this.m_merchantItem.getItem() == null) {
                    return false;
                }
                final UIMerchantChangePackTypeMessage uiMessage2 = (UIMerchantChangePackTypeMessage)message;
                final PackType packType = uiMessage2.getPackType();
                final PackType type = this.m_merchantItem.getPackType();
                this.m_merchantItem.setPackType(packType);
                final short qty = (short)(this.m_merchantItemFirstQuantity / packType.qty * packType.qty);
                final short quantity = this.m_merchantItem.getQuantity();
                this.m_merchantItem.setQuantity(qty);
                if (!this.updateTax()) {
                    this.m_merchantItem.setPackType(type);
                    this.m_merchantItem.setQuantity(quantity);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
}
