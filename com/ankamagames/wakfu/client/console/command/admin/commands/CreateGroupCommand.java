package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "creategroup | cg", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "cg help tho show full documentation. Permit to manage monster groups.", commandObsolete = false)
public class CreateGroupCommand extends ModerationCommand
{
    public static final byte HELP = 0;
    public static final byte ECO_GROUP = 1;
    public static final byte TEMPLATE_GROUP = 2;
    public static final byte USER_GROUP = 3;
    public static final byte USER_GROUP_INITIALIZE = 4;
    private final byte m_commandType;
    private final int m_id;
    private final int m_nbGroup;
    
    public CreateGroupCommand(final byte commandType, final int id) {
        super();
        this.m_commandType = commandType;
        this.m_id = id;
        this.m_nbGroup = 1;
    }
    
    public CreateGroupCommand(final byte commandType, final int id, final int nbGroup) {
        super();
        this.m_commandType = commandType;
        this.m_id = id;
        this.m_nbGroup = nbGroup;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        if (this.m_commandType == 0) {
            help();
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand(this.getCommand());
        netMessage.addIntParameter(this.m_id);
        netMessage.addIntParameter(this.m_nbGroup);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private static void help() {
        ModerationCommand.log("id : create a monster of given id not far from character");
        ModerationCommand.log("id x : same with X monsters");
        ModerationCommand.log("-t id : add given templateId");
        ModerationCommand.log("-t id x : same with X templates");
        ModerationCommand.log("-ut id : add usergroup with template ID");
        ModerationCommand.log("-ut id x : same with X usergroups");
        ModerationCommand.log("-ugi id : init usergroup ID in current instance");
        ModerationCommand.log("-ugi cn : DEPRECATED init Black Crow on Kelba");
    }
    
    private short getCommand() {
        switch (this.m_commandType) {
            case 1: {
                return 11;
            }
            case 3: {
                return 16;
            }
            case 2: {
                return 17;
            }
            case 4: {
                return 119;
            }
            default: {
                final String msg = "Type de groupe " + this.m_commandType + " inconnu";
                ConsoleManager.getInstance().err(msg);
                throw new UnsupportedOperationException(msg);
            }
        }
    }
}
