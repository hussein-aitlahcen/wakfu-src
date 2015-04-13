package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.market.constant.*;

public class ProtectorMerchantInventoryItem extends AbstractMerchantInventoryTimedItem
{
    private ProtectorMerchantItemType m_type;
    private int m_featureReferenceId;
    
    @Override
    public boolean shouldBeSerialized() {
        return false;
    }
    
    @Override
    public boolean toRaw(final RawMerchantItem raw) {
        throw new UnsupportedOperationException("Un item d'inventaire de protecteur ne doit pas \u00eatre s\u00e9rialis\u00e9 par le client");
    }
    
    @Override
    public boolean fromRaw(final RawMerchantItem rawItem) {
        this.m_item = new Item();
        if (!this.m_item.fromRaw(rawItem.item)) {
            return false;
        }
        this.setPrice(rawItem.price);
        final PackType packType = PackType.fromQuantity(rawItem.packSize);
        if (packType != null) {
            this.setPackType(packType);
            return true;
        }
        ProtectorMerchantInventoryItem.m_logger.error((Object)("Impossible de trouver le pack \u00e0 " + rawItem.packSize + " items"));
        return false;
    }
    
    public ProtectorMerchantItemType getType() {
        return this.m_type;
    }
    
    public void setType(final ProtectorMerchantItemType type) {
        this.m_type = type;
    }
    
    public int getFeatureReferenceId() {
        return this.m_featureReferenceId;
    }
    
    public void setFeatureReferenceId(final int featureReferenceId) {
        this.m_featureReferenceId = featureReferenceId;
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
    }
    
    public ProtectorMerchantItemView getView() {
        switch (this.m_type) {
            case CHALLENGE: {
                return new ProtectorChallengeItemView(this, this.m_featureReferenceId);
            }
            case BUFF: {
                return new ProtectorBuffItemView(this, this.m_featureReferenceId);
            }
            case CLIMATE_BUFF: {
                return new ProtectorClimateItemView(this, this.m_featureReferenceId);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_type = null;
        this.m_featureReferenceId = 0;
    }
}
