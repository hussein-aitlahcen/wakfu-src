package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.ui.protocol.handler.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public class UIWorldFrame implements MessageFrame
{
    private static final boolean DEBUG_MODE = false;
    private static final Logger m_logger;
    private static final UIWorldFrame m_instance;
    private final TShortObjectHashMap<UIMessageHandler> m_handlers;
    
    public UIWorldFrame() {
        super();
        (this.m_handlers = new TShortObjectHashMap<UIMessageHandler>()).put((short)17013, new DimensionalBagMessageHandler());
    }
    
    public static UIWorldFrame getInstance() {
        return UIWorldFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final UIMessageHandler handler = this.m_handlers.get((short)message.getId());
        return handler == null || handler.onMessage(message);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            return;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            return;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIWorldFrame.class);
        m_instance = new UIWorldFrame();
    }
}
