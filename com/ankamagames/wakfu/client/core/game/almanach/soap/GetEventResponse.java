package com.ankamagames.wakfu.client.core.game.almanach.soap;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.wakfu.client.core.game.almanach.zodiac.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.net.soap.*;

public class GetEventResponse implements SOAPAnswerProcessor<GetEvent>
{
    private static final Logger m_logger;
    private static final String GET_EVENT_RESPONSE = "GetEventResponse";
    private static final String MONTH = "month";
    private static final String ZODIAC = "zodiac";
    private static final String EVENT = "event";
    
    @Override
    public void process(final SOAPEnvelope envelope, final GetEvent provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("GetEventResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            return;
        }
        if (data.getValue("event") == NilData.VALUE) {
            return;
        }
        try {
            final AlmanachEventEntry entry = AlmanachEventEntry.createFromSOAP((MapData)data.getValue("event"));
            final AlmanachZodiacEntry zodiac = AlmanachZodiacEntry.createFromSOAP((MapData)data.getValue("zodiac"));
            final AlmanachMonthEntry month = AlmanachMonthEntry.createFromSOAP((MapData)data.getValue("month"));
            final GameDateConst date = provider.getDate();
            AlmanachViewManager.INSTANCE.setAlmanachEventEntryData(entry, date);
            AlmanachViewManager.INSTANCE.setAlmanachZodiakEntryData(zodiac, date);
            AlmanachViewManager.INSTANCE.setAlmanachMonthEntryData(month, date);
        }
        catch (Exception e) {
            GetEventResponse.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es d'almanach", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final GetEvent provider) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetEventResponse.class);
    }
}
