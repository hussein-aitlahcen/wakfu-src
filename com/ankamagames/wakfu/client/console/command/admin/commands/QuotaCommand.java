package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "quota", commandParameters = "(optionnal) &lt;queue | player&gt; (optionnal) &lt;true | false | number &lt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "quota queue true|false : Activate the connection queue\nquota player n : Define a player limit to n", commandObsolete = false)
public final class QuotaCommand extends ModerationCommand
{
    public static final byte INFO = 0;
    public static final byte QUEUE = 1;
    public static final byte PLAYER = 2;
    private final byte m_type;
    private short m_limit;
    private boolean m_activated;
    
    public QuotaCommand() {
        super();
        this.m_type = 0;
    }
    
    public QuotaCommand(final boolean activated) {
        super();
        this.m_type = 1;
        this.m_activated = activated;
    }
    
    public QuotaCommand(final short limit) {
        super();
        this.m_type = 2;
        this.m_limit = limit;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)1);
        netMessage.setCommand((short)61);
        netMessage.addByteParameter(this.m_type);
        if (this.m_type == 2) {
            netMessage.addShortParameter(this.m_limit);
        }
        else if (this.m_type == 1) {
            netMessage.addBooleanParameter(this.m_activated);
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        networkEntity.sendMessage(netMessage);
    }
}
