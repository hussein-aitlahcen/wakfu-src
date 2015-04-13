package com.ankamagames.wakfu.client.core.game.almanach.soap;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class GetEvent implements SOAPRequestProvider
{
    private static final String GET_EVENT_OPERATION = "GetEvent";
    private static final String LANG_PARAM_NAME = "sLang";
    private static final String DATE_PARAM_NAME = "sDate";
    private final GameDateConst m_date;
    
    public GetEvent(@NotNull final GameDateConst date) {
        super();
        this.m_date = date;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("GetEvent");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("sDate", convertToTimestamp(this.m_date));
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    private static String convertToTimestamp(final GameDateConst date) {
        if (date == null || date.isNull()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final int year = date.getYear();
        final int month = date.getMonth();
        final int day = date.getDay();
        sb.append(year).append('-');
        if (month < 10) {
            sb.append('0');
        }
        sb.append(month).append('-');
        if (day < 10) {
            sb.append('0');
        }
        sb.append(day);
        return sb.toString();
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final GetEvent that = (GetEvent)obj;
        return this.m_date.equals(that.m_date);
    }
    
    @Override
    public int hashCode() {
        return this.m_date.hashCode();
    }
    
    @Override
    public String toString() {
        return "GetEvent{m_date=" + this.m_date + '}';
    }
}
