package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "rentItem | ri", commandParameters = "&lt;itemId&gt; &lt;type&gt; &lt;time&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Type = 1 for fights count, = 2 for time count. Rent an item.", commandObsolete = false)
public final class RentItemCommand extends ModerationCommand
{
    private final int m_itemRefId;
    private final int m_rentType;
    private final long m_rentDuration;
    
    public RentItemCommand(final int itemRefId, final int rentType, final long rentDuration) {
        super();
        this.m_itemRefId = itemRefId;
        this.m_rentType = rentType;
        this.m_rentDuration = rentDuration;
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
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)160);
        netMessage.addIntParameter(this.m_itemRefId);
        netMessage.addIntParameter(this.m_rentType);
        netMessage.addLongParameter(this.m_rentDuration);
        networkEntity.sendMessage(netMessage);
    }
}
