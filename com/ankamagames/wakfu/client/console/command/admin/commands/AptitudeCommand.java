package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "aptitude | apt", commandParameters = "&lt;-restat&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Launch a restat.", commandObsolete = true)
public final class AptitudeCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int RESTAT = 1;
    private final int m_commandId;
    private final String[] m_args;
    
    public AptitudeCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 1: {
                return this.m_args.length == 0;
            }
            case 0: {
                return true;
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
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 1: {
                    this.restat(networkEntity);
                    break;
                }
                case 0: {
                    this.help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private void restat(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)124);
        networkEntity.sendMessage(netMessage);
    }
    
    private void help() {
        ModerationCommand.log("-restat : active le restat des aptitude");
    }
    
    static {
        m_logger = Logger.getLogger((Class)AptitudeCommand.class);
    }
}
