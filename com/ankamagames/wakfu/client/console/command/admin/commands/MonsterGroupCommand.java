package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "monsterGroup | mg", commandParameters = "&lt;groupId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Enable specified group.", commandObsolete = false)
public final class MonsterGroupCommand
{
    public static ModerationCommand createActivate(final long groupId) {
        return new Activate(groupId);
    }
    
    @Documentation(commandName = "", commandParameters = "", commandRights = {}, commandDescription = "", commandObsolete = true)
    private static class Activate extends ModerationCommand
    {
        private final long m_groupId;
        
        Activate(final long groupId) {
            super();
            this.m_groupId = groupId;
        }
        
        @Override
        public boolean isValid() {
            return this.m_groupId > 0L;
        }
        
        @Override
        public void execute() {
            final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
            if (networkEntity == null) {
                ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
                return;
            }
            final ModerationCommandMessage netMessage = new ModerationCommandMessage();
            netMessage.setServerId((byte)3);
            netMessage.setCommand((short)85);
            netMessage.addIntParameter((int)this.m_groupId);
            networkEntity.sendMessage(netMessage);
        }
        
        @Override
        public String toString() {
            return "Activate{m_groupId=" + this.m_groupId + '}';
        }
    }
}
