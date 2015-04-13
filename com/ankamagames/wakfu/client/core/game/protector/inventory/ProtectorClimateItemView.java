package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.common.game.climate.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorClimateItemView extends ProtectorMerchantItemView
{
    public static final String TEMPERATURE_ICON_URL = "temperatureIconUrl";
    public static final String RAIN_ICON_URL = "rainIconUrl";
    public static final String WIND_ICON_URL = "windIconUrl";
    private final int m_climateBuffId;
    private final String m_name;
    private final String m_description;
    private int[] m_iconIds;
    
    public ProtectorClimateItemView(final ProtectorMerchantInventoryItem merchantItem, final int climateBuffId) {
        super(merchantItem);
        this.m_climateBuffId = climateBuffId;
        final ClimateBonus bonus = ClimateBonusManager.INSTANCE.getBonus(this.m_climateBuffId);
        if (bonus != null) {
            this.m_name = WakfuTranslator.getInstance().getString(52, this.m_climateBuffId, new Object[0]);
            this.m_description = WakfuTranslator.getInstance().getString(53, this.m_climateBuffId, new Object[0]);
            this.m_iconIds = new int[3];
            if (bonus.getTemperature() > 0.0f) {
                this.m_iconIds[0] = 0;
            }
            else if (bonus.getTemperature() < 0.0f) {
                this.m_iconIds[0] = 1;
            }
            else {
                this.m_iconIds[0] = -1;
            }
            if (bonus.getPrecipitations() > 0.0f) {
                this.m_iconIds[1] = 2;
            }
            else if (bonus.getPrecipitations() < 0.0f) {
                this.m_iconIds[1] = 3;
            }
            else {
                this.m_iconIds[1] = -1;
            }
            if (bonus.getWind() > 0.0f) {
                this.m_iconIds[2] = 4;
            }
            else if (bonus.getWind() < 0.0f) {
                this.m_iconIds[2] = 5;
            }
            else {
                this.m_iconIds[2] = -1;
            }
        }
        else {
            this.m_name = "";
            this.m_description = "";
        }
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public ProtectorWalletContext getWalletContext() {
        return ProtectorWalletContext.CLIMATE;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("nameWithRemainingTime")) {
            final Object s = this.getRemainingTimeString();
            if (s == null) {
                return this.getName();
            }
            return new StringBuilder(this.m_name).append(" (").append(s).append(")");
        }
        else {
            if (fieldName.equals("nameWithDuration")) {
                return new StringBuilder(this.m_name).append(" (").append(this.getDurationString()).append(")");
            }
            if (fieldName.equals("iconUrl")) {
                return null;
            }
            if (fieldName.equals("temperatureIconUrl")) {
                if (this.m_iconIds[0] == -1) {
                    return null;
                }
                return WakfuConfiguration.getInstance().getIconUrl("climateBonusIconsPath", "defaultIconPath", this.m_iconIds[0]);
            }
            else if (fieldName.equals("rainIconUrl")) {
                if (this.m_iconIds[1] == -1) {
                    return null;
                }
                return WakfuConfiguration.getInstance().getIconUrl("climateBonusIconsPath", "defaultIconPath", this.m_iconIds[1]);
            }
            else {
                if (!fieldName.equals("windIconUrl")) {
                    return super.getFieldValue(fieldName);
                }
                if (this.m_iconIds[2] == -1) {
                    return null;
                }
                return WakfuConfiguration.getInstance().getIconUrl("climateBonusIconsPath", "defaultIconPath", this.m_iconIds[2]);
            }
        }
    }
    
    public int getClimateBuffId() {
        return this.m_climateBuffId;
    }
}
