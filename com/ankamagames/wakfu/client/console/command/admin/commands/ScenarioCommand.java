package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "scenario", commandParameters = "&lt;-t eventId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Force sending of TriggerServerEvent.", commandObsolete = false)
public final class ScenarioCommand extends ModerationCommand
{
    private static Logger m_logger;
    private final String[] m_args;
    
    public ScenarioCommand(final String... args) {
        super();
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        return this.m_args.length == 1;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            this.triggerEvent(networkEntity);
        }
        catch (Exception e) {
            ScenarioCommand.m_logger.error((Object)"Impossible d'executer la commande, Exception : ", (Throwable)e);
        }
    }
    
    private void triggerEvent(final NetworkEntity networkEntity) {
        final int eventID = Integer.parseInt(this.m_args[0]);
        final TriggerServerEvent msg = new TriggerServerEvent();
        msg.setEventId(eventID);
        networkEntity.sendMessage(msg);
    }
    
    static {
        ScenarioCommand.m_logger = Logger.getLogger((Class)ScenarioCommand.class);
    }
}
