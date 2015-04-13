package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.common.game.nation.government.*;

@Documentation(commandName = "nation", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Use nation help to show full documentation.", commandObsolete = false)
public class NationCommand extends ModerationCommand
{
    private int m_nationId;
    private final byte m_cmd;
    private long m_param;
    
    public NationCommand(final byte cmd) {
        super();
        this.m_cmd = cmd;
    }
    
    public NationCommand(final int nationId, final byte cmd) {
        super();
        this.m_nationId = nationId;
        this.m_cmd = cmd;
    }
    
    public NationCommand(final int nationId, final byte cmd, final long param) {
        super();
        this.m_nationId = nationId;
        this.m_cmd = cmd;
        this.m_param = param;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        switch (this.m_cmd) {
            case 1:
            case 2:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16: {
                netMessage.setServerId((byte)3);
                break;
            }
            case 3:
            case 4:
            case 5:
            case 10: {
                netMessage.setServerId((byte)6);
                break;
            }
            case 15: {
                help();
                return;
            }
            case 17: {
                showRanks();
                return;
            }
        }
        int nationIdTosend;
        if (this.m_nationId < 0) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer == null) {
                return;
            }
            nationIdTosend = localPlayer.getCitizenComportment().getNationId();
        }
        else {
            nationIdTosend = this.m_nationId;
        }
        netMessage.setCommand((short)74);
        netMessage.addIntParameter(nationIdTosend);
        netMessage.addByteParameter(this.m_cmd);
        netMessage.addLongParameter(this.m_param);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    private static void showRanks() {
        final NationRank[] ranks = NationRank.values();
        for (int i = 0; i < ranks.length; ++i) {
            final NationRank rank = ranks[i];
            ModerationCommand.log(rank.toString());
        }
    }
    
    private static void help() {
        ModerationCommand.log("\"\" : show info about nation");
        ModerationCommand.log("set nationId : set nation of current character");
        ModerationCommand.log("vote info : show info about current vote");
        ModerationCommand.log("vote info nationId : same with another nation");
        ModerationCommand.log("vote start : launch vote");
        ModerationCommand.log("vote start nationId : same with another nation");
        ModerationCommand.log("vote end : finish vote");
        ModerationCommand.log("vote end nationId : same with another nation");
        ModerationCommand.log("cp amount : give amount of citizenship ");
        ModerationCommand.log("al nationId alignementId : change nation alignment");
        ModerationCommand.log("(goInPrison | gp) nationId : launch prison feature for nationId");
        ModerationCommand.log("(offenseRem | or) nationId : remove outlaw in nationId");
        ModerationCommand.log("(offenseAdd | oa) nationId : set outlaw in nationId");
        ModerationCommand.log("switchPasseport | sp : Enable or disable passport");
        ModerationCommand.log("setRank rankId : Change character rank");
        ModerationCommand.log("showRanks : show ranks");
    }
}
