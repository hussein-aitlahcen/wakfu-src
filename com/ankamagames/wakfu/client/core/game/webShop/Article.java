package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.net.download.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class Article extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    private static final Logger m_logger;
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ICON_URL = "iconUrl";
    public static final String SUBTITLE = "subtitle";
    public static final String PRICE = "price";
    public static final String ORIGINAL_PRICE = "originalPrice";
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_ID = "currencyId";
    public static final String STOCK = "stock";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String IS_A_RENTAL = "isARental";
    public static final String RENTAL_DESCRIPTION = "rentalDescription";
    public static final String CONTENT = "content";
    public static final String CONTENTS = "contents";
    public static final String PROMOTIONS = "promotions";
    public static final String HAS_PROMOTIONS = "hasPromotions";
    public static final String CAN_BE_EQUIPED = "canBeEquiped";
    public static final String HAS_ENOUGH_MONEY = "hasEnoughMoney";
    public static final String IS_AVAILABLE = "isAvailable";
    public static final String IS_SERVER_RESTRICTION_OK = "isServerRestrictionOK";
    private final int m_id;
    private final String m_name;
    private final String m_description;
    private final String m_subtitle;
    private final float m_price;
    private final float m_originalPrice;
    private final Currency m_currency;
    private int m_stock;
    private final GameDate m_endDate;
    private final ArrayList<SubArticle> m_subArticles;
    private final ArrayList<Promotion> m_promotions;
    private final ArrayList<ImageData> m_imageData;
    private DownloadableFieldProvider m_iconUrl;
    private boolean m_canBeEquiped;
    
    public Article(final int id, final String name, final String description, final String subtitle, final float price, final float originalPrice, final Currency currency, final int stock, final ArrayList<SubArticle> subArticles, final ArrayList<ImageData> imageUrl, final ArrayList<Promotion> promotions, final GameDate endDate) {
        super();
        this.m_id = id;
        this.m_name = name;
        this.m_description = description;
        this.m_subtitle = subtitle;
        this.m_price = price;
        this.m_originalPrice = originalPrice;
        this.m_currency = currency;
        this.m_stock = stock;
        this.m_subArticles = subArticles;
        this.m_promotions = promotions;
        this.m_imageData = imageUrl;
        this.m_canBeEquiped = false;
        this.m_endDate = endDate;
        this.checkEntry();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public String getSubtitle() {
        return this.m_subtitle;
    }
    
    public float getPrice() {
        return this.m_price;
    }
    
    public GameDate getEndDate() {
        return this.m_endDate;
    }
    
    public float getOriginalPrice() {
        return this.m_originalPrice;
    }
    
    public Currency getCurrency() {
        return this.m_currency;
    }
    
    public int getStock() {
        return this.m_stock;
    }
    
    public void buyStock() {
        if (this.m_stock > 0) {
            --this.m_stock;
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "stock", "isAvailable");
        }
    }
    
    public boolean containsType(final ArticleType type) {
        for (int i = 0, size = this.m_subArticles.size(); i < size; ++i) {
            final SubArticle subArticle = this.m_subArticles.get(i);
            if (subArticle.getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsStringMetaData(final String key) {
        for (int i = 0, size = this.m_subArticles.size(); i < size; ++i) {
            final SubArticle subArticle = this.m_subArticles.get(i);
            if (subArticle.getMetaStringValue(key) != null) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<SubArticle> getSubArticles() {
        return this.m_subArticles;
    }
    
    @Override
    public String[] getFields() {
        return Article.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        if (fieldName.equals("iconUrl")) {
            return (this.m_iconUrl == null) ? null : this.m_iconUrl.getUrl();
        }
        if (fieldName.equals("subtitle")) {
            return this.m_subtitle;
        }
        if (fieldName.equals("price")) {
            return this.m_price;
        }
        if (fieldName.equals("originalPrice")) {
            return (this.m_originalPrice > 0.0f) ? new TextWidgetFormater().c().append(this.m_originalPrice).finishAndToString() : null;
        }
        if (fieldName.equals("currency")) {
            return this.m_currency.getIconUrl();
        }
        if (fieldName.equals("currencyId")) {
            return this.m_currency.getId();
        }
        if (fieldName.equals("stock")) {
            if (this.m_stock == -1) {
                return null;
            }
            if (this.m_stock == 0) {
                return WakfuTranslator.getInstance().getString("webShop.soldOut");
            }
            return WakfuTranslator.getInstance().getString("playerGift.itemCount", this.m_stock);
        }
        else {
            if (fieldName.equals("remainingTime")) {
                return this.getRemainingTime();
            }
            if (fieldName.equals("content") && this.m_subArticles.size() == 1) {
                return this.m_subArticles.get(0).getItem();
            }
            if (fieldName.equals("contents")) {
                final Collection<AbstractReferenceItem> items = new ArrayList<AbstractReferenceItem>();
                for (int i = 0, size = this.m_subArticles.size(); i < size; ++i) {
                    items.add(this.m_subArticles.get(i).getItem());
                }
                return items;
            }
            if (fieldName.equals("promotions")) {
                return this.m_promotions;
            }
            if (fieldName.equals("hasPromotions")) {
                return this.m_promotions != null && !this.m_promotions.isEmpty();
            }
            if (fieldName.equals("isARental")) {
                return this.isARental();
            }
            if (fieldName.equals("rentalDescription")) {
                return this.getRentalDescription();
            }
            if (fieldName.equals("canBeEquiped")) {
                return this.m_canBeEquiped;
            }
            if (fieldName.equals("hasEnoughMoney")) {
                final WebShopSession webShopSession = (WebShopSession)PropertiesProvider.getInstance().getObjectProperty("webShop");
                if (webShopSession == null) {
                    return false;
                }
                if (this.m_currency.isHardCurrency()) {
                    return true;
                }
                return this.m_price <= webShopSession.getWalletAmount(this.m_currency);
            }
            else {
                if (fieldName.equals("isAvailable")) {
                    return this.isAvailable();
                }
                if (fieldName.equals("isServerRestrictionOK")) {
                    return this.isServerRestrictionOK();
                }
                return null;
            }
        }
    }
    
    public boolean isServerRestrictionOK() {
        for (int i = 0; i < this.m_subArticles.size(); ++i) {
            final SubArticle subArticle = this.m_subArticles.get(i);
            if (subArticle.isServerRestrictionOK()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isTimed() {
        return this.m_endDate != null && !this.m_endDate.isNull();
    }
    
    public void update(final GameDateConst endDate, final int stock) {
        this.m_stock = stock;
        this.m_endDate.set(endDate);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "stock", "remainingTime", "isAvailable");
    }
    
    private boolean isAvailable() {
        if (this.m_stock == 0) {
            return false;
        }
        if (this.m_endDate != null && !this.m_endDate.isNull()) {
            final GameInterval remainingInterval = WakfuGameCalendar.getInstance().getDate().timeTo(this.m_endDate);
            if (!remainingInterval.isPositive()) {
                return false;
            }
        }
        return true;
    }
    
    private String getRemainingTime() {
        if (this.m_endDate.isNull()) {
            return null;
        }
        final GameInterval remainingInterval = WakfuGameCalendar.getInstance().getDate().timeTo(this.m_endDate);
        return remainingInterval.isPositive() ? TimeUtils.getShortDescription(remainingInterval) : WakfuTranslator.getInstance().getString("webShop.soldOut");
    }
    
    @Nullable
    public String getRentalDescription() {
        return (this.m_subArticles.size() == 1) ? this.m_subArticles.get(0).getRentalDescription() : null;
    }
    
    public boolean isARental() {
        return this.m_subArticles.size() == 1 && this.m_subArticles.get(0).isARental();
    }
    
    private void checkEntry() {
        if (this.m_imageData == null) {
            return;
        }
        final ImageData imageData = ImageDataHelper.findImageData(this.m_imageData, 200, 200);
        if (imageData != null) {
            this.m_iconUrl = ImageDataHelper.load(imageData, this, "iconUrl");
        }
        for (int i = 0, size = this.m_subArticles.size(); i < size; ++i) {
            final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(this.m_subArticles.get(i).getContentId());
            if (item != null) {
                if (item.getItemType().getEquipmentPositions().length != 0) {
                    this.m_canBeEquiped = true;
                    break;
                }
                final AbstractItemAction action = item.getItemAction();
                if (action instanceof SplitItemSetItemAction) {
                    this.m_canBeEquiped = true;
                    break;
                }
            }
        }
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    public void previewArticle() {
        if (!this.m_canBeEquiped) {
            return;
        }
        for (int i = 0, size = this.m_subArticles.size(); i < size; ++i) {
            final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_subArticles.get(i).getContentId());
            if (referenceItem != null) {
                if (referenceItem.getItemType().getEquipmentPositions().length != 0) {
                    UIStuffPreviewFrame.INSTANCE.equipItem(referenceItem);
                }
                else {
                    final AbstractItemAction action = referenceItem.getItemAction();
                    if (action instanceof SplitItemSetItemAction) {
                        final SplitItemSetItemAction splitItemSetItemAction = (SplitItemSetItemAction)action;
                        final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(splitItemSetItemAction.getItemSetId());
                        if (itemSet != null) {
                            for (final int itemId : itemSet.getItemIds()) {
                                final ReferenceItem itemSetRefItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
                                UIStuffPreviewFrame.INSTANCE.equipItem(itemSetRefItem);
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Article.class);
    }
}
