package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.market.constant.*;

public abstract class AbstractMerchantInventoryItem implements InventoryContent, Releasable, RawConvertible<RawMerchantItem>, LoggableEntity
{
    protected static final Logger m_logger;
    protected Item m_item;
    protected int m_price;
    protected PackType m_packType;
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public int getPrice() {
        return this.m_price;
    }
    
    public void setPrice(final int price) {
        this.m_price = price;
    }
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    public void setPackType(final PackType packType) {
        if (packType == null) {
            AbstractMerchantInventoryItem.m_logger.error((Object)("set de packtype invalide sur merchantitem=" + this), (Throwable)new Exception());
            this.m_packType = PackType.ONE;
            return;
        }
        this.m_packType = packType;
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public long getUniqueId() {
        if (this.m_item == null) {
            return -1L;
        }
        return this.m_item.getUniqueId();
    }
    
    @Override
    public int getReferenceId() {
        return this.m_item.getReferenceId();
    }
    
    @Override
    public short getQuantity() {
        return this.m_item.getQuantity();
    }
    
    @Override
    public void setQuantity(final short quantity) {
        this.m_item.setQuantity(quantity);
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
        this.m_item.updateQuantity(quantityUpdate);
    }
    
    @Override
    public boolean canStackWith(final InventoryContent inv) {
        return this.m_item.canStackWith(inv);
    }
    
    @Override
    public short getStackMaximumHeight() {
        return this.m_item.getStackMaximumHeight();
    }
    
    @Override
    public InventoryContent getCopy(final boolean pooled) {
        return null;
    }
    
    @Override
    public InventoryContent getClone() {
        return null;
    }
    
    @Override
    public void onCheckOut() {
        this.m_packType = PackType.ONE;
    }
    
    @Override
    public void onCheckIn() {
        if (this.m_item != null) {
            this.m_item.release();
            this.m_item = null;
        }
        this.m_price = 0;
        this.m_packType = null;
    }
    
    @Override
    public String getLogRepresentation() {
        return this.m_item.getLogRepresentation() + "p" + this.m_packType.qty + "k" + this.m_price;
    }
    
    public String getHumanReadableRepresentation() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AbstractMerchantInventoryItem");
        sb.append("{m_item=").append((this.m_item == null || this.m_item.getReferenceItem() == null) ? "null" : this.m_item.getReferenceItem().getName());
        sb.append(", m_price=").append(this.m_price);
        sb.append(", m_packType=").append(this.m_packType);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getLogRepresentation();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMerchantInventoryItem.class);
    }
}
