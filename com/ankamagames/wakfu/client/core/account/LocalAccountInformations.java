package com.ankamagames.wakfu.client.core.account;

import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.configuration.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class LocalAccountInformations extends AccountInformations implements FieldProvider
{
    public static final String NICKNAME_FIELD = "nickname";
    public static final String EXPIRATION_DATE_FIELD = "expirationDate";
    public static final String[] FIELDS;
    
    @Override
    public long getAccountExpirationDate() {
        final long time = super.getAccountExpirationDate();
        final String timeZoneString = SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.CALENDAR_TZ);
        final long deltaTime = SystemConfiguration.INSTANCE.getLongValue(SystemConfigurationType.CALENDAR_DELTA);
        final TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        return time + timeZone.getRawOffset() + deltaTime;
    }
    
    @Override
    public String[] getFields() {
        return LocalAccountInformations.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("nickname")) {
            return this.getAccountNickName();
        }
        if (fieldName.equals("expirationDate")) {
            long time = this.getAccountExpirationDate();
            time = Math.max(time - new Date().getTime(), 0L);
            final short years = (short)(time / 31536000000L);
            time -= years * 31536000000L;
            final short months = (short)(time / 2678400000L);
            time -= months * 2678400000L;
            final short days = (short)(time / 86400000L);
            time -= days * 86400000L;
            final short hours = (short)(time / 3600000L);
            time -= hours * 3600000L;
            final short minutes = (short)(time / 60000L);
            final WakfuTranslator translator = WakfuTranslator.getInstance();
            return translator.getString("dateFormat.yearMonthDayHourMin", years, months, days, hours, minutes);
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
    
    static {
        FIELDS = new String[] { "nickname", "expirationDate" };
    }
}
