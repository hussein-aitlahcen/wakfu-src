package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "pvp", commandParameters = "&lt:help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "pvp h to show full documentation. Permit to manage PvP", commandObsolete = false)
public class PvpCommand extends ModerationCommand
{
    public static final byte HELP = 0;
    public static final byte SET_POINTS = 1;
    public static final byte ADD_POINTS = 2;
    public static final byte SET_STATE = 3;
    public static final byte RECOMPUTE_POINTS = 4;
    public static final byte RESET_REGRESSION = 5;
    public static final byte PVP_ADD_MONEY = 6;
    private final byte m_action;
    private final String[] m_params;
    
    public PvpCommand() {
        this((byte)0, new String[0]);
    }
    
    public PvpCommand(final byte command, final String... args) {
        super();
        this.m_action = command;
        this.m_params = args;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        switch (this.m_action) {
            case 0: {
                help();
                break;
            }
            case 2: {
                this.pvpAddPoints();
                break;
            }
            case 1: {
                this.pvpSetPoints();
                break;
            }
            case 3: {
                this.pvpSetState();
                break;
            }
            case 4: {
                pvpForceRecomputePoints();
                break;
            }
            case 5: {
                pvpResetRegression();
                break;
            }
            case 6: {
                this.pvpAddMoney();
                break;
            }
        }
    }
    
    private static void help() {
        ConsoleManager.getInstance().log("pvp :");
        ConsoleManager.getInstance().log("pvp help | h : show this documentation");
        ConsoleManager.getInstance().log("pvp set <amount> : set player's PvP points");
        ConsoleManager.getInstance().log("pvp add <amount> : add amount to player's PvP points");
        ConsoleManager.getInstance().log("pvp enable <on|off> : enable/disable the PvP");
        ConsoleManager.getInstance().log("pvp recomputepoints | rp : start the weekly points calc /!\\ ranks are recomputed WITH the weekly loss of points");
        ConsoleManager.getInstance().log("pvp rr : reset the regression score of all the players");
        ConsoleManager.getInstance().log("pvp addMoney | am : add PvP money");
    }
    
    private void pvpAddMoney() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)236);
        netMessage.addIntParameter(PrimitiveConverter.getInteger(this.m_params[0]));
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private void pvpAddPoints() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)218);
        netMessage.addIntParameter(PrimitiveConverter.getInteger(this.m_params[0]));
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private void pvpSetPoints() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)217);
        netMessage.addIntParameter(PrimitiveConverter.getInteger(this.m_params[0]));
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private void pvpSetState() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)219);
        if ("on".equalsIgnoreCase(this.m_params[0])) {
            netMessage.addBooleanParameter(true);
        }
        else {
            netMessage.addBooleanParameter(false);
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private static void pvpForceRecomputePoints() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)220);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private static void pvpResetRegression() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)6);
        netMessage.setCommand((short)221);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
