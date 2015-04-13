package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "addmoney", commandParameters = "&lt;qty&gt;", commandDescription = "Give specified money quantity.", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandObsolete = false)
public class AddMoneyCommand extends ModerationCommand
{
    private final int m_amount;
    private static int m_maxAmountToAdd;
    
    public AddMoneyCommand(final int moneyAmount) {
        super();
        this.m_amount = moneyAmount;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        if (Math.abs(this.m_amount) > AddMoneyCommand.m_maxAmountToAdd) {
            ConsoleManager.getInstance().err("Unable to add this amount. max amount at a time is " + AddMoneyCommand.m_maxAmountToAdd);
            return;
        }
        try {
            final ModerationCommandMessage netMessage = new ModerationCommandMessage();
            netMessage.setServerId((byte)3);
            netMessage.setCommand((short)25);
            netMessage.addIntParameter(this.m_amount);
            networkEntity.sendMessage(netMessage);
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Error trying to add kamas to wallet : " + e);
        }
    }
    
    static {
        AddMoneyCommand.m_maxAmountToAdd = 1000000000;
    }
}
