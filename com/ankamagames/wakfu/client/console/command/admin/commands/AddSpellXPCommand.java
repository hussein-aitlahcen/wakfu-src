package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "addspellxp", commandParameters = "&lt;spellId&gt; &lt;qty&gt;", commandDescription = "Add specified xp to the spell.", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandObsolete = false)
public class AddSpellXPCommand extends ModerationCommand
{
    private final int m_referenceId;
    private final short m_quantity;
    
    public AddSpellXPCommand(final int referenceId, final short quantity) {
        super();
        this.m_referenceId = referenceId;
        this.m_quantity = quantity;
    }
    
    @Override
    public boolean isValid() {
        return this.m_quantity > 0;
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
        netMessage.setCommand((short)23);
        netMessage.addIntParameter(this.m_referenceId);
        netMessage.addShortParameter(this.m_quantity);
        networkEntity.sendMessage(netMessage);
        WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventoryManager().updateSpellsField();
    }
}
