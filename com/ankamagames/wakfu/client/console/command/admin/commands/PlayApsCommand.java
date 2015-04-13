package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "playaps | paps", commandParameters = "&lt;characterId&gt; &lt;apsId&gt; &lt;duration&gt; (&lt;follow&gt;)", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Play APD. Follow = true by default.", commandObsolete = false)
public final class PlayApsCommand extends ModerationCommand
{
    private final long m_characterId;
    private final int m_apsId;
    private final int m_duree;
    private final boolean m_follow;
    
    public PlayApsCommand(final long characterId, final int apsId, final int duree) {
        this(characterId, apsId, duree, true);
    }
    
    public PlayApsCommand(final long characterId, final int apsId, final int duree, final boolean follow) {
        super();
        this.m_characterId = characterId;
        this.m_apsId = apsId;
        this.m_duree = duree;
        this.m_follow = follow;
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
        try {
            final ModerationCommandMessage netMessage = new ModerationCommandMessage();
            netMessage.setServerId((byte)3);
            netMessage.setCommand((short)43);
            netMessage.addLongParameter(this.m_characterId);
            netMessage.addIntParameter(this.m_apsId);
            netMessage.addIntParameter(this.m_duree);
            netMessage.addBooleanParameter(this.m_follow);
            networkEntity.sendMessage(netMessage);
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Error trying to play aps : " + e);
        }
    }
}
