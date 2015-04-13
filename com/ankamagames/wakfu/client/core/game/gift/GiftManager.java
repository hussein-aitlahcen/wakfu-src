package com.ankamagames.wakfu.client.core.game.gift;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;

public class GiftManager extends ImmutableFieldProvider
{
    private static GiftManager m_instance;
    public static final String GIFT_PACKAGES_FIELD = "giftPackages";
    public static final String NUM_PACKAGES_FIELD = "numPackages";
    public static final String SELECTED_PACKAGE_FIELD = "selectedPackage";
    public static final String[] FIELDS;
    private final ArrayList<ConsumeRequest> m_requests;
    private final ArrayList<GiftPackage> m_giftPackages;
    private GiftPackage m_selected;
    
    private GiftManager() {
        super();
        this.m_requests = new ArrayList<ConsumeRequest>();
        this.m_giftPackages = new ArrayList<GiftPackage>();
        this.m_selected = null;
    }
    
    public static GiftManager getInstance() {
        return GiftManager.m_instance;
    }
    
    public void requestConsume(final GiftItem gift, final boolean consumeAll) {
        this.m_requests.add(new ConsumeRequest(this.m_selected, gift, consumeAll));
    }
    
    public void validateLastRequest(final boolean validate) {
        if (this.m_requests.size() == 0) {
            return;
        }
        final ConsumeRequest consumeRequest = this.m_requests.remove(0);
        if (!validate) {
            return;
        }
        final GiftPackage pkg = consumeRequest.getPackage();
        final int index = this.m_giftPackages.indexOf(pkg);
        if (index == -1) {
            return;
        }
        if (pkg.consume(consumeRequest.getGift(), consumeRequest.isAll())) {
            this.m_giftPackages.remove(pkg);
            if (this.m_selected == pkg) {
                this.m_selected = ((this.m_giftPackages.size() > 0) ? this.m_giftPackages.get(0) : null);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "giftPackages", "numPackages", "selectedPackage");
    }
    
    @Override
    public String[] getFields() {
        return GiftManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("giftPackages")) {
            return this.m_giftPackages;
        }
        if (fieldName.equals("selectedPackage")) {
            return this.m_selected;
        }
        if (fieldName.equals("numPackages")) {
            return this.m_giftPackages.size();
        }
        return null;
    }
    
    public void setPlayerGifts(final ArrayList<GiftPackage> giftList) {
        this.clear();
        this.m_giftPackages.addAll(giftList);
        this.fireGiftsListChanged();
        if (this.m_giftPackages.size() > 0) {
            this.setSelectedPackage(this.m_giftPackages.get(0));
        }
        else {
            this.setSelectedPackage(null);
        }
    }
    
    private void fireGiftsListChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "giftPackages");
    }
    
    public GiftPackage getSelectedPackage() {
        return this.m_selected;
    }
    
    public void setSelectedPackage(final GiftPackage selected) {
        this.m_selected = selected;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedPackage");
    }
    
    public boolean isEmpty() {
        return this.m_giftPackages.isEmpty();
    }
    
    public void clear() {
        this.m_giftPackages.clear();
        this.m_selected = null;
    }
    
    static {
        GiftManager.m_instance = new GiftManager();
        FIELDS = new String[] { "giftPackages", "numPackages", "selectedPackage" };
    }
}
