package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class MerchantInventory extends AbstractMerchantInventory implements FieldProvider
{
    public static final String BROWSING_BAG_INVENTORY_FIELD = "browsingBagInventory";
    public static final String BAG_INVENTORY_FIELD = "bagInventory";
    public static final String BAG_TYPE_FIELD = "bagType";
    public static final String LOCKED_FIELD = "locked";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String FLEA_TITLE_FIELD = "fleaTitle";
    public static final String FLEA_MARKET_REGISTERED = "fleaMarketRegistered";
    public static final String CURRENT_SIZE_FIELD = "currentSize";
    public static final String[] FIELDS;
    private String m_fleaTitle;
    private boolean m_fleaMarketRegistered;
    private int m_size;
    private String m_iconUrl;
    
    public MerchantInventory(final long uid, final MerchantItemType requiredItemType, final short slots, final byte maxPackSize) {
        super(uid, MerchantInventoryItemProvider.getInstance(), requiredItemType, slots, maxPackSize);
        this.m_fleaTitle = "";
    }
    
    @Override
    public AbstractMerchantInventoryItem makeMerchantInventoryItem() {
        return MerchantInventoryItem.checkout();
    }
    
    @Override
    public String[] getFields() {
        return MerchantInventory.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("browsingBagInventory")) {
            final AbstractMerchantInventoryItem[] content = new AbstractMerchantInventoryItem[this.getMaximumSize()];
            int i = 0;
            for (final AbstractMerchantInventoryItem c : this) {
                content[i] = c;
                ++i;
            }
            return content;
        }
        if (fieldName.equals("bagInventory")) {
            final ArrayList<AbstractMerchantInventoryItem> content2 = new ArrayList<AbstractMerchantInventoryItem>();
            final Iterator<AbstractMerchantInventoryItem> it = ((ArrayInventory<AbstractMerchantInventoryItem, R>)this).iterator(false);
            while (it.hasNext()) {
                content2.add(it.next());
            }
            return content2;
        }
        if (fieldName.equals("bagType")) {
            String s = "";
            final MerchantItemType requiredItemType = this.getRequiredItemType();
            if (requiredItemType != null) {
                final TShortIterator it2 = requiredItemType.getItemCategoriesIterator();
                while (it2.hasNext()) {
                    final short id = it2.next();
                    s += WakfuTranslator.getInstance().getString(14, id, new Object[0]);
                    if (it2.hasNext()) {
                        s += ", ";
                    }
                }
                if (s.length() > 0) {
                    s = WakfuTranslator.getInstance().getString("market.authorizedItemTypes", s);
                }
            }
            return (s.length() == 0) ? null : s;
        }
        if (fieldName.equals("locked")) {
            return this.isLocked();
        }
        if (fieldName.equals("iconUrl")) {
            return this.m_iconUrl;
        }
        if (fieldName.equals("fleaTitle")) {
            return this.m_fleaTitle;
        }
        if (fieldName.equals("fleaMarketRegistered")) {
            return this.m_fleaMarketRegistered;
        }
        if (fieldName.equals("currentSize")) {
            return this.m_size;
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
    
    public void notifyChanges() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "browsingBagInventory", "bagInventory", "bagType", "locked", "iconUrl", "fleaTitle", "fleaMarketRegistered");
    }
    
    public void setFleaTitle(final String fleaTitle) {
        this.m_fleaTitle = fleaTitle;
    }
    
    public void setFleaMarketRegistered(final boolean fleaMarketRegistered) {
        this.m_fleaMarketRegistered = fleaMarketRegistered;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fleaMarketRegistered");
    }
    
    public void setSize(final int size) {
        this.m_size = size;
    }
    
    public int getSize() {
        return this.m_size;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_iconUrl = WakfuConfiguration.getInstance().getItemSmallIconUrl(gfxId);
    }
    
    static {
        FIELDS = new String[] { "browsingBagInventory", "bagInventory", "bagType", "locked", "iconUrl", "fleaTitle", "fleaMarketRegistered", "currentSize" };
    }
}
