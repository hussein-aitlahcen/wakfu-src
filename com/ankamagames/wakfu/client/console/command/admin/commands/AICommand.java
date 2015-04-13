package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "ai", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "ai h to show complete documentation. Permit to manage AI server.", commandObsolete = false)
public final class AICommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int ON = 1;
    public static final int OFF = 2;
    private final int m_commandId;
    private final String[] m_args;
    
    public AICommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0: {
                return this.m_args.length == 0;
            }
            case 1:
            case 2: {
                return this.m_args.length == 1;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 0: {
                    this.help();
                }
                case 1: {
                    this.on();
                }
                case 2: {
                    this.off();
                }
            }
        }
        catch (Exception e) {
            AICommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void off() {
        final String serverId = this.m_args[0];
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)191);
        msg.addStringParameter(serverId);
        networkEntity.sendMessage(msg);
    }
    
    private void on() {
        final String serverId = this.m_args[0];
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)192);
        msg.addStringParameter(serverId);
        networkEntity.sendMessage(msg);
    }
    
    private void help() {
        ModerationCommand.log("ai (help | h) : show command help");
        ModerationCommand.log("ai on serverId : permit the specified server to manage fights");
        ModerationCommand.log("ai off serverId : disable the management of fights for this server");
    }
    
    static {
        m_logger = Logger.getLogger((Class)AICommand.class);
    }
}
