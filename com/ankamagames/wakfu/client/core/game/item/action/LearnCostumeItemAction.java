package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public final class LearnCostumeItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    
    LearnCostumeItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String... params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            LearnCostumeItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        final CosmeticsInventory cosmetics = (CosmeticsInventory)character.getInventory(InventoryType.COSMETICS);
        if (cosmetics.getItem(item.getReferenceId()) != null) {
            ErrorsMessageTranslator.getInstance().pushMessage(57, 3, new Object[0]);
            return false;
        }
        if (!item.hasBind() && item.getRentInfo() != null) {
            this.sendRequest(item.getUniqueId());
            final String errorMsg = WakfuTranslator.getInstance().getString("cosmeticsInventory.rentedItem.warning");
            ChatManager.getInstance().pushMessage(errorMsg, 4);
        }
        else {
            final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("cosmetics.costume.confirmation"), 6L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type != 2) {
                        return;
                    }
                    LearnCostumeItemAction.this.sendRequest(item.getUniqueId());
                }
            });
        }
        return true;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.LEARN_COSTUME;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LearnCostumeItemAction.class);
    }
}
