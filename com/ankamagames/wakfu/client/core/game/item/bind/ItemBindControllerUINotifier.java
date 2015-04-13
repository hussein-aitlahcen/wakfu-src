package com.ankamagames.wakfu.client.core.game.item.bind;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.bind.*;

public class ItemBindControllerUINotifier extends ItemBindController
{
    private static final Logger m_logger;
    
    public ItemBindControllerUINotifier(final Item item) {
        super(item);
    }
    
    @Override
    public void bind(final long data) throws ItemBindException {
        super.bind(data);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemBindControllerUINotifier.class);
    }
}
