package com.ankamagames.wakfu.client.core.game.market;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.market.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class MarketHistoryEntryView extends ImmutableFieldProvider implements MerchantItemView
{
    public static final String NAME_FIELD = "name";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String LEVEL_FIELD = "level";
    public static final String SOLD_QUANTITIES_TEXT_FIELD = "soldQuantitiesText";
    public static final String UNSOLD_QUANTITIES_TEXT_FIELD = "unsoldQuantitiesText";
    public static final String TOTAL_PRICE_FIELD = "totalPrice";
    public static final String RARITY_FIELD = "rarity";
    public static final String STACK_SIZE_FIELD = "stackSize";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String PACK_TYPE = "packType";
    public static final String HAS_UNSOLD_FIELD = "hasUnsold";
    public static final String REMAINING_TIME = "remainingDuration";
    public static String[] FIELDS;
    private short m_quantity;
    private short m_unsoldQuantity;
    private PackType m_packType;
    private int m_price;
    private Item m_item;
    private long m_id;
    private AuctionDuration m_auctionDuration;
    private GameDate m_releaseDate;
    
    public MarketHistoryEntryView(final MarketEntry marketEntry) {
        super();
        this.m_packType = PackType.ONE;
        this.m_id = marketEntry.getId();
        if (marketEntry.getRawItem().refId == 0) {
            (this.m_item = ReferenceItemManager.getInstance().getDefaultItem(marketEntry.getItemRefId())).setQuantity((short)1);
        }
        else {
            (this.m_item = new Item()).fromRaw(marketEntry.getRawItem());
            this.m_item.setQuantity((short)1);
            this.m_item = this.m_item.getCopy(GUIDGenerator.getGUID(), false);
        }
        this.m_packType = marketEntry.getPackType();
        this.m_price = marketEntry.getPackPrice();
        this.m_unsoldQuantity = marketEntry.getPackNumber();
        this.m_auctionDuration = marketEntry.getDuration();
        this.m_releaseDate = GameDate.fromLong(marketEntry.getReleaseDate());
    }
    
    public MarketHistoryEntryView(final MarketHistoryEntry historyEntry) {
        super();
        this.m_packType = PackType.ONE;
        this.m_id = historyEntry.getId();
        if (historyEntry.getRawInventoryItem() == null) {
            (this.m_item = ReferenceItemManager.getInstance().getDefaultItem(historyEntry.getItemRefId())).setQuantity((short)1);
        }
        else {
            (this.m_item = new Item()).fromRaw(historyEntry.getRawInventoryItem());
            this.m_item.setQuantity((short)1);
            this.m_item = this.m_item.getCopy(false);
        }
        this.m_packType = historyEntry.getPackType();
        this.m_price = historyEntry.getPackPrice();
        this.m_quantity = historyEntry.getPackSoldNumber();
    }
    
    @Override
    public String[] getFields() {
        return MarketHistoryEntryView.FIELDS;
    }
    
    public int getPrice() {
        return this.m_price;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_item.getName();
        }
        if (fieldName.equals("totalPrice")) {
            return this.m_price * this.m_quantity;
        }
        if (fieldName.equals("quantity")) {
            final int quantity = this.m_quantity;
            return (quantity > 1) ? String.valueOf(quantity) : null;
        }
        if (fieldName.equals("level")) {
            return this.m_item.getLevel();
        }
        if (fieldName.equals("packType")) {
            return PackTypeFieldProvider.getPackTypeRepresentation(this.m_packType);
        }
        if (fieldName.equals("soldQuantitiesText")) {
            return (this.m_quantity > 0) ? WakfuTranslator.getInstance().getString("soldQuantities", this.m_quantity) : null;
        }
        if (fieldName.equals("unsoldQuantitiesText")) {
            return (this.m_unsoldQuantity > 0) ? WakfuTranslator.getInstance().getString("unsoldQuantities", this.m_unsoldQuantity) : null;
        }
        if (fieldName.equals("rarity")) {
            final ItemRarity rarity = this.m_item.getRarity();
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(WakfuTranslator.getInstance().getString("item.rarity." + rarity.name()));
            return sb.finishAndToString();
        }
        if (fieldName.equals("hasUnsold")) {
            return this.m_unsoldQuantity > 0;
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getItemBigIconUrl(this.m_item.getGfxId());
        }
        if (!fieldName.equals("remainingDuration")) {
            return this.m_item.getFieldValue(fieldName);
        }
        if (this.m_unsoldQuantity <= 0) {
            return null;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        final GameInterval remainingDuration = new GameInterval(0, 0, 0, 30);
        remainingDuration.add(this.m_auctionDuration.interval);
        remainingDuration.substract(this.m_releaseDate.timeTo(now));
        return TimeUtils.getVeryShortDescriptionWithLongUnits(remainingDuration);
    }
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    @Override
    public Item getItem() {
        return this.m_item;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    public void setUnsoldQuantity(final short unsoldQuantity) {
        this.m_unsoldQuantity = unsoldQuantity;
    }
    
    static {
        MarketHistoryEntryView.FIELDS = new String[] { "name", "quantity", "totalPrice", "level", "soldQuantitiesText", "unsoldQuantitiesText", "rarity", "stackSize", "iconUrl", "packType", "hasUnsold" };
    }
}
