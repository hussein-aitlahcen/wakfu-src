package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "regenerateWithItem | regenWithItem | rwi", commandParameters = "&lt;itemId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Regenerate with an inventory item.", commandObsolete = false)
public final class RegenerateWithItemCommand extends ModerationCommand
{
    private final int m_itemRefId;
    
    public RegenerateWithItemCommand(final int itemRefId) {
        super();
        this.m_itemRefId = itemRefId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final RegenWithItemRequestMessage message = new RegenWithItemRequestMessage(this.m_itemRefId, WakfuGameEntity.getInstance().getLocalPlayer().getId());
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
