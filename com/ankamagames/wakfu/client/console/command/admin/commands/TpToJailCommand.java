package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "teleportToJail | tpToJail | ttj", commandParameters = "&lt;pseudo&gt; (&lt;time&gt; &lt;IG |IRL)", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Send player to moderation jail. Time is facultative, but if you give time param you need to give also time type : IG or IRL. The time is in minutes but rounded top ten.", commandObsolete = false)
public class TpToJailCommand extends ModerationCommand
{
    private final String m_pseudo;
    private int m_time;
    private final short m_commandType;
    
    public TpToJailCommand(final String pseudo, final int time, final String igIrl) {
        super();
        this.m_pseudo = pseudo;
        this.m_time = time;
        this.m_commandType = (short)("IG".equals(igIrl) ? 201 : 202);
    }
    
    public TpToJailCommand(final String pseudo) {
        super();
        this.m_pseudo = pseudo;
        this.m_commandType = 200;
    }
    
    @Override
    public boolean isValid() {
        if (!this.m_pseudo.isEmpty()) {
            if (this.m_time > 0) {
                if (this.m_commandType != 202) {
                    if (this.m_commandType != 201) {
                        return false;
                    }
                }
            }
            else if (this.m_commandType != 200) {
                return false;
            }
            return true;
        }
        return false;
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
        netMessage.setCommand((short)212);
        netMessage.addShortParameter(this.m_commandType);
        netMessage.addStringParameter(this.m_pseudo);
        if (this.m_time > 0) {
            netMessage.addIntParameter(this.m_time);
        }
        networkEntity.sendMessage(netMessage);
    }
}
