package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "giveItem | gi ", commandParameters = "&lt;pseudo&gt; &lt;itemIdgt; &lt;qty&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Give item qty to player with specified pseudo.", commandObsolete = false)
public class GiveItemCommand extends ModerationCommand
{
    private final String m_pseudo;
    private final int m_itemId;
    private final short m_stackSize;
    private final AbstractReferenceItem m_refitem;
    
    public GiveItemCommand(final String pseudo, final int itemId, final short stackSize) {
        super();
        this.m_pseudo = pseudo;
        this.m_itemId = itemId;
        this.m_stackSize = stackSize;
        this.m_refitem = ReferenceItemManager.getInstance().getReferenceItem(this.m_itemId);
    }
    
    @Override
    public boolean isValid() {
        return this.m_itemId > 0 && this.m_stackSize >= 1;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)207);
        netMessage.addStringParameter(this.m_pseudo);
        netMessage.addIntParameter(this.m_itemId);
        netMessage.addShortParameter(this.m_stackSize);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
