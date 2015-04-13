package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "mute", commandParameters = "&lt;accountId&gt; &lt;time in minuts&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Mute specified player.", commandObsolete = false)
public class MuteCommand extends ModerationCommand
{
    public static final int MUTE = 0;
    public static final int UNMUTE = 1;
    public static final int INFO = 2;
    private final int m_commandId;
    private final long m_clientId;
    private final int m_minutes;
    
    public MuteCommand(final int commandId) {
        this(commandId, -1L, -1);
    }
    
    public MuteCommand(final int commandId, final long clientId, final int minutes) {
        super();
        this.m_commandId = commandId;
        this.m_clientId = clientId;
        this.m_minutes = minutes;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0: {
                return this.m_clientId > 0L && this.m_minutes > 0;
            }
            case 1: {
                return this.m_clientId > 0L && this.m_minutes == -1;
            }
            case 2: {
                return this.m_clientId == -1L && this.m_minutes == -1;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)140);
        netMessage.addLongParameter(this.m_clientId);
        netMessage.addIntParameter(this.m_minutes);
        networkEntity.sendMessage(netMessage);
    }
}
