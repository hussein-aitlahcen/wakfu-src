package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "protector | pr", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "protector h to show full documentation.", commandObsolete = false)
public final class ProtectorCommand extends ModerationCommand
{
    public static final int HELP = 1;
    public static final int CHANGE_NATION = 2;
    public static final int ADD_MONEY = 3;
    public static final int CHANGE_KAMA_QUEST_CD = 4;
    public static final int CHANGE_KAMA_QUEST_RATIO = 5;
    private final int m_commandId;
    private final String[] m_args;
    
    public ProtectorCommand(final int commandId, final String... args) {
        super();
        this.m_args = args;
        this.m_commandId = commandId;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 2:
            case 3: {
                return this.m_args.length == 2;
            }
            case 4:
            case 5: {
                return this.m_args.length == 1;
            }
            case 1: {
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
                case 2: {
                    this.changeNation(networkEntity);
                    break;
                }
                case 3: {
                    this.addMoney(networkEntity);
                    break;
                }
                case 4: {
                    this.changeKamaQuestCd(networkEntity);
                    break;
                }
                case 5: {
                    this.changeKamaQuestRatio(networkEntity);
                    break;
                }
                case 1: {
                    help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private void changeKamaQuestRatio(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)132);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void changeKamaQuestCd(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)133);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private static void help() {
        ModerationCommand.log("(-changeNation | -cn) protectorId newNationId : change nation of protector\r\n(-addMoney | -am) protectorId amount : give money to protector\r\n");
    }
    
    private void changeNation(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)84);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addIntParameter(Integer.parseInt(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private void addMoney(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)88);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addIntParameter(Integer.parseInt(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
}
