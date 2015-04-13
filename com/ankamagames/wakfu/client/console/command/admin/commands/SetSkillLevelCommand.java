package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "", commandParameters = "", commandRights = {}, commandDescription = "", commandObsolete = true)
public class SetSkillLevelCommand extends ModerationCommand
{
    private final int m_skillId;
    private final short m_level;
    
    public SetSkillLevelCommand(final int skillId, final short level) {
        super();
        this.m_skillId = skillId;
        this.m_level = level;
    }
    
    @Override
    public boolean isValid() {
        return this.m_level > 0;
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
        netMessage.setCommand((short)78);
        netMessage.addIntParameter(this.m_skillId);
        netMessage.addShortParameter(this.m_level);
        networkEntity.sendMessage(netMessage);
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getFields());
    }
}
