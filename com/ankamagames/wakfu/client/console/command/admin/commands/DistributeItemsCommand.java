package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "distributeitems", commandParameters = "&lt;%numeric&gt; &lt;itemId&gt; &lt;qty&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Distribute specified item in an area defined by %numeric", commandObsolete = false)
public class DistributeItemsCommand extends ModerationCommand
{
    private final int m_radius;
    private final int m_itemId;
    private final short m_stackSize;
    private final AbstractReferenceItem m_refItem;
    
    public DistributeItemsCommand(final int radius, final int itemId, final short stackSize) {
        super();
        this.m_radius = radius;
        this.m_itemId = itemId;
        this.m_stackSize = stackSize;
        this.m_refItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_itemId);
    }
    
    @Override
    public boolean isValid() {
        return this.m_radius >= 0 && this.m_itemId > 0 && this.m_stackSize >= 1;
    }
    
    @Override
    public void execute() {
        final int nbPartitions = (2 * this.m_radius + 1) * (2 * this.m_radius + 1);
        final String itemName = (this.m_refItem != null) ? this.m_refItem.getName() : "!Item Not Found!";
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox("<b>Attention !\n\nVous \u00eates sur le point de distribuer un objet \u00e0 tous des joueurs se trouvant sur un total d'environ " + nbPartitions + " partitions, \u00eates vous s\u00fbr ?\n\nObjet distribu\u00e9 :</b>\n" + this.m_stackSize + "x " + itemName + " [id:" + this.m_itemId + "]", WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        if (messageBoxControler != null) {
            messageBoxControler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        DistributeItemsCommand.this.sendCommand();
                    }
                }
            });
        }
    }
    
    void sendCommand() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)55);
        netMessage.addIntParameter(this.m_radius);
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
