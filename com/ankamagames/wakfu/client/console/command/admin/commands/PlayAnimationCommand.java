package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "playAnimation | panim | playanim", commandParameters = "&lt;characterId&gt; &lt;animationId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Play animation.", commandObsolete = false)
public final class PlayAnimationCommand extends ModerationCommand
{
    private final long m_characterId;
    private final String m_linkAnimation;
    
    public PlayAnimationCommand(final long characterId, final String linkAnimation) {
        super();
        this.m_characterId = characterId;
        this.m_linkAnimation = linkAnimation;
    }
    
    @Override
    public boolean isValid() {
        return this.m_linkAnimation != null && this.m_characterId != 0L;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            final ModerationCommandMessage netMessage = new ModerationCommandMessage();
            netMessage.setServerId((byte)3);
            netMessage.setCommand((short)42);
            netMessage.addLongParameter(this.m_characterId);
            netMessage.addStringParameter(this.m_linkAnimation);
            networkEntity.sendMessage(netMessage);
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Error trying to play animation : " + e);
        }
    }
}
