package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;

@Documentation(commandName = "createset | cs ", commandParameters = "&lt;setId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Add specified set on inventory. Id 110 for admin items.", commandObsolete = false)
public class CreateSetCommand extends ModerationCommand
{
    private final short m_setReferenceId;
    
    public CreateSetCommand(final short setReferenceId) {
        super();
        this.m_setReferenceId = setReferenceId;
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
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        try {
            final ItemSet set = ItemSetManager.getInstance().getItemSet(this.m_setReferenceId);
            if (set == null) {
                ConsoleManager.getInstance().err("L'id " + this.m_setReferenceId + " ne correspond \u00e0 aucun set.");
            }
            else {
                for (final ReferenceItem refItem : set) {
                    final short quantity = 1;
                    final Item item = new Item(-1L);
                    item.initializeWithReferenceItem(refItem);
                    item.setQuantity((short)1);
                    final ModerationCommandMessage netMessage = new ModerationCommandMessage();
                    netMessage.setServerId((byte)3);
                    netMessage.setCommand((short)14);
                    netMessage.addIntParameter(refItem.getId());
                    netMessage.addShortParameter((short)1);
                    networkEntity.sendMessage(netMessage);
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(character, "bags");
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me pour l'ajout d'un set dans le sac courant : " + e);
        }
    }
}
