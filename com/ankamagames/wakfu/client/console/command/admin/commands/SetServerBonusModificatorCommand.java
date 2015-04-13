package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;

@Documentation(commandName = "setBonusFactor | sbf", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "sbf h to show full documentation. ", commandObsolete = false)
public class SetServerBonusModificatorCommand extends ModerationCommand
{
    public static final int FXP = 0;
    public static final int CXP = 1;
    public static final int PP = 2;
    public static final int STATUT = 3;
    public static final int HELP = 4;
    public static final int K = 5;
    public static final int LOOT = 6;
    public static final int GBLF = 7;
    public static final int PVP = 8;
    private final float m_xpFactor;
    private final int m_commandId;
    private List<Integer> m_instanceIdList;
    private String m_startDate;
    private String m_endDate;
    
    public SetServerBonusModificatorCommand() {
        this(3);
    }
    
    public SetServerBonusModificatorCommand(final int commandId) {
        this(commandId, -1.0f);
    }
    
    public SetServerBonusModificatorCommand(final int commandId, final float xpFactor) {
        super();
        this.m_xpFactor = xpFactor;
        this.m_commandId = commandId;
    }
    
    public SetServerBonusModificatorCommand(final int commandId, final float xpFactor, final List<Integer> instanceIdList) {
        this(commandId, xpFactor);
        this.m_instanceIdList = instanceIdList;
    }
    
    public SetServerBonusModificatorCommand(final int commandId, final float xpFactor, final List<Integer> instanceIds, final String startDate, final String endDate) {
        this(commandId, xpFactor, instanceIds);
        this.m_startDate = startDate;
        this.m_endDate = endDate;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
            case 8: {
                return this.m_xpFactor == -1.0f || this.m_xpFactor == 0.0f || (this.m_startDate != null && this.m_endDate != null);
            }
            case 3:
            case 4: {
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
        if (this.m_xpFactor < 0.0f && this.m_xpFactor != -1.0f) {
            ConsoleManager.getInstance().err("Il faut que le facteur d'xp soit >= 0, re\u00e7u=" + this.m_xpFactor);
            return;
        }
        short command = 0;
        switch (this.m_commandId) {
            case 1: {
                command = 136;
                break;
            }
            case 0: {
                command = 39;
                break;
            }
            case 2: {
                command = 137;
                break;
            }
            case 3: {
                command = 138;
                break;
            }
            case 5: {
                command = 163;
                break;
            }
            case 6: {
                command = 164;
                break;
            }
            case 7: {
                command = 165;
                break;
            }
            case 8: {
                command = 238;
                break;
            }
            case 4: {
                final String s = "setBonusFactor|sbf help : show full documentation\nsetBonusFactor|sbf : show current bonuses and provided events\nsetBonusFactor|sbf fxp : show current xp bonus\nsetBonusFactor|sbf fxp instanceId : same for an instance\nsetBonusFactor|sbf fxp -1.0 : reset bonus xp for all the server\nsetBonusFactor|sbf fxp -1.0 instanceId : same for instance\nsetBonusFactor|sbf fxp value startDate endDate : apply value as xp bonus from startDate to endDate\nsetBonusFactor|sbf fxp value startDate endDate instanceId : same for instance\nDate syntax : dd/mm/yyyy hh:mm\nreplace fxp by cxp : craft xp\nreplace fxp by k : money loot\nreplace fxp by loot : number of loots try\nreplace fxp by gblf : bonus learning factor for guilds\nreplace fxp by pp : prospection rate\nreplace fxp by pvp : pvp merit and glory orbs limit\n";
                ConsoleManager.getInstance().log("setBonusFactor|sbf help : show full documentation\nsetBonusFactor|sbf : show current bonuses and provided events\nsetBonusFactor|sbf fxp : show current xp bonus\nsetBonusFactor|sbf fxp instanceId : same for an instance\nsetBonusFactor|sbf fxp -1.0 : reset bonus xp for all the server\nsetBonusFactor|sbf fxp -1.0 instanceId : same for instance\nsetBonusFactor|sbf fxp value startDate endDate : apply value as xp bonus from startDate to endDate\nsetBonusFactor|sbf fxp value startDate endDate instanceId : same for instance\nDate syntax : dd/mm/yyyy hh:mm\nreplace fxp by cxp : craft xp\nreplace fxp by k : money loot\nreplace fxp by loot : number of loots try\nreplace fxp by gblf : bonus learning factor for guilds\nreplace fxp by pp : prospection rate\nreplace fxp by pvp : pvp merit and glory orbs limit\n");
                return;
            }
            default: {
                return;
            }
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand(command);
        netMessage.addFloatParameter(this.m_xpFactor);
        if (this.m_startDate != null) {
            final GameDate startDate = ModerationCommandUtils.getGameDate(this.m_startDate);
            netMessage.addLongParameter(startDate.toLong());
            if (startDate == null) {
                ConsoleManager.getInstance().err("Erreur de syntaxe dans la date de d\u00e9part - taper setBonusFactor help pour voir la syntaxe");
                return;
            }
        }
        if (this.m_endDate != null) {
            final GameDate endDate = ModerationCommandUtils.getGameDate(this.m_endDate);
            netMessage.addLongParameter(endDate.toLong());
            if (endDate == null) {
                ConsoleManager.getInstance().err("Erreur de syntaxe dans la date de fin - taper setBonusFactor help pour voir la syntaxe");
                return;
            }
        }
        if (this.m_instanceIdList != null) {
            for (final Integer instanceId : this.m_instanceIdList) {
                netMessage.addIntParameter(instanceId);
            }
        }
        networkEntity.sendMessage(netMessage);
    }
    
    public static String getHelp() {
        return null;
    }
}
