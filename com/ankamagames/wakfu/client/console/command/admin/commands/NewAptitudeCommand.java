package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.aptitude.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;

@Documentation(commandName = "aptitude | apt", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "aptitude help to show full documentation. Permit to manage a aptitude system.", commandObsolete = false)
public final class NewAptitudeCommand extends ModerationCommand
{
    public static final int HELP = 0;
    public static final int SHOW = 1;
    public static final int SHOW_SERVER_INFO = 2;
    public static final int SET_LEVEL = 3;
    public static final int ADD_LEVEL_LEGIT = 4;
    public static final int RESTAT = 5;
    public static final int RESTAT_COMMON = 6;
    public static final int ADD_OLD_APTITUDE_LEVEL = 7;
    private final int m_commandId;
    private final String[] m_args;
    
    public NewAptitudeCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6: {
                return this.m_args.length == 0;
            }
            case 3:
            case 4:
            case 7: {
                return this.m_args.length == 2;
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
                    help();
                    break;
                }
                case 1: {
                    show();
                    break;
                }
                case 2: {
                    showServerInfo(networkEntity);
                    break;
                }
                case 3: {
                    this.setLevel(networkEntity);
                    break;
                }
                case 4: {
                    this.addLevelLegit(networkEntity);
                    break;
                }
                case 5: {
                    restat(networkEntity);
                    break;
                }
                case 7: {
                    this.addOldAptitudeLevel(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private void addOldAptitudeLevel(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)237);
        msg.addShortParameter(Short.parseShort(this.m_args[0]));
        msg.addShortParameter(Short.parseShort(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private static void restat(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)124);
        networkEntity.sendMessage(netMessage);
    }
    
    private void addLevelLegit(final NetworkEntity networkEntity) {
        final LevelUpNewAptitudeRequestMessage requestMessage = new LevelUpNewAptitudeRequestMessage();
        final int bonusId = Integer.parseInt(this.m_args[0]);
        final short level = Short.parseShort(this.m_args[1]);
        final TIntShortHashMap modifs = new TIntShortHashMap();
        modifs.put(bonusId, level);
        requestMessage.setAptitudeModifications(modifs);
        networkEntity.sendMessage(requestMessage);
    }
    
    private void setLevel(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)232);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addShortParameter(Short.parseShort(this.m_args[1]));
        networkEntity.sendMessage(msg);
    }
    
    private static void showServerInfo(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)231);
        networkEntity.sendMessage(msg);
    }
    
    private static void show() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            ModerationCommand.log("No info, please select a character");
            return;
        }
        final AptitudeBonusInventory aptitudeBonusInventory = localPlayer.getAptitudeBonusInventory();
        ModerationCommand.log(aptitudeBonusInventory.toString());
    }
    
    private static void help() {
        ModerationCommand.log("('aptitude'|'apt') ('help'|'h') : display command help");
        ModerationCommand.log("('aptitude'|'apt') ('show'|'s') : display client personnals aptitude bonuses");
        ModerationCommand.log("('aptitude'|'apt') ('showServerInfo'|'ssi') : display server personnals aptitude bonuses");
        ModerationCommand.log("('aptitude'|'apt') ('setLevel'|'sl') bonusId level : set the level of the aptitude bonus");
        ModerationCommand.log("('aptitude'|'apt') ('addLevelLegit'|'all') bonusId level : add level to the aptitude bonus with limit check");
        ModerationCommand.log("('aptitude'|'apt') ('restat') : reset all aptitudes");
        ModerationCommand.log("('aptitude'|'apt') ('addOldAptitudeLevel' | 'aoal') aptitudeId level : Add level to an old aptitude");
    }
}
