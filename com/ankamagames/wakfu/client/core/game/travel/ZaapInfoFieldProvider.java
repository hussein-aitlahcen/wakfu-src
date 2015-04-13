package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.configuration.*;

public class ZaapInfoFieldProvider extends ImmutableFieldProvider implements TravelInfo
{
    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String IS_ACTIVATED_FIELD = "isActivated";
    public static final String IS_ENABLED_FIELD = "isEnabled";
    public static final String ERROR_TEXT_FIELD = "errorText";
    public static final String[] FIELDS;
    public static final byte NO_HIGH_LEVEL_INSTANCE_ACCESS = 1;
    private final long m_zaapFrom;
    private final boolean m_enabled;
    private final TByteHashSet m_disableReasons;
    private final ZaapLink m_zaapInfo;
    
    public ZaapInfoFieldProvider(final ZaapLink zaapInfo, final long zaapFrom, final boolean enabled, final TByteHashSet disableReasons) {
        super();
        this.m_zaapInfo = zaapInfo;
        this.m_zaapFrom = zaapFrom;
        this.m_enabled = enabled;
        this.m_disableReasons = disableReasons;
    }
    
    @Override
    public String[] getFields() {
        return ZaapInfoFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("price")) {
            return this.getCost();
        }
        if (fieldName.equals("isEnabled")) {
            return this.isEnabled();
        }
        if (fieldName.equals("isActivated")) {
            return this.isActive();
        }
        if (!fieldName.equals("errorText")) {
            return null;
        }
        if (this.m_enabled) {
            return null;
        }
        final TextWidgetFormater twf = new TextWidgetFormater();
        if (this.m_disableReasons.contains((byte)1)) {
            twf.newLine();
            twf.append(WakfuTranslator.getInstance().getString("error.playerNotInstanceAccessRight"));
        }
        return (twf.length() == 0) ? null : twf.finishAndToString();
    }
    
    @Override
    public long getId() {
        return (this.m_zaapInfo.getEndZaapId() == this.m_zaapFrom) ? this.m_zaapInfo.getStartZaapId() : ((long)this.m_zaapInfo.getEndZaapId());
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(36, (int)this.getId(), new Object[0]);
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public boolean isActive() {
        return true;
    }
    
    @Override
    public int getCost() {
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.ZAAP_FREE)) {
            return 0;
        }
        return this.m_zaapInfo.getCost();
    }
    
    static {
        FIELDS = new String[] { "name", "price" };
    }
}
