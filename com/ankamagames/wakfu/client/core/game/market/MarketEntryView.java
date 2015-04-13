package com.ankamagames.wakfu.client.core.game.market;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.game.market.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.gems.*;

public class MarketEntryView extends ImmutableFieldProvider implements MerchantItemView
{
    public static final String NAME_FIELD = "name";
    public static final String SELLER_NAME_FIELD = "sellerName";
    public static final String LEVEL_FIELD = "level";
    public static final String PRICE_FIELD = "price";
    public static final String RARITY_FIELD = "rarity";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String PACK_TYPE = "packType";
    public static final String REMAINING_DURATION_FIELD = "remainingDuration";
    public static String[] FIELDS;
    private int m_averagePrice;
    private String m_sellerName;
    private PackType m_packType;
    private int m_price;
    private short m_quantity;
    private Item m_item;
    private long m_id;
    private AuctionDuration m_auctionDuration;
    private GameDateConst m_releaseDate;
    private GemsDisplayer m_gems;
    
    public MarketEntryView(final MarketEntry marketEntry) {
        super();
        this.m_averagePrice = -1;
        this.m_packType = PackType.ONE;
        this.m_id = marketEntry.getId();
        this.m_quantity = marketEntry.getPackNumber();
        this.m_packType = marketEntry.getPackType();
        this.m_price = marketEntry.getPackPrice();
        this.m_sellerName = marketEntry.getSellerName();
        this.m_auctionDuration = marketEntry.getDuration();
        this.m_releaseDate = GameDate.fromLong(marketEntry.getReleaseDate());
        (this.m_item = new Item()).fromRaw(marketEntry.getRawItem());
        this.m_item.setQuantity((short)1);
        this.m_item = this.m_item.getCopy(GUIDGenerator.getGUID(), false);
    }
    
    @Override
    public String[] getFields() {
        return MarketEntryView.FIELDS;
    }
    
    public String getSeller() {
        return this.m_sellerName;
    }
    
    @Override
    public Item getItem() {
        return this.m_item;
    }
    
    public int getPrice() {
        return this.m_price;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_item.getName();
        }
        if (fieldName.equals("sellerName")) {
            return this.getSeller();
        }
        if (fieldName.equals("level")) {
            if (this.m_item.hasPet()) {
                return this.m_item.getPet().getLevel();
            }
            if (this.m_item.hasXp()) {
                return this.m_item.getXp().getLevel();
            }
            return this.m_item.getLevel();
        }
        else {
            if (fieldName.equals("packType")) {
                return PackTypeFieldProvider.getPackTypeRepresentation(this.m_packType);
            }
            if (fieldName.equals("price")) {
                return WakfuTranslator.getInstance().formatNumber(this.m_price);
            }
            if (fieldName.equals("rarity")) {
                final ItemRarity rarity = this.m_item.getRarity();
                final TextWidgetFormater sb = new TextWidgetFormater();
                sb.append(WakfuTranslator.getInstance().getString("item.rarity." + rarity.name()));
                return sb.finishAndToString();
            }
            if (fieldName.equals("quantity")) {
                return (this.m_quantity > 1) ? WakfuTranslator.getInstance().formatNumber(this.m_quantity) : null;
            }
            if (fieldName.equals("remainingDuration")) {
                final GameDateConst currentDate = WakfuGameCalendar.getInstance().getDate();
                GameInterval remainingDuration;
                if (this.m_auctionDuration.interval.greaterThan(this.m_releaseDate.timeTo(currentDate))) {
                    remainingDuration = new GameInterval(this.m_auctionDuration.interval);
                    remainingDuration.substract(this.m_releaseDate.timeTo(currentDate));
                }
                else {
                    remainingDuration = new GameInterval(this.m_releaseDate.timeTo(currentDate));
                    remainingDuration.substract(this.m_auctionDuration.interval);
                }
                if (remainingDuration.getDays() > 0 || remainingDuration.getHours() > 0) {
                    return WakfuTranslator.getInstance().getString("remainingDurationHours", remainingDuration.getDays() * 24 + remainingDuration.getHours());
                }
                if (remainingDuration.getMinutes() > 0) {
                    return WakfuTranslator.getInstance().getString("remainingDurationMinutes", remainingDuration.getMinutes());
                }
                return null;
            }
            else {
                if (fieldName.equals("iconUrl")) {
                    return WakfuConfiguration.getInstance().getItemBigIconUrl(this.m_item.getGfxId());
                }
                if (fieldName.equals("gems")) {
                    return this.getGemDisplayer();
                }
                return this.m_item.getFieldValue(fieldName);
            }
        }
    }
    
    private GemsDisplayer getGemDisplayer() {
        if (this.m_gems == null) {
            if (this.m_item.hasGems()) {
                final Gems gems = this.m_item.getGems();
                this.m_gems = new GemsDisplayer(null, gems);
            }
            else {
                this.m_gems = new GemsDisplayer(null, new Gems(this.getItem().getReferenceItem()));
            }
        }
        return this.m_gems;
    }
    
    public boolean isOverTimed() {
        final GameDateConst currentDate = WakfuGameCalendar.getInstance().getDate();
        return this.m_auctionDuration.interval.lowerThan(this.m_releaseDate.timeTo(currentDate));
    }
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    static {
        MarketEntryView.FIELDS = new String[] { "name", "sellerName", "level", "price", "rarity", "quantity", "iconUrl", "packType", "remainingDuration" };
    }
}
