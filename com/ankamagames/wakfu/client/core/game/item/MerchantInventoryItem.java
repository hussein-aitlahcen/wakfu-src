package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import java.text.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;

public class MerchantInventoryItem extends AbstractMerchantInventoryItem implements FieldProvider
{
    public static final String PACK_FIELD = "packType";
    public static final String PRICE_FIELD = "price";
    public static final String FORMATED_PRICE_FIELD = "formatedPrice";
    public static final String ITEM_FIELD = "item";
    public static final String PACK_LIST_FIELD = "packList";
    public static final String DURATION_LIST_FIELD = "durationList";
    public static final String DURATION_FIELD = "duration";
    public static final String[] FIELDS;
    private ArrayList<PackTypeFieldProvider> m_packTypes;
    private ArrayList<DurationFieldProvider> m_durations;
    protected AuctionDuration m_auctionDuration;
    
    public static MerchantInventoryItem checkout() {
        final MerchantInventoryItem merchantInventoryItem = new MerchantInventoryItem();
        merchantInventoryItem.onCheckOut();
        return merchantInventoryItem;
    }
    
    private MerchantInventoryItem() {
        super();
        this.m_packTypes = new ArrayList<PackTypeFieldProvider>();
        this.m_durations = new ArrayList<DurationFieldProvider>();
    }
    
    @Override
    public boolean shouldBeSerialized() {
        return true;
    }
    
    @Override
    public String[] getFields() {
        return MerchantInventoryItem.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("packType")) {
            if (this.m_packTypes.isEmpty()) {
                this.fillPackTypes();
            }
            for (int i = 0, size = this.m_packTypes.size(); i < size; ++i) {
                final PackTypeFieldProvider fieldProvider = this.m_packTypes.get(i);
                if (fieldProvider.getPackType() == this.getPackType()) {
                    return fieldProvider;
                }
            }
            return null;
        }
        if (fieldName.equals("duration")) {
            if (this.m_durations.isEmpty()) {
                this.fillDurations();
            }
            for (int i = 0, size = this.m_durations.size(); i < size; ++i) {
                final DurationFieldProvider fieldProvider2 = this.m_durations.get(i);
                if (fieldProvider2.getDuration() == this.getAuctionDuration()) {
                    return fieldProvider2;
                }
            }
            return null;
        }
        if (fieldName.equals("price")) {
            return this.getPrice();
        }
        if (fieldName.equals("formatedPrice")) {
            return NumberFormat.getIntegerInstance().format(this.getPrice());
        }
        if (fieldName.equals("item")) {
            return this.getItem();
        }
        if (fieldName.equals("packList")) {
            if (this.m_packTypes.isEmpty()) {
                this.fillPackTypes();
            }
            return this.m_packTypes;
        }
        if (fieldName.equals("durationList")) {
            if (this.m_durations.isEmpty()) {
                this.fillDurations();
            }
            return this.m_durations;
        }
        return (this.getItem() == null) ? null : this.getItem().getFieldValue(fieldName);
    }
    
    @Override
    public short getQuantity() {
        if (this.getItem() == null) {
            return -1;
        }
        return super.getQuantity();
    }
    
    public AuctionDuration getAuctionDuration() {
        return this.m_auctionDuration;
    }
    
    public void fillPackTypes() {
        this.m_packTypes.clear();
        for (final PackType packType : PackType.values()) {
            if (packType.qty <= this.getQuantity()) {
                this.m_packTypes.add(new PackTypeFieldProvider(packType, true, packType == this.m_packType));
            }
        }
    }
    
    public void fillDurations() {
        this.m_durations.clear();
        for (final AuctionDuration auctionDuration : AuctionDuration.values()) {
            this.m_durations.add(new DurationFieldProvider(auctionDuration, auctionDuration == this.m_auctionDuration));
        }
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equalsIgnoreCase("price")) {
            final int number = Actions.parseStringNumber(value.toString());
            if (number != -1) {
                this.setPrice(number);
            }
        }
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equalsIgnoreCase("price");
    }
    
    @Override
    public void setPrice(final int price) {
        super.setPrice(price);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "price", "formatedPrice");
    }
    
    @Override
    public void setPackType(final PackType packType) {
        super.setPackType(packType);
        for (final PackTypeFieldProvider packTypeFieldProvider : this.m_packTypes) {
            packTypeFieldProvider.setSelected(packTypeFieldProvider.getPackType() == packType);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "packType");
    }
    
    public void setDuration(final AuctionDuration auctionDuration) {
        this.m_auctionDuration = auctionDuration;
        for (final DurationFieldProvider durationFieldProvider : this.m_durations) {
            durationFieldProvider.setSelected(durationFieldProvider.getDuration() == auctionDuration);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "duration");
    }
    
    @Override
    public void setQuantity(final short quantity) {
        super.setQuantity(quantity);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "quantity");
    }
    
    @Override
    public InventoryContent getClone() {
        final MerchantInventoryItem merchantInventoryItem = new MerchantInventoryItem();
        merchantInventoryItem.m_item = this.m_item;
        merchantInventoryItem.m_packType = this.m_packType;
        merchantInventoryItem.m_price = this.m_price;
        return merchantInventoryItem;
    }
    
    @Override
    public boolean fromRaw(final RawMerchantItem rawItem) {
        this.m_item = ReferenceItemManager.getInstance().unSerializeContent(rawItem.item);
        if (this.m_item == null) {
            return false;
        }
        this.setPrice(rawItem.price);
        final PackType packType = PackType.fromQuantity(rawItem.packSize);
        if (packType != null) {
            this.setPackType(packType);
            return true;
        }
        MerchantInventoryItem.m_logger.error((Object)("Impossible de trouver le pack \u00e0 " + rawItem.packSize + " items"));
        return false;
    }
    
    @Override
    public boolean toRaw(final RawMerchantItem rawItem) {
        rawItem.price = this.getPrice();
        rawItem.packSize = this.getPackType().qty;
        this.m_item.toRaw(rawItem.item);
        return true;
    }
    
    public String getName() {
        return this.getItem().getName();
    }
    
    public String getIconUrl() {
        return this.getItem().getIconUrl();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_packTypes.clear();
        this.m_durations.clear();
        this.m_auctionDuration = null;
    }
    
    static {
        FIELDS = new String[] { "packType", "price", "formatedPrice", "item", "packList", "duration", "durationList" };
    }
    
    public static class DurationFieldProvider implements FieldProvider
    {
        public static final String DURATION_FIELD = "duration";
        public static final String SELECTED_FIELD = "selected";
        public static final String TAX_FACTOR_FIELD = "taxFactor";
        private final AuctionDuration m_duration;
        private boolean m_selected;
        public final String[] FIELDS;
        
        public DurationFieldProvider(final AuctionDuration duration, final boolean selected) {
            super();
            this.FIELDS = new String[] { "duration", "selected", "taxFactor" };
            this.m_duration = duration;
            this.m_selected = selected;
        }
        
        public String getDurationRepresentation() {
            return WakfuTranslator.getInstance().getString("duration.hour", this.m_duration.timeMs / 3600000L);
        }
        
        public AuctionDuration getDuration() {
            return this.m_duration;
        }
        
        public void setSelected(final boolean selected) {
            this.m_selected = selected;
        }
        
        @Override
        public String[] getFields() {
            return this.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("duration")) {
                return this.getDurationRepresentation();
            }
            if (fieldName.equals("selected")) {
                return this.m_selected;
            }
            if (fieldName.equals("taxFactor")) {
                return WakfuTranslator.getInstance().getString("desc.taxFactor", (int)(this.m_duration.taxFactor * 100.0));
            }
            return null;
        }
        
        @Override
        public void setFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void prependFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void appendFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public boolean isFieldSynchronisable(final String fieldName) {
            return false;
        }
        
        @Override
        public String toString() {
            return this.getDurationRepresentation();
        }
    }
}
