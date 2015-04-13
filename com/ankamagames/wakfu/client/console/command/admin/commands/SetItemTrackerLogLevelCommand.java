package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "setItemTrackerLogLevel | sitll", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "sitll h to show full documentation. Manage logging level of an item.", commandObsolete = false)
public final class SetItemTrackerLogLevelCommand extends ModerationCommand
{
    private final int m_logLevel;
    
    public SetItemTrackerLogLevelCommand() {
        this(-1);
    }
    
    public SetItemTrackerLogLevelCommand(final int logLevel) {
        super();
        this.m_logLevel = logLevel;
    }
    
    @Override
    public boolean isValid() {
        final int realLevelValue = this.getRealLogLevel();
        return this.m_logLevel == -1 || realLevelValue == 50000 || realLevelValue == 40000 || realLevelValue == 30000 || realLevelValue == 20000 || realLevelValue == 10000;
    }
    
    private int getRealLogLevel() {
        return this.m_logLevel * 10000;
    }
    
    @Override
    public void execute() {
        if (this.m_logLevel == -1) {
            final String s = "setitemtrackerloglevel|sitll help|h : show full documentation\nsetitemtrackerloglevel|sitll levelId : set logging level\nPossible values :\n1 : DEBUG\n2 : INFO\n3 : WARN\n4 : ERROR\n5 : FATAL\n";
            ConsoleManager.getInstance().log("setitemtrackerloglevel|sitll help|h : show full documentation\nsetitemtrackerloglevel|sitll levelId : set logging level\nPossible values :\n1 : DEBUG\n2 : INFO\n3 : WARN\n4 : ERROR\n5 : FATAL\n");
            return;
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)186);
        msg.addIntParameter(this.getRealLogLevel());
        networkEntity.sendMessage(msg);
    }
}
