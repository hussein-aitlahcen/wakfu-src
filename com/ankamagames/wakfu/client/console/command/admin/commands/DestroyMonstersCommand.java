package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "destroymonsters | dm", commandParameters = "&lt;monsterId&gt; &lt;qty&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Destroy specified qty of monsters near from character.", commandObsolete = false)
public class DestroyMonstersCommand extends ModerationCommand
{
    private int m_monsterBreedId;
    private final int m_monsterCount;
    
    public DestroyMonstersCommand(final int monsterBreedId) {
        super();
        this.m_monsterBreedId = monsterBreedId;
        this.m_monsterCount = 1;
    }
    
    public DestroyMonstersCommand(final int monsterBreedId, final int monsterCount) {
        super();
        this.m_monsterBreedId = monsterBreedId;
        this.m_monsterCount = monsterCount;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)93);
        netMessage.addIntParameter(this.m_monsterBreedId);
        netMessage.addIntParameter(this.m_monsterCount);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
