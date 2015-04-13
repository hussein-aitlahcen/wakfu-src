package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class WorkerTestCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        ProcessScheduler.getInstance().schedule(new Runnable() {
            private long m_previousTime = System.currentTimeMillis();
            
            @Override
            public void run() {
                final long now = System.currentTimeMillis();
                System.err.println("Delta time : " + (now - this.m_previousTime));
                if (now - this.m_previousTime < 1000L) {
                    final int bp = 0;
                }
                this.m_previousTime = now;
            }
        }, 1000L, -1);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorkerTestCommand.class);
    }
}
