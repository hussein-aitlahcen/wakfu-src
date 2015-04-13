package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "ping", commandParameters = "&lt;start | stop&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Without parameter log ping result. With, enable or disable auto ping.", commandObsolete = false)
public class PingCommand extends ModerationCommand implements MessageHandler
{
    protected static final Logger m_logger;
    private static int m_currentKey;
    private static boolean m_autoPingActive;
    private static long m_autoPingClockId;
    private boolean m_once;
    private boolean m_start;
    
    public PingCommand() {
        super();
        this.m_once = true;
    }
    
    public PingCommand(final boolean start) {
        super();
        this.m_once = false;
        this.m_start = start;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        if (this.m_once) {
            this.ping();
        }
        else if (PingCommand.m_autoPingActive) {
            this.stopAutoPing();
        }
        else {
            this.startAutoPing();
        }
    }
    
    private void startAutoPing() {
        if (!PingCommand.m_autoPingActive) {
            PingCommand.m_autoPingClockId = MessageScheduler.getInstance().addClock(this, 1000L, 1);
            PingCommand.m_autoPingActive = true;
        }
    }
    
    private void stopAutoPing() {
        if (PingCommand.m_autoPingActive) {
            MessageScheduler.getInstance().removeClock(PingCommand.m_autoPingClockId);
            PingCommand.m_autoPingActive = false;
            PingCommand.m_autoPingClockId = 0L;
        }
    }
    
    private boolean ping() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            try {
                ++PingCommand.m_currentKey;
                networkEntity.sendMessage(new PingMessage((byte)0, PingCommand.m_currentKey));
                networkEntity.sendMessage(new PingMessage((byte)2, PingCommand.m_currentKey));
                networkEntity.sendMessage(new PingMessage((byte)3, PingCommand.m_currentKey));
                networkEntity.sendMessage(new PingMessage((byte)1, PingCommand.m_currentKey));
                networkEntity.sendMessage(new PingMessage((byte)4, PingCommand.m_currentKey));
                return true;
            }
            catch (Exception e) {
                PingCommand.m_logger.error((Object)"[PING COMMAND] exception lev\u00e9e", (Throwable)e);
                return false;
            }
        }
        ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        return false;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message.getId() == Integer.MIN_VALUE) {
            if (!this.ping()) {
                this.stopAutoPing();
            }
            return false;
        }
        return true;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)PingCommand.class);
        PingCommand.m_currentKey = 0;
        PingCommand.m_autoPingActive = false;
        PingCommand.m_autoPingClockId = 0L;
    }
}
