package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

@Documentation(commandName = "fight", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "fight h to show full documentation.", commandObsolete = false)
public final class FightCommand extends ModerationCommand
{
    public static final int SHOW_BONUS_POINT_SELECTABLE = 0;
    public static final int SELECT_BONUS = 1;
    public static final int HELP = 2;
    public static final int WIN = 3;
    public static final int FLEE = 4;
    public static final int MAKE_FLEE = 5;
    public static final int CREATE_ARCADE = 6;
    public static final int CREATE_BOUFBOWL = 7;
    public static final int COLLECT_FIGHT_RANDOM_VALUE = 8;
    public static final int CREATE_COLLECT_FIGHT = 9;
    public static final int SET_RECONNECTION_TURN_TIMEOUT = 10;
    private final int m_commandId;
    private final String[] m_args;
    
    public FightCommand(final int commandId, final String... args) {
        super();
        this.m_args = args.clone();
        this.m_commandId = commandId;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 9: {
                return this.m_args.length == 0;
            }
            case 1:
            case 6:
            case 8:
            case 10: {
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
                case 0: {
                    showBonusPointSelectable();
                    break;
                }
                case 1: {
                    this.selectBonus();
                    break;
                }
                case 2: {
                    help();
                    break;
                }
                case 3: {
                    win();
                    break;
                }
                case 4: {
                    flee();
                    break;
                }
                case 5: {
                    makeFlee();
                    break;
                }
                case 6: {
                    this.createArcade();
                    break;
                }
                case 7: {
                    this.createBoufbowl();
                    break;
                }
                case 8: {
                    this.collectFightRandomValue();
                    break;
                }
                case 9: {
                    this.createCollectFight();
                    break;
                }
                case 10: {
                    this.setReconnectionTurnTimeout();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de place de march\u00e9 " + e);
        }
    }
    
    private static void help() {
        ModerationCommand.log("fight ('-showBonusPointSelectbable' | '-sbps') : show available timeline bonuses");
        ModerationCommand.log("fight ('-selectBonus' | '-sb') : request to server to select a bonus");
        ModerationCommand.log("fight -win : win the fight by victory of your character");
        ModerationCommand.log("fight -flee : flee of fights, other stay in");
        ModerationCommand.log("fight -ca idDonjon : launch specified arcade fight");
        ModerationCommand.log("fight -cb : launch Boufbawl fight");
        ModerationCommand.log("fight ('-createCollectFight' | '-ccf') : launch capt'chat fight");
        ModerationCommand.log("fight ('-collectFightRandomValue' | '-cfrv') value : change 'normal' percent chance to proc capt'chat fight");
        ModerationCommand.log("fight ('-setReconnectionTurnTimeout' | '-srtt') value : Change le nombre de tour limite pour se reconnecter en combat");
    }
    
    private void setReconnectionTurnTimeout() {
        final int turnTimeoutForReconnection = Integer.parseInt(this.m_args[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)215);
        netMessage.addIntParameter(turnTimeoutForReconnection);
        networkEntity.sendMessage(netMessage);
    }
    
    private void createCollectFight() {
        sendCommandToGameServer((short)194);
    }
    
    private void collectFightRandomValue() {
        final int randomValue = Integer.parseInt(this.m_args[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)193);
        netMessage.addIntParameter(randomValue);
        networkEntity.sendMessage(netMessage);
    }
    
    private static void win() {
        sendCommandToGameServer((short)90);
    }
    
    private void selectBonus() {
        final int effectId = Integer.parseInt(this.m_args[0]);
        final PointEffectSelectionMessage msg = new PointEffectSelectionMessage();
        msg.setEffectId(effectId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private static void showBonusPointSelectable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            ModerationCommand.log("Pas de combat, pas de bonus");
        }
        else {
            final Collection<WakfuEffect> selection = fight.getTimeline().getTimeScoreGauges().getEffectsAvailableForSelection(localPlayer.getId(), fight.getOriginalTeamId(localPlayer));
            for (final WakfuEffect effect : selection) {
                final String effectDesc = WakfuTranslator.getInstance().getString(30, effect.getEffectId(), new Object[0]);
                ModerationCommand.log("Id : " + effect.getEffectId() + " : " + effectDesc);
            }
        }
    }
    
    private static void flee() {
        sendCommandToGameServer((short)102);
    }
    
    private static void makeFlee() {
        sendCommandToGameServer((short)103);
    }
    
    private void createArcade() {
        final int dungeonId = Integer.parseInt(this.m_args[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)120);
        netMessage.addIntParameter(dungeonId);
        networkEntity.sendMessage(netMessage);
    }
    
    private void createBoufbowl() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)125);
        networkEntity.sendMessage(netMessage);
    }
    
    private static void sendCommandToGameServer(final short commandId) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand(commandId);
        networkEntity.sendMessage(netMessage);
    }
}
