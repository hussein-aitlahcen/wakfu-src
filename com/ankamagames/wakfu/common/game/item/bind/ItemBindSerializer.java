package com.ankamagames.wakfu.common.game.item.bind;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public final class ItemBindSerializer
{
    private static final Logger m_logger;
    
    public static ItemBind unserialize(final RawItemBind raw) {
        final ItemBindType bindType = ItemBindType.getFromId(raw.type);
        return new ItemBindModel(bindType, raw.data);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemBindSerializer.class);
    }
}
