package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "additemxp", commandParameters = "&lt;qty&gt;", commandDescription = "Give xp to an item.", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandObsolete = false)
public final class AddItemXpCommand extends ModerationCommand
{
    private final long m_xpAmount;
    
    public AddItemXpCommand(final long xpAmount) {
        super();
        this.m_xpAmount = xpAmount;
    }
    
    @Override
    public boolean isValid() {
        return this.m_xpAmount > 0L;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)117);
        netMessage.addLongParameter(this.m_xpAmount);
        networkEntity.sendMessage(netMessage);
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getFields());
    }
}
