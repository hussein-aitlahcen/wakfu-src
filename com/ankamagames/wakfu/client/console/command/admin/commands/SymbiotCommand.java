package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific.*;

@Documentation(commandName = "symbiot", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "symbiot h to show full documentation.", commandObsolete = false)
public class SymbiotCommand extends ModerationCommand
{
    public static final byte ADD = 0;
    public static final byte REMOVE = 1;
    public static final byte RENAME = 2;
    public static final byte CHOOSE_CURRENT_INDEX = 3;
    public static final byte INFO = 4;
    public static final byte HELP = 5;
    private final byte m_commandId;
    private final String[] m_args;
    
    public SymbiotCommand(final byte commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0: {
                return this.m_args.length == 1 || this.m_args.length == 2;
            }
            case 1: {
                return this.m_args.length == 1;
            }
            case 2: {
                return this.m_args.length == 2;
            }
            case 3: {
                return this.m_args.length == 1;
            }
            case 4: {
                return true;
            }
            case 5: {
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
                case 0: {
                    this.add(networkEntity);
                    break;
                }
                case 1: {
                    this.remove();
                    break;
                }
                case 2: {
                    this.rename(networkEntity);
                    break;
                }
                case 3: {
                    this.chooseCurrentIndex(networkEntity);
                    break;
                }
                case 4: {
                    info();
                    break;
                }
                case 5: {
                    help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private static void help() {
        ConsoleManager.getInstance().log("-add breedId : add mosnter in symbiot");
        ConsoleManager.getInstance().log("-add breedId qty : same with qty");
        ConsoleManager.getInstance().log("-rm index : remove monster at index");
        ConsoleManager.getInstance().log("-rename index name : rename monster at index");
        ConsoleManager.getInstance().log("-index index : change selected index");
        ConsoleManager.getInstance().log("-i : show complete info about symbiot");
        ConsoleManager.getInstance().log("h | help : show full command documentation");
    }
    
    private static void info() {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
        final Symbiot symbiot = localPlayer.getSymbiot();
        if (symbiot != null) {
            ConsoleManager.getInstance().customTrace("Index courant : " + symbiot.getCurrentCreatureIndex() + "\n", 11141375);
            for (byte i = 0; i < symbiot.size(); ++i) {
                final BasicInvocationCharacteristics crea = symbiot.getCreatureParametersFromIndex(i);
                ConsoleManager.getInstance().customTrace(crea + "\n", 11141375);
            }
            ConsoleManager.getInstance().customTrace("Creatures disponibles : ", 11141375);
            for (byte i = 0; i < symbiot.size(); ++i) {
                final boolean isAvailable = symbiot.isAvailable(i);
                if (isAvailable) {
                    ConsoleManager.getInstance().customTrace(Byte.toString(i), 11141375);
                }
            }
        }
        else {
            ConsoleManager.getInstance().log("pas de symbiote");
        }
    }
    
    private void add(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)10);
        netMessage.addByteParameter(this.m_commandId);
        netMessage.addIntParameter(Integer.parseInt(this.m_args[0]));
        if (this.m_args.length == 2) {
            netMessage.addIntParameter(Integer.parseInt(this.m_args[1]));
        }
        else {
            netMessage.addIntParameter(1);
        }
        networkEntity.sendMessage(netMessage);
    }
    
    private void remove() {
        final LocalPlayerCharacter localPlayerInfo = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayerInfo != null && localPlayerInfo.getSymbiot() != null) {
            localPlayerInfo.getSymbiot().removeFromIndex(Byte.parseByte(this.m_args[0]));
        }
    }
    
    private void rename(final NetworkEntity networkEntity) {
        final LocalPlayerCharacter localPlayerInfo = WakfuGameEntity.getInstance().getLocalPlayer();
        final String name = this.m_args[1];
        final byte index = Byte.parseByte(this.m_args[0]);
        if (localPlayerInfo.getSymbiot() == null) {
            ConsoleManager.getInstance().err("Pas de symbiote");
            return;
        }
        final BasicInvocationCharacteristics creatureToRename = localPlayerInfo.getSymbiot().getCreatureParametersFromIndex(index);
        if (creatureToRename == null) {
            ConsoleManager.getInstance().err("Pas de creature dispo a l'index fourni");
            return;
        }
        if (!WordsModerator.getInstance().validateName(name)) {
            ConsoleManager.getInstance().err(" Nom invalide");
            return;
        }
        creatureToRename.setName(name);
        final OsamodasSymbiotRenameCreatureMessage msg = new OsamodasSymbiotRenameCreatureMessage();
        msg.setCreatureIndex(index);
        msg.setCreatureName(name);
        networkEntity.sendMessage(msg);
    }
    
    private void chooseCurrentIndex(final NetworkEntity networkEntity) {
        final LocalPlayerCharacter localPlayerInfo = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayerInfo.getSymbiot() == null) {
            ConsoleManager.getInstance().err("Pas de symbiote");
            return;
        }
        final byte index = Byte.parseByte(this.m_args[0]);
        localPlayerInfo.getSymbiot().setCurrentCreatureFromIndex(index);
        ConsoleManager.getInstance().log("Nouvel index courant : " + index);
        localPlayerInfo.updateShortcutBars();
        final OsamodasSymbiotSelectCreatureMessage msg = new OsamodasSymbiotSelectCreatureMessage();
        msg.setCreatureId(index);
        networkEntity.sendMessage(msg);
    }
}
