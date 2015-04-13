package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "buff", commandParameters = "&lt;level&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Up current character spells and level to specified level. You can't enter a lower level than your current.", commandObsolete = false)
public class BuffCharacterCommand extends ModerationCommand
{
    private final Short m_level;
    private final byte m_elementId;
    private final int m_spellId;
    
    public BuffCharacterCommand() {
        this((short)(-1));
    }
    
    public BuffCharacterCommand(final Short level) {
        super();
        this.m_level = level;
        this.m_elementId = -1;
        this.m_spellId = -1;
    }
    
    public BuffCharacterCommand(final Short level, final byte elementId) {
        super();
        this.m_elementId = elementId;
        this.m_level = level;
        this.m_spellId = -1;
    }
    
    public BuffCharacterCommand(final Short level, final int spellId) {
        super();
        this.m_level = level;
        this.m_spellId = spellId;
        this.m_elementId = -1;
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
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)60);
        netMessage.addShortParameter(this.m_level);
        netMessage.addByteParameter(this.m_elementId);
        netMessage.addIntParameter(this.m_spellId);
        networkEntity.sendMessage(netMessage);
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getFields());
    }
}
