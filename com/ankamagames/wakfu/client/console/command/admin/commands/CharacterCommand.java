package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;

@Documentation(commandName = "character | ch", commandParameters = "&lt;setVisibility | dnd&gt; &lt;0 | 1&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "To become invisible or appear as if you're not online (but still visible).", commandObsolete = false)
public final class CharacterCommand extends ModerationCommand
{
    public static final int HELP = 0;
    public static final int SET_SEX = 1;
    public static final int SET_VISIBILITY = 2;
    public static final int DO_NOT_DISTURB = 3;
    public static final int INFO = 4;
    private final int m_commandId;
    private final String[] m_args;
    
    public CharacterCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 4: {
                return true;
            }
            case 1:
            case 2:
            case 3: {
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
                    break;
                }
                case 1: {
                    this.setSex(networkEntity);
                    break;
                }
                case 2: {
                    this.setVisibility(networkEntity);
                    break;
                }
                case 3: {
                    this.doNotDisturb(networkEntity);
                    break;
                }
                case 4: {
                    this.info(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private void info(final NetworkEntity networkEntity) {
        final ModerationCommandMessage worldMessage = new ModerationCommandMessage();
        worldMessage.setServerId((byte)3);
        worldMessage.setCommand((short)185);
        networkEntity.sendMessage(worldMessage);
    }
    
    private void doNotDisturb(final NetworkEntity networkEntity) {
        final ModerationCommandMessage worldMessage = new ModerationCommandMessage();
        worldMessage.setServerId((byte)2);
        worldMessage.setCommand((short)184);
        worldMessage.addByteParameter(Byte.parseByte(this.m_args[0]));
        networkEntity.sendMessage(worldMessage);
    }
    
    private void setVisibility(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)183);
        msg.addByteParameter(Byte.parseByte(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void setSex(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)162);
        msg.addByteParameter(Byte.parseByte(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void help() {
        ModerationCommand.log("'character'|'ch' 'help'|'h': Affiche l'aide de la commande");
        ModerationCommand.log("'character'|'ch' 'info': Affiche les infos concernant le personnage");
        ModerationCommand.log("'character'|'ch' 'setSex' sexId : permet de changer le sexe de son personnage");
        ModerationCommand.log("'character'|'ch' 'setVisibility' '0'|'1' : permet de rendre son personnage visible ou invisible");
        ModerationCommand.log("'character'|'ch' 'doNotDisturb'|'dnd' '0'|'1' : permet de passer en mode do not disturb afin de ne plus recevoir de demande d'echange, de combat... Cette modif est valable entre chaque d\u00e9co/reco et n'est annul\u00e9e que par un red\u00e9marrage serveur ou un ch dnd 0");
    }
    
    @Override
    public String toString() {
        return "CharacterCommand{m_commandId=" + this.m_commandId + ", m_args=" + Arrays.toString(this.m_args) + '}';
    }
}
