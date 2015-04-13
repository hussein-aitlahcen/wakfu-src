package com.ankamagames.wakfu.client.core.game.dimensionalBag.room;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class DimensionalBagRoomContentFactory implements RoomContentFactory
{
    protected static final Logger m_logger;
    public static final DimensionalBagRoomContentFactory INSTANCE;
    
    @Override
    public RoomContent createContent(final PersonalSpace bag, final RawInteractiveElementPersistantData data) {
        final WakfuClientMapInteractiveElement element = ((InteractiveElementFactory<WakfuClientMapInteractiveElement, C>)WakfuClientInteractiveElementFactory.getInstance()).createInteractiveElement(data.templateId);
        if (element != null && element instanceof ItemizableInfo) {
            final ItemizableInfo ie = (ItemizableInfo)element;
            ie.setBag(bag);
            ie.fromRawPersistantData(data);
            return ie;
        }
        DimensionalBagRoomContentFactory.m_logger.error((Object)("Impossible de cr\u00e9er un RoomContent de templateId=" + data.templateId));
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagRoomContentFactory.class);
        INSTANCE = new DimensionalBagRoomContentFactory();
    }
}
