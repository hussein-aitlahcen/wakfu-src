package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "restoreCharacter | rch", commandParameters = "&lt;characterId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Restore a character. He must be deleted.", commandObsolete = false)
public final class RestoreCharacterCommand extends ModerationCommand
{
    private final long m_characterId;
    
    public RestoreCharacterCommand(final long characterId) {
        super();
        this.m_characterId = characterId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand((short)182);
        msg.addLongParameter(this.m_characterId);
        networkEntity.sendMessage(msg);
    }
}
