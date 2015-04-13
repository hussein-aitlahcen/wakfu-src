package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "godmode", commandParameters = "&lt;h&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Become a god. godmode h to show full documentation.", commandObsolete = false)
public class GodModeCommand extends ModerationCommand
{
    private final int m_commandId;
    private final String[] m_args;
    
    public GodModeCommand() {
        super();
        this.m_commandId = 1;
        this.m_args = new String[0];
    }
    
    public GodModeCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return this.m_args.length == 0;
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
                case 0: {
                    help();
                    break;
                }
                case 1: {
                    this.all(networkEntity);
                    break;
                }
                default: {
                    this.sendCommandIdToServer(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de place de march\u00e9 " + e);
        }
    }
    
    public static String getHelp() {
        return null;
    }
    
    private static void help() {
        ModerationCommand.log("help | h : show full documentation");
        ModerationCommand.log("-all | -a : execute all the options");
        ModerationCommand.log("-fight | -f : character and spells to max level");
        ModerationCommand.log("-kamas | -k : set money to 999 999 999");
        ModerationCommand.log("-exploit | -ex : get all achievements");
        ModerationCommand.log("-emotes | -em : unlock emotes");
    }
    
    private void all(final NetworkEntity networkEntity) {
        this.sendCommandIdToServer(networkEntity);
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getFields());
    }
    
    private void sendCommandIdToServer(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)30);
        netMessage.addIntParameter(this.m_commandId);
        networkEntity.sendMessage(netMessage);
    }
}
