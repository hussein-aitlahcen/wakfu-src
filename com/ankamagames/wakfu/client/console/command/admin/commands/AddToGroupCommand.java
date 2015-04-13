package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "addtogroup | atg", commandParameters = "&lt;groupId&gt; &lt;monsterId&gt; &lt;qty&gt;", commandDescription = "Add qty monsters in specified monster group.", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandObsolete = false)
public class AddToGroupCommand extends ModerationCommand
{
    private final long m_groupId;
    private final int m_characterId;
    private final int m_characterQuantity;
    
    public AddToGroupCommand(final long groupId, final int characterId, final int characterQuantity) {
        super();
        this.m_groupId = groupId;
        this.m_characterId = characterId;
        this.m_characterQuantity = characterQuantity;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)12);
        netMessage.addLongParameter(this.m_groupId);
        netMessage.addIntParameter(this.m_characterId);
        netMessage.addIntParameter(this.m_characterQuantity);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
