package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "check", commandParameters = "&lt;-u&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Give informations about current partition usergroups.", commandObsolete = false)
public final class CheckCommand extends ModerationCommand
{
    public static final int HELP = 0;
    public static final int USER_GROUP_BY_ID = 1;
    public static final int USER_GROUPS = 2;
    private final int m_commandId;
    private final String[] m_args;
    
    public CheckCommand(final int commandId, final String... args) {
        super();
        this.m_args = args;
        this.m_commandId = commandId;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 1: {
                return this.m_args.length == 1;
            }
            case 0:
            case 2: {
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
                    this.checkUserGroupById(networkEntity);
                    break;
                }
                case 2: {
                    this.checkUserGroups(networkEntity);
                    break;
                }
                case 0: {
                    this.help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de check " + e);
        }
    }
    
    private void help() {
        ModerationCommand.log("(--userGroup | -u) Donne des informations sur les UserGroup de la partition courante.\r\n");
    }
    
    private void checkUserGroupById(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)95);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void checkUserGroups(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)96);
        networkEntity.sendMessage(msg);
    }
}
