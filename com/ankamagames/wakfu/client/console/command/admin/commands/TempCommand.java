package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar.*;

@Documentation(commandName = "", commandParameters = "", commandRights = {}, commandDescription = "", commandObsolete = true)
public class TempCommand extends ModerationCommand
{
    protected static Logger m_logger;
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        validateRegistration(networkEntity);
    }
    
    private static void validateRegistration(final NetworkEntity networkEntity) {
        final ValidateParticipationMessage message = new ValidateParticipationMessage();
        message.setCharacterId(591892L);
        message.setEventUid(0L);
        networkEntity.sendMessage(message);
    }
    
    private static void register(final NetworkEntity networkEntity) {
        final RegisterToEventRequestMessage request = new RegisterToEventRequestMessage();
        request.setEvent(0L);
        networkEntity.sendMessage(request);
    }
    
    private static void unregister(final NetworkEntity networkEntity) {
        final UnregisterToEventRequestMessage unregister = new UnregisterToEventRequestMessage();
        unregister.setEvent(5L);
        networkEntity.sendMessage(unregister);
    }
    
    static {
        TempCommand.m_logger = Logger.getLogger((Class)TempCommand.class);
    }
}
