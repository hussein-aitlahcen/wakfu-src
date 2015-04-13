package com.ankamagames.wakfu.client.core.protector.ecosystem;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class ProtectorEcosystemElement extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String ENDANGERED_STATUS = "endangeredStatus";
    public static final String ENDANGERED_STATUS_COLOR = "endangeredStatus";
    public static final String IS_PROTECTED = "isProtected";
    public static final String IS_EXTINCT = "isExtinct";
    public static final String CAN_BE_REINTRODUCED = "canBeReintroduced";
    public static final String PROTECTION_PRICE = "protectionPrice";
    public static final String PROTECTION_PRICE_VALUE = "protectionPriceValue";
    public static final String REINTRODUCTION_PRICE = "reintroductionPrice";
    public static final String REINTRODUCTION_PRICE_VALUE = "reintroductionPriceValue";
    public static final String ICON_URL = "iconUrl";
    public static final String GET_SEEDS_TEXT = "getSeedsText";
    protected final int m_familyId;
    protected final int m_parentFamilyId;
    protected final int m_protectPrice;
    protected final int m_reintroducePrice;
    
    protected ProtectorEcosystemElement(final int familyId, final int parentFamilyId, final int protectPrice, final int reintroducePrice) {
        super();
        this.m_familyId = familyId;
        this.m_parentFamilyId = parentFamilyId;
        this.m_protectPrice = protectPrice;
        this.m_reintroducePrice = reintroducePrice;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("isProtected")) {
            return this.isProtected();
        }
        if (fieldName.equals("isExtinct")) {
            return this.isExtinct();
        }
        if (fieldName.equals("protectionPrice")) {
            return this.m_protectPrice + "§";
        }
        if (fieldName.equals("reintroductionPrice")) {
            return this.m_reintroducePrice + "§";
        }
        if (fieldName.equals("protectionPriceValue")) {
            return this.m_protectPrice;
        }
        if (fieldName.equals("reintroductionPriceValue")) {
            return this.m_reintroducePrice;
        }
        if (fieldName.equals("iconUrl")) {
            return this.getIconUrl();
        }
        if (fieldName.equals("canBeReintroduced")) {
            return this.canBeReintroduced();
        }
        if (fieldName.equals("getSeedsText")) {
            return WakfuTranslator.getInstance().getString("craft.getSeeds", this.getResourceTypeSeedName());
        }
        if (fieldName.equals("endangeredStatus")) {
            if (this.isExtinct()) {
                return WakfuTranslator.getInstance().getString("protector.ecosystem.extinct.species");
            }
            if (this.isEndangered()) {
                return WakfuTranslator.getInstance().getString("protector.ecosystem.endangered.species");
            }
            return WakfuTranslator.getInstance().getString("protector.ecosystem.well.representated.species");
        }
        else {
            if (!fieldName.equals("endangeredStatus")) {
                return null;
            }
            if (this.isExtinct()) {
                return Color.RED.getRGBtoHex();
            }
            if (this.isEndangered()) {
                return Color.ORANGE.getRGBtoHex();
            }
            return Color.BLACK.getRGBtoHex();
        }
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public int getProtectPrice() {
        return this.m_protectPrice;
    }
    
    public int getReintroducePrice() {
        return this.m_reintroducePrice;
    }
    
    protected abstract boolean canBeReintroduced();
    
    protected abstract String getIconUrl();
    
    protected abstract String getName();
    
    protected abstract boolean isProtected();
    
    protected abstract boolean isEndangered();
    
    protected abstract boolean isExtinct();
    
    public abstract boolean isMonster();
    
    public abstract String getResourceTypeSeedName();
    
    public void updateProtectedField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isProtected");
    }
    
    public void updateExtinctField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isExtinct", "endangeredStatus", "endangeredStatus", "iconUrl");
    }
}
