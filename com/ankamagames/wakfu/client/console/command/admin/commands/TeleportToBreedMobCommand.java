package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "teleportToMonster |tpToMonster | tptm", commandParameters = "&lt;groupMonsterId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Teleport to monsterGroup.", commandObsolete = false)
public class TeleportToBreedMobCommand extends ModerationCommand
{
    private final short m_breedId;
    
    public TeleportToBreedMobCommand(final short breedId) {
        super();
        this.m_breedId = breedId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)59);
        netMessage.addShortParameter(this.m_breedId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
