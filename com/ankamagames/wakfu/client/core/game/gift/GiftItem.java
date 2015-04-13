package com.ankamagames.wakfu.client.core.game.gift;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;

public class GiftItem extends ImmutableFieldProvider implements Gift
{
    private String m_guid;
    private int m_referenceId;
    private short m_quantity;
    private ReferenceItem m_referenceItem;
    
    @Override
    public String[] getFields() {
        return GiftItem.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getItemBigIconUrl(this.m_referenceItem.getGfxId());
        }
        if (fieldName.equals("name")) {
            return this.m_referenceItem.getName() + " x" + this.m_quantity;
        }
        if (fieldName.equals("quantity")) {
            return this.m_quantity;
        }
        if (fieldName.equals("allowControls")) {
            return true;
        }
        return this.m_referenceItem.getFieldValue(fieldName);
    }
    
    @Override
    public boolean consume(final boolean all) {
        if (all) {
            this.m_quantity = 0;
        }
        else {
            --this.m_quantity;
        }
        if (this.m_quantity == 0) {
            return true;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "quantity");
        return false;
    }
    
    public ReferenceItem getReferenceItem() {
        return this.m_referenceItem;
    }
    
    public String getGuid() {
        return this.m_guid;
    }
    
    public static GiftItem fromRaw(final RawGiftItem rawGift) throws GiftException {
        try {
            final GiftItem giftItem = new GiftItem();
            giftItem.m_guid = rawGift.guid;
            giftItem.m_referenceId = rawGift.referenceId;
            giftItem.m_quantity = rawGift.quantity;
            giftItem.m_referenceItem = ReferenceItemManager.getInstance().getReferenceItem(giftItem.m_referenceId);
            if (giftItem.m_referenceItem == null) {
                throw new IllegalArgumentException("L'item de refId=" + rawGift.referenceId + " n'existe pas");
            }
            return giftItem;
        }
        catch (Exception e) {
            throw new GiftException(e);
        }
    }
}
