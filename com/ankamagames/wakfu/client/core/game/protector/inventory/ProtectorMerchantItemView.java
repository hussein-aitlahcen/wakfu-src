package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import java.text.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public abstract class ProtectorMerchantItemView extends ImmutableFieldProvider
{
    protected static Logger m_logger;
    public static final String NAME_FIELD = "name";
    public static final String NAME_WITH_DURATION_FIELD = "nameWithDuration";
    public static final String NAME_WITH_REMAINING_TIME_FIELD = "nameWithRemainingTime";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ITEM_PRICE = "price";
    public static final String DURATION = "duration";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String BUY_ENABLED = "buyEnabled";
    private final ProtectorMerchantInventoryItem m_merchantItem;
    
    protected ProtectorMerchantItemView(final ProtectorMerchantInventoryItem merchantItem) {
        super();
        this.m_merchantItem = merchantItem;
    }
    
    public abstract String getName();
    
    public abstract ProtectorWalletContext getWalletContext();
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("price")) {
            return NumberFormat.getIntegerInstance().format(this.getMerchantItem().getPrice()) + "§";
        }
        if (fieldName.equals("buyEnabled")) {
            final ProtectorWalletHandler wallet = ProtectorView.getInstance().getProtector().getWallet();
            return wallet != null && wallet.getAmountOfCash(this.getWalletContext()) >= this.getMerchantItem().getPrice();
        }
        if (fieldName.equals("duration")) {
            return this.getDurationString();
        }
        if (fieldName.equals("remainingTime")) {
            return this.getRemainingTimeString();
        }
        return null;
    }
    
    public void updateTime() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "nameWithRemainingTime", "remainingTime");
    }
    
    public void onWalletUpdated() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "buyEnabled");
    }
    
    protected Object getRemainingTimeString() {
        final long remaining = (this.m_merchantItem.getStartDate() + this.m_merchantItem.getDuration() - WakfuGameCalendar.getInstance().getInternalTimeInMs()) / 1000L;
        if (this.m_merchantItem.getStartDate() == -1L || remaining <= 0L) {
            return null;
        }
        return new TextWidgetFormater().append(WakfuTranslator.getInstance().getString("remaining.duration")).append(" ").append(TimeUtils.getVeryShortDescription(new GameInterval(remaining))).finishAndToString();
    }
    
    protected String getDurationString() {
        return new TextWidgetFormater().append(WakfuTranslator.getInstance().getString("duration")).append(" ").append(TimeUtils.getVeryShortDescription(GameInterval.fromSeconds(this.m_merchantItem.getDuration() / 1000L))).finishAndToString();
    }
    
    public ProtectorMerchantInventoryItem getMerchantItem() {
        return this.m_merchantItem;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    static {
        ProtectorMerchantItemView.m_logger = Logger.getLogger((Class)ProtectorMerchantItemView.class);
    }
}
