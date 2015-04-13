package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

@Documentation(commandName = "createitem | ci", commandParameters = "&lt;itemId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Add item on inventory.", commandObsolete = false)
public class CreateItemCommand extends ModerationCommand
{
    private final int m_referenceId;
    private final short m_quantity;
    
    public CreateItemCommand(final int referenceId, final short quantity) {
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
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        AbstractBag container = null;
        try {
            Item item = null;
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_referenceId);
            if (refItem != null) {
                item = new Item(-1L);
                item.initializeWithReferenceItem(refItem);
                item.setQuantity(this.m_quantity);
                container = character.getBags().getFirstContainerWithFreePlaceFor(item);
                final ModerationCommandMessage netMessage = new ModerationCommandMessage();
                netMessage.setServerId((byte)3);
                netMessage.setCommand((short)14);
                netMessage.addIntParameter(this.m_referenceId);
                netMessage.addShortParameter(this.m_quantity);
                networkEntity.sendMessage(netMessage);
            }
            else {
                ConsoleManager.getInstance().err("ReferenceItem d'Id " + this.m_referenceId + " non trouv\u00e9e. Peut-\u00eatre un probl\u00e8me d'export ?");
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me pour l'ajout d'un item dans le sac courant : " + e);
        }
    }
}
