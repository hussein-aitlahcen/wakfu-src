package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "setLevel | setLvl", commandParameters = "&lt;level&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Set player level.", commandObsolete = false)
public final class SetLevelCommand extends ModerationCommand
{
    private final short m_level;
    
    public SetLevelCommand(final short level) {
        super();
        this.m_level = level;
    }
    
    @Override
    public boolean isValid() {
        return CharacterXpTable.getInstance().isLevelValid(this.m_level);
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
        netMessage.setCommand((short)44);
        netMessage.addShortParameter(this.m_level);
        networkEntity.sendMessage(netMessage);
    }
}
