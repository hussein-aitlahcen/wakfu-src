package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "craft", commandParameters = "&lt;--help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "craft --help to show full documentation. Used for manage jobs.", commandObsolete = false)
public final class CraftCommand extends ModerationCommand
{
    public static final int HELP = 0;
    public static final int LEARN = 1;
    public static final int UNLEARN = 2;
    public static final int ADD_XP = 3;
    private final int m_commandId;
    private final String[] m_args;
    
    public CraftCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 1: {
                return this.m_args.length == 1;
            }
            case 2: {
                return this.m_args.length == 1 || this.m_args.length == 0;
            }
            case 3: {
                return this.m_args.length == 2;
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
                    this.learnCraft(networkEntity);
                    break;
                }
                case 2: {
                    this.unlearnCraft(networkEntity);
                    break;
                }
                case 3: {
                    this.addXp(networkEntity);
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
        ModerationCommand.log("(--learn | -l) + Id_m\u00e9tier : learn this job.\r\n(--addXp | -ax) + Id_m\u00e9tier + xp : add or remove xp from a job\r\n(--help | -h) : show the help");
    }
    
    private void learnCraft(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)97);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void unlearnCraft(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)98);
        msg.addIntParameter((this.m_args.length == 0) ? -1 : Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void addXp(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)99);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addLongParameter(Long.parseLong(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
}
