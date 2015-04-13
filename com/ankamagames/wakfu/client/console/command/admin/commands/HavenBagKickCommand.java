package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "havenBagKick | hbk", commandParameters = "&lt;characterId | pseudo&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Kick the haven bag of specified player. With pseudo only for connected character. With characterId only on the same game server.", commandObsolete = false)
public class HavenBagKickCommand extends ModerationCommand
{
    private final long m_characterId;
    private final String m_characterPseudo;
    
    public HavenBagKickCommand(final long s) {
        super();
        this.m_characterId = s;
        this.m_characterPseudo = null;
    }
    
    public HavenBagKickCommand(final String s) {
        super();
        this.m_characterPseudo = s;
        this.m_characterId = 0L;
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterId > 0L || this.m_characterPseudo != null;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        if (this.m_characterPseudo == null && this.m_characterId > 0L) {
            netMessage.setServerId((byte)3);
            netMessage.setCommand((short)199);
            netMessage.addLongParameter(this.m_characterId);
        }
        else if (this.m_characterPseudo != null) {
            netMessage.setServerId((byte)2);
            netMessage.setCommand((short)212);
            netMessage.addShortParameter((short)199);
            netMessage.addStringParameter(this.m_characterPseudo);
        }
        networkEntity.sendMessage(netMessage);
    }
}
