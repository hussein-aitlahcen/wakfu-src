package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.events.actions.*;

@Documentation(commandName = "clientGameEvent | cge", commandParameters = "&lt;-a | -i&gt; &lt;idEvent&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Enable (-a) or disable (-i) specified event.", commandObsolete = false)
public final class ClientGameEventCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int ACTIVATE = 1;
    public static final int INACTIVATE = 2;
    private final int m_commandId;
    private final String[] m_args;
    
    public ClientGameEventCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
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
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 1: {
                    this.activate();
                    break;
                }
                case 2: {
                    this.inactivate();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private void inactivate() {
        final ClientEventAction action = ClientEventActionType.createFromType(ClientEventActionType.CHANGE_EVENT_ACTIVATION_STATE.getId(), 0);
        final ArrayList<ParserObject> params = new ArrayList<ParserObject>();
        params.add(new ConstantIntegerValue(Integer.parseInt(this.m_args[0])));
        params.add(new ConstantBooleanCriterion(false));
        action.setParams(params);
        action.execute();
    }
    
    private void activate() {
        final ClientEventAction action = ClientEventActionType.createFromType(ClientEventActionType.CHANGE_EVENT_ACTIVATION_STATE.getId(), 0);
        final ArrayList<ParserObject> params = new ArrayList<ParserObject>();
        params.add(new ConstantIntegerValue(Integer.parseInt(this.m_args[0])));
        params.add(new ConstantBooleanCriterion(true));
        action.setParams(params);
        action.execute();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientGameEventCommand.class);
    }
}
