package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "setPlayerTitle | spt", commandParameters = "&lt;titleId&gt; &lt;\"pseudo\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Give title to player.", commandObsolete = false)
public class SetPlayerTitleCommand extends ModerationCommand
{
    private final int m_titleId;
    private final String m_playerName;
    
    public SetPlayerTitleCommand(final int titleId, final String playerName) {
        super();
        this.m_titleId = titleId;
        this.m_playerName = playerName;
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
        if (!WakfuTranslator.getInstance().containsContentKey(34, this.m_titleId)) {
            ConsoleManager.getInstance().err("Titre inconnu id=" + this.m_titleId);
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)135);
        netMessage.addIntParameter(this.m_titleId);
        netMessage.addStringParameter(this.m_playerName);
        networkEntity.sendMessage(netMessage);
    }
}
