package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;

public class FrescoCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        UIFrescoFrame.getInstance().setFrescoId(0);
        WakfuGameEntity.getInstance().pushFrame(UIFrescoFrame.getInstance());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FrescoCommand.class);
    }
}
